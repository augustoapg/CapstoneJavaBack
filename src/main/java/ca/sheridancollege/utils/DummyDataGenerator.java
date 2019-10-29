package ca.sheridancollege.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.github.javafaker.Faker;

import ca.sheridancollege.beans.Basket;
import ca.sheridancollege.beans.Bike;
import ca.sheridancollege.beans.Customer;
import ca.sheridancollege.beans.LockItem;
import ca.sheridancollege.beans.Rental;
import ca.sheridancollege.beans.RentalComponent;
import ca.sheridancollege.beans.SystemUser;
import ca.sheridancollege.dao.BasketDAO;
import ca.sheridancollege.dao.BikeDAO;
import ca.sheridancollege.dao.CustomerDAO;
import ca.sheridancollege.dao.LockDAO;
import ca.sheridancollege.dao.RentalDAO;
import ca.sheridancollege.dao.SystemUserDAO;
import ca.sheridancollege.enums.BasketState;
import ca.sheridancollege.enums.BikeState;
import ca.sheridancollege.enums.CustomerType;
import ca.sheridancollege.enums.LockState;

public class DummyDataGenerator {

	Faker faker = new Faker(new Locale("en-CA"));
	BikeDAO bikeDAO = new BikeDAO();
	CustomerDAO custDAO = new CustomerDAO();
	RentalDAO rentalDAO = new RentalDAO();
	SystemUserDAO sysUserDAO = new SystemUserDAO();
	LockDAO keyLockDAO = new LockDAO();
	BasketDAO basketDAO = new BasketDAO();
	
	public void generateRandomBikes(int numOfBikes) throws Exception {
		for(int i = 0; i < numOfBikes; i++) {
			String name = "B" + String.format("%03d", (i + 1));
			String imgPath = randomBetween(1, 2) + ".jpg";
			String manufacturer = "M" + randomBetween(1, 9);
			String prodCode = String.valueOf(i);
			String serial = "serial" + i;
			String model = "Model " + randomBetween(1, 3);
			
			Bike bike = new Bike(name, null, BikeState.AVAILABLE, imgPath, manufacturer, prodCode, serial, model);

			// at least half of the bikes will be AVAILABLE
			if (i <= numOfBikes/2) {
				bike.setState(BikeState.values()[randomBetween(0, BikeState.values().length - 1)]);
				bike.setNotes("This is a random note");
			}
			
			try {
				bikeDAO.addBike(bike);				
			} catch (Exception e) {
				throw e;
			}
		}
	}

	public void generateRandomSystemUsers() {
		SystemUser sysUser = new SystemUser("test@test.com", BCrypt.hashpw("password", BCrypt.gensalt()), "Admin");
		sysUserDAO.addSysUser(sysUser);
		SystemUser sysUser2 = new SystemUser("admin", BCrypt.hashpw("admin", BCrypt.gensalt()), "Admin");
		sysUserDAO.addSysUser(sysUser2);
	}

	public void generateRandomRentals() throws Exception {
		List<Customer> customers = custDAO.getAllCustomer();
		List<Bike> bikes = bikeDAO.getAllBikes();
		List<LockItem> lockItems = keyLockDAO.getAllLockItems();
		
		List<RentalComponent> rcListActive = new ArrayList<RentalComponent>();
		rcListActive.add(bikes.get(0));
		rcListActive.add(lockItems.get(0));
		
		List<RentalComponent> rcListLate = new ArrayList<RentalComponent>();
		rcListLate.add(bikes.get(1));
		rcListLate.add(lockItems.get(1));
		
		List<RentalComponent> rcListReturned = new ArrayList<RentalComponent>();
		rcListReturned.add(bikes.get(2));
		rcListReturned.add(lockItems.get(2));
		
		List<RentalComponent> rcListReturnedLate = new ArrayList<RentalComponent>();
		rcListReturnedLate.add(bikes.get(3));
		rcListReturnedLate.add(lockItems.get(3));
		
		Rental rentalActive = new Rental(setRandomSignedOutDate("active"), setRandomDueDate("active", null), null, customers.get(0), rcListActive, "");
		Rental rentalLate = new Rental(setRandomSignedOutDate("late"), setRandomDueDate("late", null), null, customers.get(1), rcListLate, "");
		
		LocalDate returnedSignOut = setRandomSignedOutDate("returned");
		LocalDate returnedDue = setRandomDueDate("returned", returnedSignOut);
		LocalDate returnedReturn = setRandomReturnedDate("returned", returnedDue);
		Rental rentalReturned = new Rental(returnedSignOut, returnedDue, returnedReturn, customers.get(2), rcListReturned, "");
		
		LocalDate returnedLateSignOut = setRandomSignedOutDate("returned_late");
		LocalDate returnedLateDue = setRandomDueDate("returned_late", returnedLateSignOut);
		LocalDate returnedLateReturn = setRandomReturnedDate("returned_late", returnedLateDue);
		Rental rentalReturnedLate = new Rental(returnedLateSignOut, returnedLateDue, returnedLateReturn, customers.get(3), rcListReturnedLate, "");
		
		try {
			rentalDAO.addRental(rentalActive);
			rentalDAO.addRental(rentalLate);
			rentalDAO.addRental(rentalReturned);
			rentalDAO.addRental(rentalReturnedLate);			
		} catch (Exception e) {
			throw e;
		}
	}

	public void generateRandomKeyLocks(int quantityOfLocks, int quantityOfKeysPerLock) throws Exception {
		for(int i = 1; i < quantityOfLocks + 1; i++) {
			String name = "L" + String.format("%03d", (i + 1));
			LockItem lockItem = new LockItem(name, null, quantityOfKeysPerLock, LockState.AVAILABLE);
			try {
				keyLockDAO.addLockItem(lockItem);				
			} catch (Exception e) {
				throw e;
			}
		}
	}
	
	public void generateRandomBaskets(int quantityOfLocks) throws Exception {
		for(int i = 1; i < quantityOfLocks + 1; i++) {
			String name = "BSK" + String.format("%03d", (i + 1));
			Basket basket = new Basket(name, null, BasketState.AVAILABLE);
			try {
				basketDAO.addBasket(basket);				
			} catch (Exception e) {
				throw e;
			}
		}
	}

	private LocalDate setRandomSignedOutDate(String status) {
		LocalDate signOut;
		if(status == "active") {
			signOut = LocalDate.now().plusDays(-randomBetween(0, 6));
		} else if(status == "late") {
			signOut = LocalDate.now().plusDays(-randomBetween(6, 10));
		} else {
			signOut = LocalDate.now().plusDays(-randomBetween(20, 30));
		}
		
		return signOut;
	}

	private LocalDate setRandomDueDate(String status, LocalDate signedOutDate) {
		LocalDate dueDate;

		if(status == "active") {
			dueDate = LocalDate.now().plusDays(randomBetween(1, 6));
		} else if(status == "late") {
			dueDate = LocalDate.now().plusDays(-randomBetween(1, 5));
		} else {
			
			dueDate = signedOutDate;
			dueDate = LocalDate.now().plusDays(randomBetween(4, 6));
		}
		
		return dueDate;
	}
	
	private LocalDate setRandomReturnedDate(String status, LocalDate dueDate) {
		LocalDate returnedDate;
		
		if(status == "returned") {
			returnedDate = dueDate;
			returnedDate = LocalDate.now().plusDays(-randomBetween(1, 2));
		} else  {
			returnedDate = dueDate;
			returnedDate = LocalDate.now().plusDays(randomBetween(1, 3));
		}
		
		return returnedDate;
	}

	public void generateRandomCustomer(int numOfCustomers) {
		for(int i = 0; i < numOfCustomers; i++) {
			String fName = faker.name().firstName();
			String lName = faker.name().lastName();
			String emContactFName = faker.name().firstName();
			String emContactLName = faker.name().lastName();
			
			LocalDate today = LocalDate.now();
			
			// user created today, with end of program in December 31 two years from now. Waiver dates are added in the addCustomer()
			Customer customer = new Customer(
					999999900 + i,
					fName,
					lName,
					faker.address().streetAddress(),
					fName + lName + "@sheridan.ca",
					fName + lName + "@gmail.com",
					Long.parseLong((faker.phoneNumber().cellPhone().replaceAll("[\\s\\-().]", ""))),
					CustomerType.values()[randomBetween(0, CustomerType.values().length - 1)], false, true, "", emContactFName, emContactLName,
					Long.parseLong((faker.phoneNumber().cellPhone().replaceAll("[\\s\\-\\.()]", ""))),
					null,
					LocalDate.of(today.getYear() + 2, 12, 31),
					null,
					null
					);
			custDAO.addCustomer(customer);
		}
	}
	
	/**
	 * returns a random number between and including a min and a max
	 * 
	 * @param min minimum number in range 
	 * @param max maximum number in range
	 * @return random integer number between range
	 */
	private int randomBetween(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}
}

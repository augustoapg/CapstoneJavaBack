package ca.sheridancollege.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.github.javafaker.Faker;

import ca.sheridancollege.beans.Bike;
import ca.sheridancollege.beans.Customer;
import ca.sheridancollege.beans.KeyItem;
import ca.sheridancollege.beans.LockItem;
import ca.sheridancollege.beans.Rental;
import ca.sheridancollege.beans.RentalComponent;
import ca.sheridancollege.beans.SystemUser;
import ca.sheridancollege.dao.BikeDAO;
import ca.sheridancollege.dao.CustomerDAO;
import ca.sheridancollege.dao.KeyLockDAO;
import ca.sheridancollege.dao.RentalDAO;
import ca.sheridancollege.dao.SystemUserDAO;
import ca.sheridancollege.enums.BikeState;
import ca.sheridancollege.enums.CustomerType;
import ca.sheridancollege.enums.KeyState;
import ca.sheridancollege.enums.LockState;

public class DummyDataGenerator {

	Faker faker = new Faker(new Locale("en-CA"));
	BikeDAO bikeDAO = new BikeDAO();
	CustomerDAO custDAO = new CustomerDAO();
	RentalDAO rentalDAO = new RentalDAO();
	SystemUserDAO sysUserDAO = new SystemUserDAO();
	KeyLockDAO keyLockDAO = new KeyLockDAO();
	
	public void generateRandomBikes(int numOfBikes) {
		for(int i = 0; i < numOfBikes; i++) {
			Bike bike = new Bike(null, BikeState.AVAILABLE, randomBetween(1, 2) + ".jpg", "M" + randomBetween(1, 9), i + "", i);

			// at least half of the bikes will be AVAILABLE
			if (i <= numOfBikes/2) {
				bike.setBikeState(BikeState.values()[randomBetween(0, BikeState.values().length - 1)]);
				bike.setNotes("This is a random note");
			}
			
			bikeDAO.addBike(bike);
		}
	}

	public void generateRandomSystemUsers() {
		SystemUser sysUser = new SystemUser("test@test.com", BCrypt.hashpw("password", BCrypt.gensalt()), "Admin");
		sysUserDAO.addSysUser(sysUser);
		SystemUser sysUser2 = new SystemUser("admin", BCrypt.hashpw("admin", BCrypt.gensalt()), "Admin");
		sysUserDAO.addSysUser(sysUser2);
	}

	public void generateRandomRentals() {
		List<Customer> customers = custDAO.getAllCustomer();
		List<Bike> bikes = bikeDAO.getAllBikes();
		List<LockItem> lockItems = keyLockDAO.getAllLockItems();
		List<KeyItem> keyItems = keyLockDAO.getAllKeyItems();
		
		System.out.println("about to create active list");
		
		List<RentalComponent> rcListActive = new ArrayList<RentalComponent>();
		rcListActive.add(bikes.get(0));
		rcListActive.add(lockItems.get(0));
		rcListActive.add(keyItems.get(0));
		
		System.out.println("about to create late list");
		
		List<RentalComponent> rcListLate = new ArrayList<RentalComponent>();
		rcListActive.add(bikes.get(1));
		rcListActive.add(lockItems.get(1));
		rcListActive.add(keyItems.get(1));
		
		System.out.println("about to create rtn list");
		
		List<RentalComponent> rcListReturned = new ArrayList<RentalComponent>();
		rcListActive.add(bikes.get(2));
		rcListActive.add(lockItems.get(2));
		rcListActive.add(keyItems.get(2));
		
		System.out.println("about to create rtn late list");
		
		List<RentalComponent> rcListReturnedLate = new ArrayList<RentalComponent>();
		rcListActive.add(bikes.get(3));
		rcListActive.add(lockItems.get(3));
		rcListActive.add(keyItems.get(3));
		
		System.out.println("about to create rental active");
		
		Rental rentalActive = new Rental(setRandomSignedOutDate("active"), setRandomDueDate("active", null), null, customers.get(0), rcListActive, "");
		
		System.out.println("about to create rental late");
		Rental rentalLate = new Rental(setRandomSignedOutDate("late"), setRandomDueDate("late", null), null, customers.get(1), rcListLate, "");
		
		LocalDate returnedSignOut = setRandomSignedOutDate("returned");
		LocalDate returnedDue = setRandomDueDate("returned", returnedSignOut);
		LocalDate returnedReturn = setRandomReturnedDate("returned", returnedDue);
		System.out.println("about to create rental returned");
		Rental rentalReturned = new Rental(returnedSignOut, returnedDue, returnedReturn, customers.get(2), rcListReturned, "");
		
		LocalDate returnedLateSignOut = setRandomSignedOutDate("returned_late");
		LocalDate returnedLateDue = setRandomDueDate("returned_late", returnedLateSignOut);
		LocalDate returnedLateReturn = setRandomReturnedDate("returned_late", returnedLateDue);
		System.out.println("about to create rental rtrned late");
		Rental rentalReturnedLate = new Rental(returnedLateSignOut, returnedLateDue, returnedLateReturn, customers.get(3), rcListReturnedLate, "");
		
		System.out.println("about to add active into dao");
		
		rentalDAO.addRental(rentalActive);
		
		System.out.println("about to add late into dao");
		
		rentalDAO.addRental(rentalLate);
		
		System.out.println("about to add returned into dao");
		
		rentalDAO.addRental(rentalReturned);
		
		System.out.println("about to add returned late into dao");
		
		rentalDAO.addRental(rentalReturnedLate);
	}

	public void generateRandomKeyLocks(int quantityOfKeyLocks) {
		for(int i = 1; i < quantityOfKeyLocks + 1; i++) {
			KeyItem keyItem = new KeyItem(KeyState.AVAILABLE);
			LockItem lockItem = new LockItem(keyItem, LockState.AVAILABLE);
			keyLockDAO.addKeyItem(keyItem);
			keyLockDAO.addLockItem(lockItem);
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
			
			Customer customer = new Customer(999999900 + i, fName, lName,
					faker.address().streetAddress(), fName + lName + "@sheridan.ca", fName + lName + "@gmail.com", faker.phoneNumber().cellPhone(), 
					CustomerType.values()[randomBetween(0, CustomerType.values().length - 1)], false, true, "", emContactFName, emContactLName, faker.phoneNumber().cellPhone());
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

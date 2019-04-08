package ca.sheridancollege.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.github.javafaker.Faker;

import ca.sheridancollege.beans.Bike;
import ca.sheridancollege.beans.Customer;
import ca.sheridancollege.beans.Rental;
import ca.sheridancollege.beans.SystemUser;
import ca.sheridancollege.dao.BikeDAO;
import ca.sheridancollege.dao.CustomerDAO;
import ca.sheridancollege.dao.RentalDAO;
import ca.sheridancollege.dao.SystemUserDAO;
import ca.sheridancollege.enums.CustomerType;

public class DummyDataGenerator {

	Faker faker = new Faker(new Locale("en-CA"));
	BikeDAO bikeDAO = new BikeDAO();
	CustomerDAO custDAO = new CustomerDAO();
	RentalDAO rentalDAO = new RentalDAO();
	SystemUserDAO sysUserDAO = new SystemUserDAO();
	
	public void generateRandomBikes(int numOfBikes) {
		for(int i = 0; i < numOfBikes; i++) {
			Bike bike = new Bike(null, false, true, randomBetween(1, 2) + ".jpg");

			if (Math.random() <= 0.5) {
				bike.setRepairNeeded(true);
				bike.setNotes("Scratched on body");
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
		
		Rental rentalActive = new Rental(setRandomSignedOutDate("active"), setRandomDueDate("active", null), null, customers.get(0), bikes.get(0), "");
		Rental rentalLate = new Rental(setRandomSignedOutDate("late"), setRandomDueDate("late", null), null, customers.get(1), bikes.get(1), "");
		
		Date returnedSignOut = setRandomSignedOutDate("returned");
		Date returnedDue = setRandomDueDate("returned", returnedSignOut);
		Date returnedReturn = setRandomReturnedDate("returned", returnedDue);
		Rental rentalReturned = new Rental(returnedSignOut, returnedDue, returnedReturn, customers.get(2), bikes.get(2), "");
		
		Date returnedLateSignOut = setRandomSignedOutDate("returned_late");
		Date returnedLateDue = setRandomDueDate("returned_late", returnedLateSignOut);
		Date returnedLateReturn = setRandomReturnedDate("returned_late", returnedLateDue);
		Rental rentalReturnedLate = new Rental(returnedLateSignOut, returnedLateDue, returnedLateReturn, customers.get(3), bikes.get(3), "");
		
		rentalDAO.addRental(rentalActive);
		rentalDAO.addRental(rentalLate);
		rentalDAO.addRental(rentalReturned);
		rentalDAO.addRental(rentalReturnedLate);
	}


	private Date setRandomSignedOutDate(String status) {
		Calendar signOut = Calendar.getInstance();
		if(status == "active") {
			signOut.roll(Calendar.DAY_OF_YEAR, -randomBetween(0, 6));
		} else if(status == "late") {
			signOut.roll(Calendar.DAY_OF_YEAR, -randomBetween(6, 10));
		} else {
			signOut.roll(Calendar.DAY_OF_YEAR, -randomBetween(20, 30));
		}
		
		return signOut.getTime();
	}

	private Date setRandomDueDate(String status, Date signedOutDate) {
		Calendar dueDate = Calendar.getInstance();

		if(status == "active") {
			dueDate.roll(Calendar.DAY_OF_YEAR, randomBetween(1, 6));
		} else if(status == "late") {
			dueDate.roll(Calendar.DAY_OF_YEAR, -randomBetween(1, 5));
		} else {
			dueDate.setTime(signedOutDate);
			dueDate.roll(Calendar.DAY_OF_YEAR, randomBetween(4, 6));
		}
		
		return dueDate.getTime();
	}
	
	private Date setRandomReturnedDate(String status, Date dueDate) {
		Calendar returnedDate = Calendar.getInstance();
		
		if(status == "returned") {
			returnedDate.setTime(dueDate);
			returnedDate.roll(Calendar.DAY_OF_YEAR, -randomBetween(1, 2));
		} else if(status == "returned_late") {
			returnedDate.setTime(dueDate);
			returnedDate.roll(Calendar.DAY_OF_YEAR, randomBetween(1, 3));
		}
		
		return returnedDate.getTime();
	}

	public void generateRandomCustomer(int numOfCustomers) {
		for(int i = 0; i < numOfCustomers; i++) {
			String fName = faker.name().firstName();
			String lName = faker.name().lastName();
			
			Customer customer = new Customer(999999900 + i, fName, lName,
					faker.address().streetAddress(), fName + lName + "@sheridan.ca", fName + lName + "@gmail.com", faker.phoneNumber().cellPhone(), 
					CustomerType.values()[randomBetween(0, CustomerType.values().length - 1)], false, true, "");
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

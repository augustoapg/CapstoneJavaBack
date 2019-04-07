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
			Bike bike = new Bike("Scratched on body", true, true, null);

			if (Math.random() <= 0.5) {
				bike.setImgPath("1.jpg");
				bike.setAvailable(true);
			} else {
				bike.setImgPath("2.jpg");
				bike.setAvailable(false);
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

	public void generateRandomRentals(int numOfRentals) {
		List<Customer> customers = custDAO.getAllCustomer();
		List<Bike> bikes = bikeDAO.getAllBikes();
		
		for(int i = 0; i < numOfRentals; i++) {
			Rental rentalActive = generateRandomActiveRental(customers.get(randomBetween(0, customers.size() - 1)));
			
			Calendar calActiveDue = Calendar.getInstance();
			Calendar calLateDue = Calendar.getInstance();
			Calendar calReturnedSignOut = Calendar.getInstance();
			Calendar calReturnedDue = Calendar.getInstance();
			Calendar calReturnedReturn = Calendar.getInstance();
			Calendar calReturnedLateSignOut = Calendar.getInstance();
			Calendar calReturnedLateDue = Calendar.getInstance();
			Calendar calReturnedLateReturn = Calendar.getInstance();
			
			calActiveDue.set(2019, 3, 16);
			calLateDue.set(2019, 3, 5);
			calReturnedSignOut.set(2019, 3, 1);
			calReturnedDue.set(2019, 3, 16);
			calReturnedReturn.set(2019, 3, 6);
			calReturnedLateSignOut.set(2019, 2, 25);
			calReturnedLateDue.set(2019, 3, 3);
			calReturnedLateReturn.set(2019, 3, 6);
			
			Rental rentalActive = new Rental(new Date(), calActiveDue.getTime(), null, cust, bikeDAO.getAllBikes().get(0), "");
			Rental rentalLate = new Rental(new Date(), calLateDue.getTime(), null, cust, bikeDAO.getAllBikes().get(0), "");
			Rental rentalReturned = new Rental(calReturnedSignOut.getTime(), calReturnedDue.getTime(), calReturnedReturn.getTime(), cust, bikeDAO.getAllBikes().get(0), "");
			Rental rentalReturnedLate = new Rental(calReturnedLateSignOut.getTime(), calReturnedLateDue.getTime(), calReturnedLateReturn.getTime(), cust, bikeDAO.getAllBikes().get(0), "");
			
			rentalDAO.addRental(rentalActive);
			rentalDAO.addRental(rentalLate);
			rentalDAO.addRental(rentalReturned);
			rentalDAO.addRental(rentalReturnedLate);
		}
	}
	
	private Rental generateRandomActiveRental(Customer cust, Bike bike) {
		Date signedOutDate = setRandomSignedOutDate("active");
		Date dueDate = setRandomDueDate("active", signedOutDate);
		Date returnedDate = null;
		
		Rental rentalActive = new Rental(signedOutDate, dueDate, null, cust, bike, "");
		
		return null;
	}

	private Date setRandomReturnedDate(String status) {
		// TODO Auto-generated method stub
		return null;
	}

	private Date setRandomSignedOutDate(String status) {
		Date dueDate = new Date();
		if(status == "active") {
			Calendar cal = Calendar.getInstance();
			cal.roll(Calendar.DAY_OF_YEAR, -randomBetween(0, 6));
			dueDate = faker.date().between(cal.getTime(), new Date());
		}
		
		return dueDate;
	}

	private Date setRandomDueDate(String status, Date signedOutDate) {
		Date dueDate = new Date();
		// returns any date from today until 6 days from now
		if(status == "active") {
			Calendar cal = Calendar.getInstance();
			cal.roll(Calendar.DAY_OF_YEAR, randomBetween(1, 6));
			dueDate = faker.date().between(new Date(), cal.getTime());
		}
		
		return dueDate;
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

package ca.sheridancollege.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.github.javafaker.Faker;

import ca.sheridancollege.beans.Basket;
import ca.sheridancollege.beans.Bike;
import ca.sheridancollege.beans.Customer;
import ca.sheridancollege.beans.LockItem;
import ca.sheridancollege.beans.PreDefinedPayable;
import ca.sheridancollege.beans.Rental;
import ca.sheridancollege.beans.RentalComponent;
import ca.sheridancollege.beans.SystemUser;
import ca.sheridancollege.beans.Waiver;
import ca.sheridancollege.dao.BasketDAO;
import ca.sheridancollege.dao.BikeDAO;
import ca.sheridancollege.dao.CustomerDAO;
import ca.sheridancollege.dao.LockDAO;
import ca.sheridancollege.dao.PreDefinedPayableDAO;
import ca.sheridancollege.dao.RentalDAO;
import ca.sheridancollege.dao.SystemUserDAO;
import ca.sheridancollege.dao.WaiverDAO;
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
	PreDefinedPayableDAO preDefPayableDAO = new PreDefinedPayableDAO();
	WaiverDAO waiverDAO = new WaiverDAO();
	
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
		ZonedDateTime today = ZonedDateTime.now(ZoneId.of("America/Toronto"));
		if(status == "active") {
			signOut = today.toLocalDate().plusDays(-randomBetween(0, 6));
		} else if(status == "late") {
			signOut = today.toLocalDate().plusDays(-randomBetween(6, 10));
		} else {
			signOut = today.toLocalDate().plusDays(-randomBetween(20, 30));
		}
		
		return signOut;
	}

	private LocalDate setRandomDueDate(String status, LocalDate signedOutDate) {
		LocalDate dueDate;
		ZonedDateTime today = ZonedDateTime.now(ZoneId.of("America/Toronto"));

		if(status == "active") {
			dueDate = today.toLocalDate().plusDays(randomBetween(1, 6));
		} else if(status == "late") {
			dueDate = today.toLocalDate().plusDays(-randomBetween(1, 5));
		} else {
			
			dueDate = signedOutDate;
			dueDate = today.toLocalDate().plusDays(randomBetween(4, 6));
		}
		
		return dueDate;
	}
	
	private LocalDate setRandomReturnedDate(String status, LocalDate dueDate) {
		LocalDate returnedDate;
		ZonedDateTime today = ZonedDateTime.now(ZoneId.of("America/Toronto"));
		
		if(status == "returned") {
			returnedDate = dueDate;
			returnedDate = today.toLocalDate().plusDays(-randomBetween(1, 2));
		} else  {
			returnedDate = dueDate;
			returnedDate = today.toLocalDate().plusDays(randomBetween(1, 3));
		}
		
		return returnedDate;
	}

	public void generateRandomCustomer(int numOfCustomers) {
		ZonedDateTime today = ZonedDateTime.now(ZoneId.of("America/Toronto"));
		
		for(int i = 0; i < numOfCustomers; i++) {
			String fName = faker.name().firstName();
			String lName = faker.name().lastName();
			String emContactFName = faker.name().firstName();
			String emContactLName = faker.name().lastName();
			
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
		
		// one Customer created to be someone with an expired waiver
		// lastSigned was Jan 1st of last year
		// expiration date was last Aug 31st last year
		Customer waiverExpiredCustomer = new Customer(
				111111110,
				faker.name().firstName(),
				faker.name().lastName(),
				faker.address().streetAddress(),
				faker.name().lastName() + "@sheridan.ca",
				faker.name().lastName() + "@gmail.com",
				Long.parseLong((faker.phoneNumber().cellPhone().replaceAll("[\\s\\-().]", ""))),
				CustomerType.values()[randomBetween(0, CustomerType.values().length - 1)], false, true, "", faker.name().firstName(), faker.name().lastName(),
				Long.parseLong((faker.phoneNumber().cellPhone().replaceAll("[\\s\\-\\.()]", ""))),
				LocalDate.of(today.getYear() - 1, 1, 1),
				LocalDate.of(today.getYear() + 2, 12, 31),
				LocalDate.of(today.getYear() - 1, 1, 1),
				LocalDate.of(today.getYear() - 1, 8, 31)
				);
		custDAO.addCustomer(waiverExpiredCustomer);
	}
	
	public void generatePreDefinedPayables() {
		try {
			preDefPayableDAO.addPreDefinedPayable(new PreDefinedPayable("Bike Lost", 300));
			preDefPayableDAO.addPreDefinedPayable(new PreDefinedPayable("Lock Lost", 60));
			preDefPayableDAO.addPreDefinedPayable(new PreDefinedPayable("Key Lost", 30));
			preDefPayableDAO.addPreDefinedPayable(new PreDefinedPayable("Basket Lost", 50));
			preDefPayableDAO.addPreDefinedPayable(new PreDefinedPayable("Light Lost", 5));
			preDefPayableDAO.addPreDefinedPayable(new PreDefinedPayable("Bike Damage", 5));
			preDefPayableDAO.addPreDefinedPayable(new PreDefinedPayable("Key Damage", 5));			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void generateWaiver() {
		String waiverInfo = "SHERIDAN BIKE HUB RENTAL PROGRAM: RELEASE OF LIABILITY, WAIVER OF CLAIMS, ASSUMPTION OF RISKS AND INDEMNITY AGREEMENT \r\n" + 
				"\r\n" + 
				"WARNING: BY SIGNING THIS DOCUMENT YOU WILL WAIVE CERTAIN LEGAL RIGHTS, INCLUDING THE RIGHT TO SUE.  	\r\n" + 
				"I, the undersigned, do hereby acknowledge the following:	\r\n" + 
				"\r\n" + 
				"1) I am at least 18 years of age;	\r\n" + 
				"\r\n" + 
				"2) I am legally competent to sign this Release of Liability, Waiver of Claims, Assumption of Risks and Indemnity Agreement for participation in The Sheridan College Institute of Technology and Advanced Learning (“Sheridan”) Bike Hub Rental Program;\r\n" + 
				"\r\n" + 
				"3) I have the requisite cycling experience to operate the rental bike and I will not misuse or unduly abuse the rental bike;\r\n" + 
				"\r\n" + 
				"4) My use and/or participation in the Sheridan Bike Hub Rental Program is entirely voluntary;\r\n" + 
				"\r\n" + 
				"5) I have a personal duty and responsibility to learn and to follow road safety standards, guidelines, and procedures, and to operate the rental bike in a manner appropriate to any and all prevailing road and weather conditions. I understand that I need to wear a helmet at all times while operating the rental bike and practice safe cycling while the bike is in my possession. I also acknowledge that it is my responsibility to ensure that the rental bike is in good working condition at all times;\r\n" + 
				"\r\n" + 
				"6) I understand that I am responsible for the well being of the bike while it is under my possession, including theft and any physical damage to the bike, and I hereby am responsible for paying the costs of replacing or repairing the bike, and any additional costs associated with the loss of the bike, if such theft or damage occurs. I also acknowledge that I am responsible for maintaining function and ensuring replacement of the rental bike’s accessories including but not limited to lights, bells and baskets;\r\n" + 
				"\r\n" + 
				"7) In the event that the bike is not returned as and when due and I cannot be reached for two weeks after the return date, Sheridan Bike Hub will report the bike stolen to the Region of Peel Police; and\r\n" + 
				"\r\n" + 
				"8) I accept full responsibility for the rental bike for the duration of the rental and it is my responsibility to check for flaws, defects, and damage prior to accepting the rental bike. I understand that all charges for the rental are applied at the time that the bike is in my possession, whether it is in use or not.\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"ASSUMPTION OF RISK: \r\n" + 
				"\r\n" + 
				"I understand and accept that renting this bike and participating in bicycling involves risks not limited to: loss or damage to personal property, all manner of personal injury, injury resulting from falling on uneven or slippery terrain including man made features, impacting against the floor, ground, trees, rocks, cement, course structures or other obstructions; impacting with other users of the road; potential for bone and muscular skeletal injury such as sprains and strains; any illness or injury resulting from exposure to cold, wet or windy weather, or the effects of heat and strong sunlight, or any insect bites/stings; any manner of injury or illness resulting from bicycling; inability to access medical help in an emergency; cuts and abrasions from ground contact and/ or the bike; negligence of other road users; and other unforeseen dangers and risks. I have no medical conditions, nor have I been diagnosed with a condition that prevents me from bicycling. \r\n" + 
				" \r\n" + 
				"I freely accept and fully assume all such risks, dangers and hazards and the possibility of personal injury, death, property damage or loss, resulting therefrom. 	\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"RELEASE OF LIABILITY, WAIVER OF CLAIMS AND INDEMNITY AGREEMENT \r\n" + 
				"\r\n" + 
				"In consideration of The Sheridan College Institute of Technology and Advanced Learning (“Sheridan”) Bike Hub Rental Program renting me this bike, I agree as follows:\r\n" + 
				"1.	TO WAIVE ANY AND ALL CLAIMS that I have or may have in the future against Sheridan, the Board of Governors of Sheridan and its members, officers, employees, students, agents, volunteers and independent contractors (all of whom are hereinafter collectively referred to as “Releasees”);\r\n" + 
				"\r\n" + 
				"2.	TO RELEASE THE RELEASEES from any and all liability for any loss, damage, injury or expense that I may suffer, or that my next of kin may suffer as a result of my participation in the Sheridan Bike Rental Program due to any cause whatsoever INCLUDING NEGLIGENCE, BREACH OF CONTRACT, OR BREACH OF ANY STATUTORY OR OTHER DUTY OF CARE; 	\r\n" + 
				" \r\n" + 
				"3.	TO HOLD HARMLESS AND INDEMNIFY THE RELEASEES from any and all liability for any damage to the property of, or personal injury to any third party, resulting from my participation in the Sheridan Bike Rental Program; and \r\n" + 
				"\r\n" + 
				"4.	This agreement shall be effective and binding upon my heirs, next of kin, executors, administrators, assigns and representatives in the event of death or incapacity. \r\n" + 
				"In entering into this Agreement, I am not relying upon any oral or written representations of statements made by the Releasees other than what is set forth in this Agreement. \r\n" + 
				"I HAVE READ AND UNDERSTOOD THIS AGREEMENT AND I AM AWARE THAT BY SIGNING THIS AGREEMENT I AM WAIVING CERTAIN LEGAL RIGHTS WHICH I OR MY HEIRS, NEXT OF KIN, EXECUTORS, ADMINISTERS AND ASSIGNS MAY HAVE AGAINST THE RELEASEES.  I DECLARE THAT I HAVE SIGNED IT FREELY AND WITHOUT ANY INDUCEMENT OR ASSURANCES OF ANY KIND WHATSOEVER. I AGREE THAT IF ANY PORTION OF THIS AGREEMENT IS HELD TO BE INVALID, THE REMAINDER SHALL CONTINUE IN FULL FORCE AND EFFECT.\r\n" + 
				"This Agreement must be completed in full, signed, dated, and witnessed prior to participation in the Sheridan Bike Rental Program.";
		
		ZonedDateTime today = ZonedDateTime.now(ZoneId.of("America/Toronto"));
		Waiver waiver = new Waiver(today.toLocalDate(), waiverInfo);
		try {
			waiverDAO.addWaiver(waiver);
		} catch (Exception e) {
			System.out.println("Error adding dummy Waiver");
			System.out.println(e.getMessage());
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

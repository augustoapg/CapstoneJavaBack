/**
 * Project: HMC Bike Rental
 * Group Name: Team Cube
 * Team members:
 * 		- Augusto A P Goncalez
 * 		- Jianlin Luo
 * 		- Julia Sakamoto
 * 		- Vikki Wai-Kei Wong
 */

package ca.sheridancollege;

import java.io.ByteArrayInputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.ChronoUnit;

import ca.sheridancollege.utils.ExcelGenerator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ca.sheridancollege.dao.*;
import ca.sheridancollege.enums.BasketState;
import ca.sheridancollege.enums.BikeState;
import ca.sheridancollege.enums.CustomerType;
import ca.sheridancollege.enums.LockState;
import ca.sheridancollege.utils.DummyDataGenerator;
import ca.sheridancollege.beans.*;

import static ca.sheridancollege.utils.ExcelGenerator.dateFormatter;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class HomeController {

	BikeDAO bikeDAO = new BikeDAO();
	CustomerDAO custDAO = new CustomerDAO();
	RentalDAO rentalDAO = new RentalDAO();
	PayableDAO payableDAO = new PayableDAO();
	SystemUserDAO sysUserDAO = new SystemUserDAO();
	LockDAO lockDAO = new LockDAO();
	BasketDAO basketDAO = new BasketDAO();
	PreDefinedPayableDAO preDefPayableDAO = new PreDefinedPayableDAO();
	WaiverDAO waiverDAO = new WaiverDAO();
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value = "/addDummyData", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> addDummyData(Model model) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();

		DummyDataGenerator dummyData = new DummyDataGenerator();
		
		try {
			dummyData.generateRandomBikes(10);
			dummyData.generateRandomCustomer(30);
			dummyData.generateRandomKeyLocks(20, 4);
			dummyData.generateRandomBaskets(10);
			dummyData.generateRandomRentals();
			dummyData.generateRandomSystemUsers();
			dummyData.generatePreDefinedPayables();
			dummyData.generateWaiver();
		} catch (Exception e) {
			log.info("/addDummyData - " + e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
		
		objNode.put("message", "Dummy data added");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}

	@RequestMapping(value = "/getBikes", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getBikes() {
		List<Bike> bikes = bikeDAO.getAllBikes();
		log.info("/getBikes - Getting All Bikes - " + bikes.size() + " retrieved");
		return new ResponseEntity<Object>(bikes, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getLocks", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getLocks() {
		List<LockItem> lockItems = lockDAO.getAllLockItems();
		log.info("/getLocks - Getting All Locks - " + lockItems.size() + " retrieved");
		return new ResponseEntity<Object>(lockItems, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getBaskets", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getBaskets() {
		List<Basket> baskets = basketDAO.getAllBaskets();
		log.info("/getBaskets - Getting All Baskets - " + baskets.size() + " retrieved");
		return new ResponseEntity<Object>(baskets, HttpStatus.OK);
	}

	@RequestMapping(value = "/getRentals", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getRentals() {
		List<Rental> rentals = rentalDAO.getAllRentals();
		log.info("/getRentals - Getting All Rentals- " + rentals.size() + " retrieved");
		return new ResponseEntity<Object>(rentals, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getPayables", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getPayables() {
		List<Payable> payables = payableDAO.getAllPayables();
		if (payables != null) {
			log.info("/getPayables - Getting all Payables - " + payables.size() + " retrieved");
		} else {
			log.info("/getPayables - No Payables were found in DB");
		}
		
		return new ResponseEntity<Object>(payables, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/getCustomers", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getCustomers() {
		List<Customer> customers = custDAO.getAllCustomer();
		log.info("/getCustomers - Getting all Customers - " + customers.size() + " retrieved");
		return new ResponseEntity<Object>(customers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getLockByID/{id}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getLockByID(@PathVariable int id) {
		LockItem lockItem = lockDAO.getLockItemById(id);
		log.info("/getLockByID - Getting Lock by ID " +id+ " ");
		return new ResponseEntity<Object>(lockItem, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getBasketByID/{id}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getBasketByID(@PathVariable int id) {
		Basket basket = basketDAO.getBasketById(id);
		log.info("/getBasketByID - Getting Basket by ID " + id + " ");
		return new ResponseEntity<Object>(basket, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getPayableByID/{id}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getPayableByID(@PathVariable int id) {
		Payable payable = payableDAO.getPayableById(id);
		log.info("/getPayableByID - Getting Payable by ID "+ id );
		return new ResponseEntity<Object>(payable, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getPayablesByCustID/{id}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getPayablesByCustID(@PathVariable int id) {
		List<Payable> payables = payableDAO.getPayablesByCustId(id);
		if (payables != null) {
			log.info("/getPayablesByCustID/{id} - Getting Payables by sheridanID" + id + " - " + payables.size() + " retrieved");
		} else {
			log.info("/getPayablesByCustID/{id} - No Payables were found with Customer ID " + id);
		}
		return new ResponseEntity<Object>(payables, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getPayablesByRentalId/{id}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getPayablesByRentalId(@PathVariable int id) {
		List<Payable> payables = payableDAO.getPayablesByRentalId(id);
		if (payables != null) {
			log.info("/getPayablesByRentalId/{id} - Getting Payables by Rental ID: " + id + " - " + payables.size() + " retrieved");
		} else {
			log.info("/getPayablesByRentalId/{id} - No Payables were found with Rental ID " + id);
		}
		return new ResponseEntity<Object>(payables, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getCustomer/{sheridanIdInput}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getCustomerByID(@PathVariable String sheridanIdInput) {
		int sheridanId = 0;
		try {
			sheridanId = Integer.parseInt(sheridanIdInput);
			Customer customer = custDAO.getCustomer(sheridanId);
			
			// if customer not found
			if (customer == null) {
				log.info("/getCustomer/{sheridanIdInput} - Cutomer not found with ID: " + sheridanIdInput);
				return new ResponseEntity<Object>(customer, HttpStatus.NO_CONTENT);
			}

			log.info("/getCustomer/{sheridanIdInput} - Getting Customer with ID: " + sheridanIdInput);
			return new ResponseEntity<Object>(customer, HttpStatus.OK);
		} catch (NumberFormatException e) {
			// if input is invalid (cannot convert string to int)
			log.error("/getCustomer/{sheridanIdInput} - Error. Customer ID:" + sheridanIdInput, e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Sheridan ID");
		}
	}
	
	@RequestMapping(value = "/searchCustomers/{keyword}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> searchCustomers(@PathVariable String keyword) {
		try {
			List<Customer> customers = custDAO.searchCustomers(keyword);

			if (customers == null) {
				log.info("/searchCustomers/{keyword} - Customer not found with keyword: " + keyword);
				return new ResponseEntity<Object>(customers, HttpStatus.NO_CONTENT);
			}
			
			log.info("/searchCustomers/{keyword} - Getting Customers with keyword \""+keyword+"\" - " + customers.size() + " retrieved");
			return new ResponseEntity<Object>(customers, HttpStatus.OK);
		} catch (NumberFormatException e) {
			// if input is invalid (cannot convert string to int)
			log.error("/searchCustomers/{keyword} - Error.", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input");
		}
	}


	@RequestMapping(value = "/getActiveRentalByDate/{type}/{stDate}/{enDate}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getActiveRentalByDate(@PathVariable String type, @PathVariable String stDate, @PathVariable String enDate) {
		List<Rental> rentals = new ArrayList<Rental>();
		try {
			if (type.equalsIgnoreCase("due")) {
				rentals = rentalDAO.getRentalsByDate("activeduedate", stDate, enDate);
			} 
			else if (type.equalsIgnoreCase("signout")) {
				rentals = rentalDAO.getRentalsByDate("activesignout", stDate, enDate);
			} 
			else {
				// type not recognized
				log.error("/getActiveRentalByDate/{type}/{stDate}/{enDate} - " +
						"Type \""+type+"\" not recognized");
			}
			
		} catch (Exception e) {
			log.error("/getActiveRentalByDate/{type}/{stDate}/{enDate} - Error.", e);
		}
		
		if (rentals != null) {
			log.info("/getActiveRentalByDate/{type}/{stDate}/{enDate} - Getting Rentals by date - " + rentals.size() + " retrieved");
		} else {
			log.info("/getActiveRentalByDate/{type}/{stDate}/{enDate} - No rentals found with date");
		}
		
		return new ResponseEntity<Object>(rentals, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getArchivedRentalByDate/{type}/{stDate}/{enDate}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getArchivedRentalByDate(@PathVariable String type, @PathVariable String stDate, @PathVariable String enDate) {
		List<Rental> rentals = new ArrayList<Rental>();
		try {
			if (type.equalsIgnoreCase("due")) {
				rentals = rentalDAO.getRentalsByDate("archiveduedate", stDate, enDate);
			} 
			else if (type.equalsIgnoreCase("signout")) {
				rentals = rentalDAO.getRentalsByDate("archivesignout", stDate, enDate);
			} 
			else if (type.equalsIgnoreCase("return")) {
				rentals = rentalDAO.getRentalsByDate("archivereturn", stDate, enDate);
			} 
			else {
				// type not recognized. 
				log.error("/getArchivedRentalByDate/{type}/{stDate}/{enDate} - Type \""+type+"\" not recognized");
			}
			
		} catch (Exception e) {
			log.error("/getArchivedRentalByDate/{type}/{stDate}/{enDate} - Error.", e);
		}
		
		if (rentals != null) {
			log.info("/getArchivedRentalByDate/{type}/{stDate}/{enDate} - Getting Rentals by date - " + rentals.size() + " retrieved");
		} else {
			log.info("/getArchivedRentalByDate/{type}/{stDate}/{enDate} - No rentals found with date");
		}
		
		return new ResponseEntity<Object>(rentals, HttpStatus.OK);
	}

	@RequestMapping(value = "/getRental/{id}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getRentalById(@PathVariable int id) {
		Rental rental = rentalDAO.getRental(id);
		
		if (rental == null) {
			log.info("/getRental - Rental not found with ID: " + id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Rental not found: " + id);
		}

		log.info("/getRental - Getting Rental with ID: " + id);
		return new ResponseEntity<Object>(rental, HttpStatus.OK);
	}

	@RequestMapping(value = "/getActiveRentals", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getActiveRentals() {
		List<Rental> rentals = rentalDAO.getActiveRentals();

		if (rentals.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No active rentals were found");
		}
		log.info("/getActiveRentals - Getting All Active Rentals - " + rentals.size() + " retrieved");
		return new ResponseEntity<Object>(rentals, HttpStatus.OK);
	}

	@RequestMapping(value = "/getActiveRental/{id}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getActiveRental(@PathVariable int id) {
		Rental rental = rentalDAO.getActiveRental(id);

		if(rental == null) {
			log.info("/getActiveRental/{id} - Active Rental not found with ID: " + id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Active rental not found: " + id);
		}
		
		log.info("/getActiveRental/{id} - Getting Active Rental with ID: " + id);
		return new ResponseEntity<Object>(rental, HttpStatus.OK);
	}

	@RequestMapping(value = "/getActiveRentalByCustID/{sheridanId}", method =
			RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getActiveRentalByCustID(@PathVariable int sheridanId) {
		Rental rental = rentalDAO.getActiveRentalByCustID(sheridanId);

		if(rental == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Active " +
					"rental not found: " + sheridanId);
		}
		return new ResponseEntity<Object>(rental, HttpStatus.OK);
	}

	@RequestMapping(value = "/getArchivedRentals", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getArchivedRentals() {
		List<Rental> rentals = rentalDAO.getArchivedRentals();
		log.info("/getArchivedRentals - Getting All Archived Rentals- " + rentals.size() + " retrieved");
		
		if (rentals.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No archived rentals were found");
		}
		return new ResponseEntity<Object>(rentals, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getArchivedRental/{id}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getArchivedRental(@PathVariable int id) {
		Rental rental = rentalDAO.getArchivedRental(id);

		if(rental == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Archive rental not found: " + id);
		}
		return new ResponseEntity<Object>(rental, HttpStatus.OK);
	}

	@RequestMapping(value = "/getArchivedRentalsByCustID/{sheridanId}", method =
			RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getArchivedRentalsByCustID(@PathVariable int sheridanId) {
		List<Rental> rentals = rentalDAO.getArchivedRentalsByCustID(sheridanId);

		if(rentals == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Archive rental not found: " + sheridanId);
		}
		return new ResponseEntity<Object>(rentals, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getReportGeneralData/from={strFromDate}&to={strToDate}", method =
			RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<?> getReportGeneralData(@PathVariable String strFromDate, @PathVariable String strToDate) {			
		int numTotalRentals = 0;
		int numLateRentals = 0;
		int numNewCustomers = 0;
		double avgRentDays = 0.0;
		
		// getting information on Customers created by date range
		try {
			LocalDate fromDate = LocalDate.parse(strFromDate);
			LocalDate toDate = LocalDate.parse(strToDate);			
			List<Customer> newCustomers = custDAO.getCustomerByCreatedDate(fromDate, toDate);
			numNewCustomers = newCustomers.size();
		} catch (DateTimeException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Invalid date.");
		}
		
		// getting num of rentals and average rental days for date range
		List<Rental> rentals = null;
		
		try {
			rentals = rentalDAO.getRentalsByDate("allsignout", strFromDate, strToDate);
			if (rentals != null) {
				numTotalRentals = rentals.size();
				avgRentDays = getAverageRentDays(rentals);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
		
		// getting late rentals
		List<Rental> lateRentals = rentalDAO.getLateRentals();
		numLateRentals = lateRentals.size();
		
		// creating JSON
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		objNode.put("totalRentals", numTotalRentals);
		objNode.put("numOfLateRentals", numLateRentals);
		objNode.put("numOfNewCustomers", numNewCustomers);
		objNode.put("avgRentDays", avgRentDays);
		
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getReportHistoricalData", method =
			RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<?> getReportHistoricalData() {
		long numOfStudents = 0;
		long numOfStaff = 0;
		double avgHistoricalRentDays = 0.0;
		
		// getting historical values 
		numOfStudents = custDAO.getNumberOfCustomersByType(CustomerType.STUDENT);
		numOfStaff = custDAO.getNumberOfCustomersByType(CustomerType.STAFF);
		List<Rental> allRentals = rentalDAO.getAllRentals();
		avgHistoricalRentDays = getAverageRentDays(allRentals);
		
		// creating JSON
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		objNode.put("numberOfStudents", numOfStudents);
		objNode.put("numberOfStaff", numOfStaff);
		objNode.put("historicalRentDays", avgHistoricalRentDays);
		
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	private double getAverageRentDays(List<Rental> rentals) {
		long[] rentalDays = new long[rentals.size()];
		double avg = 0.0;
		
		for (int i = 0; i < rentals.size(); i++) {
			Rental rental = rentals.get(i);
			if (rental.getReturnedDate() != null) {
				rentalDays[i] = ChronoUnit.DAYS.between(rental.getSignOutDate(), rental.getReturnedDate());
			} else {
				rentalDays[i] = ChronoUnit.DAYS.between(rental.getSignOutDate(), ZonedDateTime.now(ZoneId.of("America/Toronto")));
			}
		}
		
		long sum = 0;
		
		for (int i = 0; i < rentalDays.length; i++) {
			sum += rentalDays[i];
		}
		
		avg = (double)sum/(double)rentalDays.length;
		
		return avg;
	}
	
	@RequestMapping(value = "/getAllPreDefinedPayables", method = RequestMethod.GET, produces = {"application/json"})
	public ResponseEntity<Object> getAllPreDefinedPayables() {
		List<PreDefinedPayable> preDefPayables = preDefPayableDAO.getAllPreDefinedPayables();

		if (preDefPayables.isEmpty()) {
			log.info("/getAllPreDefinedPayables - No Pre Defined Payables were found");
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No pre defined payables were found");
		}
		log.info("/getAllPreDefinedPayables - Getting All Pre Defined Payables - " + preDefPayables.size() + " retrieved");
		return new ResponseEntity<Object>(preDefPayables, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getPreDefinedPayableByID/{id}", method = RequestMethod.GET, produces = {"application/json"})
	public ResponseEntity<Object> getPreDefinedPayableByID(@PathVariable int id) {
		PreDefinedPayable preDefPayable = preDefPayableDAO.getPredefinedPayableByID(id);

		if (preDefPayable == null) {
			log.info("/getPreDefinedPayableByID - No Pre Defined Payable found with ID " + id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No pre defined payable found with id " + id);
		}
		log.info("/getPreDefinedPayableByID - Getting Pre Defined Payable with ID - " + id);
		return new ResponseEntity<Object>(preDefPayable, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getPreDefinedPayableByCategory/{category}", method = RequestMethod.GET, produces = {"application/json"})
	public ResponseEntity<Object> getPreDefinedPayableByCategory(@PathVariable String category) {
		PreDefinedPayable preDefPayable = preDefPayableDAO.getPredefinedPayableByCategory(category);

		if (preDefPayable == null) {
			log.info("/getPreDefinedPayableByCategory - No Pre Defined Payable found with category " + category);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No pre defined payable found with category " + category);
		}
		log.info("/getPreDefinedPayableByCategory - Getting Pre Defined Payable with category - " + category);
		return new ResponseEntity<Object>(preDefPayable, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getWaiver", method = RequestMethod.GET, produces = {"application/json"})
	public ResponseEntity<Object> getWaiver() {
		Waiver waiver = waiverDAO.getWaiver();
		return new ResponseEntity<Object>(waiver, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/updateWaiver", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<Object> updateWaiver(@RequestBody Waiver waiver) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		try {
			waiverDAO.addWaiver(waiver);			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
		
		objNode.put("message", "Waiver updated successfully");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/editBike", method = RequestMethod.PATCH, produces = {"application/json"})
	public ResponseEntity<?> editBike(@RequestBody Bike newBike) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		if (newBike.getName() == null || newBike.getName().trim().isEmpty()) {
			log.info("/editBike/{id} - Bike sent without name: " + newBike.getId());
	    	return ResponseEntity.status(HttpStatus.CONFLICT).body("Bike requires a name");
		}
	
		Bike bike = bikeDAO.getBikeById(newBike.getId());
	    if (bike == null) {
	    	log.info("/editBike - Bike not found with ID: " + newBike.getId());
	    	return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Bike not found: " + newBike.getId());
	    }
	    
	    try {
	    	bikeDAO.editBike(newBike);			
		} catch (Exception e) {
			log.info("/editBike - " + e.getMessage());
	    	return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
		}
	    
	    log.info("/editBike - Edited Bike with ID: " + newBike.getId());
		objNode.put("message", "Bike has been updated");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/editPreDefinedPayable", method = RequestMethod.PATCH, produces = {"application/json"})
	public ResponseEntity<?> editPreDefinedPayable(@RequestBody PreDefinedPayable newPreDefinedPayable) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		if (newPreDefinedPayable.getCategory() == null || newPreDefinedPayable.getCategory().trim().isEmpty()) {
			log.info("/editPreDefinedPayable - PreDefinedPayable sent without category: " + newPreDefinedPayable.getId());
	    	return ResponseEntity.status(HttpStatus.CONFLICT).body("PreDefinedPayable requires a category");
		}
	
		PreDefinedPayable preDefinedPayable = preDefPayableDAO.getPredefinedPayableByID(newPreDefinedPayable.getId());
	    if (preDefinedPayable == null) {
	    	log.info("/editPreDefinedPayable/{id} - PreDefinedPayable not found with ID: " + newPreDefinedPayable.getId());
	    	return ResponseEntity.status(HttpStatus.NO_CONTENT).body("PreDefinedPayable not found: " + newPreDefinedPayable.getId());
	    }
	    
	    try {
	    	preDefPayableDAO.editPreDefinedPayable(newPreDefinedPayable);			
		} catch (Exception e) {
			log.info("/editPreDefinedPayable - " + e.getMessage());
	    	return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
		}
	    
	    log.info("/editPreDefinedPayable - Edited PreDefinedPayable with ID: " + newPreDefinedPayable.getId());
		objNode.put("message", "PreDefinedPayable has been updated");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/deletePreDefinedPayable", method = RequestMethod.DELETE, produces = {"application/json"})
	public ResponseEntity<?> deletePreDefinedPayable(@RequestBody PreDefinedPayable preDefinedPayable) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
	    
	    try {
	    	preDefPayableDAO.deletePreDefinedPayable(preDefinedPayable);			
		} catch (Exception e) {
			log.info("/deletePreDefinedPayable - " + e.getMessage());
	    	return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
		}
	    
	    log.info("/deletePreDefinedPayable - Deleted PreDefinedPayable with ID: " + preDefinedPayable.getId());
		objNode.put("message", "PreDefinedPayable has been deleted");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/returnRental", method = RequestMethod.PATCH, produces = {"application/json"})
	public ResponseEntity<?> returnRental(@RequestBody Rental newRental) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		Rental rentalToReturn = rentalDAO.getRental(newRental.getId());
	    if (rentalToReturn == null) {
	    	return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Rental was not found: " + newRental.getId());
	    }
	    
	    // If bike was returned before
	    else if ("Returned".equals(rentalToReturn.getRentalState().toString()) || 
	    		"Returned Late".equals(rentalToReturn.getRentalState().toString())) {
	    	log.info("/returnRental - Error. Bike has been returned. Rental ID: " + newRental.getId());
	    	return ResponseEntity.status(HttpStatus.NO_CONTENT).body("This rental has already ended: " + newRental.getId());
	    }
	    
	    try {
	    	rentalDAO.returnRental(rentalToReturn, newRental);	    	
	    } catch (Exception e) {
	    	log.info("/returnRental - " + e.getMessage());
	    	return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	    }
	    
	    log.info("/returnRental - Returned Bike with rental ID: " + newRental.getId());
		objNode.put("message", "Rental has been returned");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/newCustomer", method = RequestMethod.POST, 
			produces = {"application/json"}, consumes="application/json")
	public ResponseEntity<?> newCustomer(@RequestBody Customer customer) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		if(custDAO.getCustomer(customer.getSheridanId()) != null) {
			log.info("/newCustomer - Customer already exists with ID: " + customer.getSheridanId());
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Customer already exists");
		}
		customer.setWillRecvEmail(true);
		customer.setBlackListed(false);
		custDAO.addCustomer(customer);
		
		log.info("/newCustomer - Added customer with ID: " + customer.getSheridanId());
		objNode.put("message", "Customer added");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/newPayable/{rentalID}", method = RequestMethod.POST, 
			produces = {"application/json"}, consumes="application/json")
	public ResponseEntity<?> newPayable(@PathVariable int rentalID, @RequestBody Payable payable) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		Rental rental = rentalDAO.getRental(rentalID);
		
		if(rental == null) {
			log.info("/newPayable/" + rentalID + " - Error. No rental was found with ID: " + rentalID);
	    	return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No rental was found with ID: " + rentalID);
		} 
		
		int payableID = 0;
		try {
			payableID = payableDAO.addPayable(payable, rental);
			// Customer gets blacklisted if a new Payable was added and if was not blacklisted before
			blackListCustomer(rental.getCustomer());
		} catch (Exception e) {
			log.info("/newPayable/" + rentalID + "-" + e.getMessage());
	    	return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
		
		log.info("/newPayable/"+ rentalID +"/ - Added payable with ID: " + payableID);
		objNode.put("message", "Payable added");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	private void blackListCustomer(Customer customer) throws Exception {
		if (!customer.isBlackListed()) {
			customer.setBlackListed(true);
			custDAO.editCustomer(customer);				
		}
	}

	@RequestMapping(value = "/newRental", method = RequestMethod.POST, 
			produces = {"application/json"}, consumes="application/json")
	public ResponseEntity<?> newRental(@RequestBody Rental rental) {
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		// get all customer data and return error if customer not found
		int sheridanId = rental.getCustomer().getSheridanId();
		Customer customer = custDAO.getCustomer(sheridanId);
		
		if(customer == null) {
			log.info("/newRental - Customer does not exist with ID: " + sheridanId);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer does not exist: " + rental.getCustomer().getSheridanId());
		}
		
		// get all RentalComponents
		List<RentalComponent> rentalComponents = rental.getRentalComponents();
		List<RentalComponent> rentalComponentsUpdated = new ArrayList<RentalComponent>();
		
		// get all data for each rentalComponent. Returns BadRequest in case RentalComponent does
		// not start with valid character
		for (RentalComponent rc : rentalComponents) {
			int id = rc.getId();
			
			if (rc instanceof Bike) {
				Bike bike = bikeDAO.getBikeById(id);
				if(bike == null) {
					log.info("/newRental - Bike does not exist with ID: " + id);
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bike does not exist: " + id);
				} else if(bike.getState() != BikeState.AVAILABLE) {
					log.info("/newRental - Bike is not available with ID: " + id + ". Current status: " + bike.getState());
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bike is not available with ID: " + id + ". Current status: " + bike.getState());
				}
				rentalComponentsUpdated.add(bike);
			} else if (rc instanceof LockItem) {
				LockItem lockItem = lockDAO.getLockItemById(id);
				if(lockItem == null) {
					log.info("/newRental - Lock does not exist with ID: " + id);
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lock does not exist: " + id);
				} else if(lockItem.getState() != LockState.AVAILABLE) {
					log.info("/newRental - Lock is not available with ID: " + id + ". Current status: " + lockItem.getState());
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lock is not available with ID: " + id + ". Current status: " + lockItem.getState());
				}
				rentalComponentsUpdated.add(lockItem);
				
			} else if (rc instanceof Basket) {
				Basket basket = basketDAO.getBasketById(id);
				if(basket == null) {
					log.info("/newRental - Basket does not exist with ID: " + id);
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Basket does not exist: " + id);
				} else if(basket.getState() != BasketState.AVAILABLE) {
					log.info("/newRental - Basket is not available with ID: " + id + ". Current status: " + basket.getState());
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Basket is not available with ID: " + id + ". Current status: " + basket.getState());
				}
				rentalComponentsUpdated.add(basket);
	
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rental Component " + id + " is not valid");
			}
		}
		
		ZonedDateTime today = ZonedDateTime.now(ZoneId.of("America/Toronto"));
		
		rental.setCustomer(customer);
		rental.setRentalComponents(rentalComponentsUpdated);
		rental.setSignOutDate(today.toLocalDate());
		rental.setDueDate(today.toLocalDate().plusDays(7));
		
		try {
			rentalDAO.addRental(rental);
		} catch (Exception e) {
	    	log.info("/newRental - " + e.getMessage());
	    	return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	    }

		log.info("/newRental - Added rental with ID: " + sheridanId);
		objNode.put("message", "Rental was added");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/newBike", method = RequestMethod.POST,
			produces = {"application/json"}, consumes = "application/json")
	public ResponseEntity<?> newBike(@RequestBody Bike newBike) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		if (newBike.getName() == null || newBike.getName().trim().isEmpty()) {
			log.info("/newBike - Bike sent without name");
	    	return ResponseEntity.status(HttpStatus.CONFLICT).body("Bike requires a name");
		}
		
		int newBikeId;
		
		try {
			newBikeId = bikeDAO.addBike(newBike);			
		} catch (Exception e) {
	    	log.info("/newBike - " + e.getMessage());
	    	return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	    }

		log.info("/newBike - Added bike with ID: " + newBikeId);
		objNode.put("message", "Bike was added with id " + newBike.getName());
		objNode.put("id", newBikeId);
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/newLock", method = RequestMethod.POST,
			produces = {"application/json"}, consumes = "application/json")
	public ResponseEntity<?> newLock(@RequestBody LockItem newLock) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		if (newLock.getName() == null || newLock.getName().trim().isEmpty()) {
			log.info("/newLock - Lock sent without name");
	    	return ResponseEntity.status(HttpStatus.CONFLICT).body("Lock requires a name");
		}
		
		int newLockID;
		
		try {
			newLockID = lockDAO.addLockItem(newLock);			
		} catch (Exception e) {
	    	log.info("/newLock - " + e.getMessage());
	    	return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	    }

		log.info("/newLock - Added lock with ID: " + newLockID);
		objNode.put("message", "Lock was added with id " + newLock.getName());
		objNode.put("id", newLockID);
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/newBasket", method = RequestMethod.POST,
			produces = {"application/json"}, consumes = "application/json")
	public ResponseEntity<?> newBasket(@RequestBody Basket newBasket) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		if (newBasket.getName() == null || newBasket.getName().trim().isEmpty()) {
			log.info("/newBasket - Basket sent without name");
	    	return ResponseEntity.status(HttpStatus.CONFLICT).body("Basket requires a name");
		}
		
		int newBasketID;
		
		try {
			newBasketID = basketDAO.addBasket(newBasket);			
		} catch (Exception e) {
	    	log.info("/newBasket - " + e.getMessage());
	    	return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	    }

		log.info("/newBasket - Added basket with ID: " + newBasketID);
		objNode.put("message", "Basket was added with id " + newBasket.getName());
		objNode.put("id", newBasketID);
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value="/editCustomer", method=RequestMethod.PATCH, produces = {"application/json"})
	public ResponseEntity<?> editCustomer(@RequestBody Customer newCustomer) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();		
		
		Customer customer = custDAO.getCustomer(newCustomer.getSheridanId());
		
		if (customer == null) {
			log.info("/editCustomer - Customer was not found with ID: " + newCustomer.getSheridanId());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Customer was not found with ID: " + newCustomer.getSheridanId());
		}
		
		try {
			custDAO.editCustomer(newCustomer);			
		} catch (Exception e) {
	    	log.info("/editCustomer - " + e.getMessage());
	    	return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	    }
		
		log.info("/editCustomer - Edited Customer with ID: " + newCustomer.getSheridanId());
		objNode.put("message", "Customer was updated");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}

	@RequestMapping(value="/customerSignedWaiver/{id}", method=
			RequestMethod.PATCH, produces = {"application/json"})
	public ResponseEntity<?> customerSignedWaiver(@PathVariable int id) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();

		Customer existingCust = custDAO.getCustomer(id);

		if (existingCust == null) {
			log.info("/customerSignedWaiver - Customer was not found with ID: " + id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Customer was not found with ID: " + id);
		}

		try {
			custDAO.updateCustomerWaiver(existingCust);
		} catch (Exception e) {
			log.info("/customerSignedWaiver - " + e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}

		log.info("/customerSignedWaiver - Customer signed waiver with ID: " + id);

		objNode.put("message", "Customer was updated");
		objNode.put("signdate",
				existingCust.getLastWaiverSignedAt().toString());
		objNode.put("expirydate",
				existingCust.getWaiverExpirationDate().toString());
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}


	@RequestMapping(value="/editRental", method=RequestMethod.PATCH, produces = {"application/json"})
	public ResponseEntity<?> editRental(@RequestBody Rental newRental) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();		
		
		Rental rental = rentalDAO.getRental(newRental.getId());
		
		if (rental == null) {
			log.info("/editRental - Rental was not found with ID: " + newRental.getId());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Rental was not found with ID: " + newRental.getId());
		}
		
		try {
			rentalDAO.editRental(newRental);			
		} catch (Exception e) {
	    	log.info("/editRental - " + e.getMessage());
	    	return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	    }
		
		log.info("/editRental - Edited Rental with ID: " + newRental.getId());
		objNode.put("message", "Rental was updated");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value="/editLock", method=RequestMethod.PATCH, produces = {"application/json"})
	public ResponseEntity<?> editLock(@RequestBody LockItem newLock) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		if (newLock.getName() == null || newLock.getName().trim().isEmpty()) {
			log.info("/editLock/{id} - Lock sent without name");
	    	return ResponseEntity.status(HttpStatus.CONFLICT).body("Lock requires a name");
		}
		
		LockItem lock = lockDAO.getLockItemById(newLock.getId());
		
		if (lock == null) {
			log.info("/editLock - LockItem was not found with ID: " + newLock.getId());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Lock was not found with ID: " + newLock.getId());
		}
		
		try {
			lockDAO.editLockItem(newLock);			
		} catch (Exception e) {
	    	log.info("/editLock - " + e.getMessage());
	    	return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	    }
		
		log.info("/editLock - Edited LockItem with ID: " + newLock.getId());
		objNode.put("message", "Lock was updated");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value="/editBasket", method=RequestMethod.PATCH, produces = {"application/json"})
	public ResponseEntity<?> editBasket(@RequestBody Basket newBasket) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		if (newBasket.getName() == null || newBasket.getName().trim().isEmpty()) {
			log.info("/editBasket/{id} - Basket sent without name");
	    	return ResponseEntity.status(HttpStatus.CONFLICT).body("Basket requires a name");
		}
		
		Basket basket = basketDAO.getBasketById(newBasket.getId());
		
		if (basket == null) {
			log.info("/editBasket - Basket was not found with ID: " + newBasket.getId());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Basket was not found with ID: " + basket.getId());
		}
		
		try {
			basketDAO.editBasket(newBasket);			
		} catch (Exception e) {
	    	log.info("/editBasket - " + e.getMessage());
	    	return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	    }
		
		log.info("/editBasketItem - Edited BasketItem with ID: " + newBasket.getId());
		objNode.put("message", "Basket was updated");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value="/editPayable", method=RequestMethod.PATCH, produces = {"application/json"})
	public ResponseEntity<?> editPayable(@RequestBody Payable newPayable) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();		
	
		Payable payable = payableDAO.getPayableById(newPayable.getPayable_id());
		
		if (payable == null) {
			log.info("/editPayable - Payable was not found with ID: " + newPayable.getPayable_id());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Payable was not found with ID: " + newPayable.getPayable_id());
		}
		
		try {
			// updates the rental
			Rental rental = payable.getRental();
			newPayable.setRental(rental);
			payableDAO.editPayable(newPayable);
			blackListCustomer(rental.getCustomer());
		} catch (Exception e) {
	    	log.info("/editPayable - " + e.getMessage());
	    	return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	    }
		
		log.info("/editPayable - Edited Payable with ID: " + newPayable.getPayable_id());
		objNode.put("message", "Payable was updated");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/updatePayables/{rentalId}", method = RequestMethod.PATCH, produces = {"application/json"})
	public ResponseEntity<Object> updatePayables(@PathVariable int rentalId, @RequestBody List<Payable> payablesList) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		List<Payable> result = new ArrayList<Payable>();
		
		List<Payable> payablesInDB = payableDAO.getPayablesByRentalId(rentalId);
		
		boolean isPayablesEmpty = (payablesList == null || payablesList.isEmpty());
		
		if (payablesInDB != null && !payablesInDB.isEmpty()) {
			// check if there are any items in the DB that should be deleted
			for (Payable payableInDB : payablesInDB) {
				// if the Payable in the DB is not contained in the newly sent payablesList, delete it from DB
				if (isPayablesEmpty || !payablesList.contains(payableInDB)) {
					try {
						payableDAO.deletePayable(payableInDB);					
					} catch (Exception e) {
						log.info("/updatePayables - " + e.getMessage());
						return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
					}
				}
			}
		}
		
		// add or update each Payable
		if (!isPayablesEmpty) {
			for (Payable payable : payablesList) {
				try {
					payableDAO.editPayable(payable);
				} catch (Exception e) {
					log.info("/updatePayables - " + e.getMessage());
					return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
				}
			}
			
			// blacklist customer if not yet blacklisted
			try {
				// rental object that comes with the request body does not contain full rental object, just its id
				Rental rental = rentalDAO.getRental(payablesList.get(0).getRental().getId());
				Customer customer = rental.getCustomer();
				blackListCustomer(customer);
			} catch (Exception e) {
				log.info("/updatePayables - " + e.getMessage());
				return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
			}
		}
		
		log.info("/updatePayables - List of Payables updated");
		
		result.addAll(payableDAO.getPayablesByRentalId(rentalId));
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<Object> login(@RequestBody LoginUser loginUser) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		SystemUser sysUser = sysUserDAO.getSysUser(loginUser.getEmail());
		
		// Check if user exists and password is valid
		if(sysUser != null && BCrypt.checkpw(loginUser.getPassword(), sysUser.getPassword())) {
			objNode.put("valid", true);
			objNode.put("role", sysUser.getRole());
		} else {
			objNode.put("valid", false);
		}
		
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}


	@RequestMapping(value = "/download/report.xlsx", method = RequestMethod.GET)
	public ResponseEntity<Object> excelReport() throws Exception {
		List<Rental> rentals = rentalDAO.getAllRentals();
		List<Bike> bikes = bikeDAO.getAllBikes();
		List<LockItem> locks = lockDAO.getAllLockItems();
		List<Basket> baskets = basketDAO.getAllBaskets();
		List<Customer> customers = custDAO.getAllCustomer();
		List<Payable> payables = payableDAO.getAllPayables();

		ByteArrayInputStream in = ExcelGenerator.reportExcel(rentals, bikes,
				locks, baskets, customers, payables);
		// return IOUtils.toByteArray(in);

		HttpHeaders headers = new HttpHeaders();

		//TODO: CHANGE FILENAME TO DATE
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy" +
				"-MM-dd HH:mm");
		String formatedString = date.format(formatter);

		headers.add("Content-Disposition", "attachment; " +
				"filename=BikeHubReport" + formatedString + ".xlsx");

		headers.add("Content-Type", "application/vnd" +
				".openxmlformats-officedocument.spreadsheetml.sheet");

		return ResponseEntity
				.ok()
				.headers(headers)
				.body(new InputStreamResource(in));
	}
}

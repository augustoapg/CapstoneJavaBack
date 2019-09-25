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

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

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
import ca.sheridancollege.enums.BikeState;
import ca.sheridancollege.enums.LockState;
import ca.sheridancollege.utils.DummyDataGenerator;
import ca.sheridancollege.beans.*;
import ca.sheridancollege.beans.SystemUser;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class HomeController {
	BikeDAO bikeDAO = new BikeDAO();
	CustomerDAO custDAO = new CustomerDAO();
	RentalDAO rentalDAO = new RentalDAO();
	SystemUserDAO sysUserDAO = new SystemUserDAO();
	LockDAO lockDAO = new LockDAO();
	RentalComponentDAO rentalComponentDAO = new RentalComponentDAO();
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value = "/addDummyData", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> addDummyData(Model model) {
		DummyDataGenerator dummyData = new DummyDataGenerator();
		
		dummyData.generateRandomBikes(10);
		dummyData.generateRandomCustomer(30);
		dummyData.generateRandomKeyLocks(20, 4);
		dummyData.generateRandomRentals();
		dummyData.generateRandomSystemUsers();
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
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

	@RequestMapping(value = "/getRentals", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getRentals() {
		List<Rental> rentals = rentalDAO.getAllRentals();
		log.info("/getRentals - Getting All Rentals- " + rentals.size() + " retrieved");
		return new ResponseEntity<Object>(rentals, HttpStatus.OK);
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
		log.info("/getLocks - Getting Lock by ID " +id+ " ");
		return new ResponseEntity<Object>(lockItem, HttpStatus.OK);
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
	
	@RequestMapping(value = "/getRental/{id}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getRentalById(@PathVariable int id) {
		Rental rental = rentalDAO.getRental(id);
		
		if (rental == null) {
			log.info("/getRental - Rental not found with ID: " + id);
			return new ResponseEntity<Object>(rental, HttpStatus.NO_CONTENT);
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
	
	@RequestMapping(value = "/getArchivedRentals", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getArchiveRentals() {
		List<Rental> rentals = rentalDAO.getArchiveRentals();
		log.info("/getArchivedRentals - Getting All Archived Rentals- " + rentals.size() + " retrieved");
		
		if (rentals.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No archive rentals were found");
		}
		return new ResponseEntity<Object>(rentals, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getArchiveRental/{id}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getArchiveRental(@PathVariable int id) {
		Rental rental = rentalDAO.getArchiveRental(id);

		if(rental == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Archive rental not found: " + id);
		}
		return new ResponseEntity<Object>(rental, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/editBike", method = RequestMethod.PATCH, produces = {"application/json"})
	public ResponseEntity<?> editBike(@RequestBody Bike newBike) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		Bike bike = bikeDAO.getBikeById(newBike.getId());
	    if (bike == null) {
	    	log.info("/editBike/{id} - Bike not found with ID: " + newBike.getId());
	    	return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Bike not found: " + newBike.getId());
	    }
	    bikeDAO.editBike(newBike);
	    
	    log.info("/editBike/{id} - Edited Bike with ID: " + newBike.getId());
		objNode.put("message", "Bike has been updated");
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
	    
	    rentalDAO.returnRental(rentalToReturn, newRental);
	    
	    log.info("/returnRental - Returned Bike with rental ID: " + newRental.getId());
		objNode.put("message", "Bike has been returned");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/newCustomer", method = RequestMethod.POST, 
			produces = {"application/json"}, consumes="application/json")
	public ResponseEntity<?> newCustomer(@RequestBody Customer customer) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		if(custDAO.getCustomer(customer.getSheridanId()) != null) {
			log.info("/newCustomer - Customer already exists with ID: " + customer.getSheridanId());
			objNode.put("message", "Customer already exists");
	    	return new ResponseEntity<Object>(objNode, HttpStatus.CONFLICT);
		}
		customer.setWillRecvEmail(true);
		customer.setBlackListed(false);
		custDAO.addCustomer(customer);
		
		log.info("/newCustomer - Added customer with ID: " + customer.getSheridanId());
		objNode.put("message", "Customer added");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
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
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bike does not exist: " + id);
				} else if(lockItem.getState() != LockState.AVAILABLE) {
					log.info("/newRental - Lock is not available with ID: " + id + ". Current status: " + lockItem.getState());
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lock is not available with ID: " + id + ". Current status: " + lockItem.getState());
				}
				rentalComponentsUpdated.add(lockItem);
	
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rental Component " + id + " is not valid");
			}
		}
		
		rental.setCustomer(customer);
		rental.setRentalComponents(rentalComponentsUpdated);
		rental.setSignOutDate(LocalDate.now());
		rental.setDueDate(LocalDate.now().plusDays(7));
		
		rentalDAO.addRental(rental);

		log.info("/newRental - Added rental with ID: " + sheridanId);
		objNode.put("message", "Rental was added");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/newBike", method = RequestMethod.POST,
			produces = {"application/json"}, consumes = "application/json")
	public ResponseEntity<?> newBike(@RequestBody Bike newBike) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		int newBikeId = bikeDAO.addBike(newBike);

		log.info("/newBike - Added bike with ID: " + newBike.getId());
		objNode.put("message", "Bike was added with id " + newBikeId);
		objNode.put("id", newBikeId);
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/newLock", method = RequestMethod.POST,
			produces = {"application/json"}, consumes = "application/json")
	public ResponseEntity<?> newLock(@RequestBody LockItem newLock) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		int newLockID = lockDAO.addLockItem(newLock);

		log.info("/newLock - Added lock with ID: " + newLockID);
		objNode.put("message", "Lock was added with id " + newLockID);
		objNode.put("id", newLockID);
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}

	@RequestMapping(value="/editRental", method=RequestMethod.PATCH, produces = {"application/json"})
	public ResponseEntity<?> editRental(@RequestBody Rental newRental) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();		
		
		Rental rental = rentalDAO.getRental(newRental.getId());
		
		if (rental == null) {
			log.info("/editRental - Rental was not found with ID: " + newRental.getId());
			objNode.put("message", "Rental was not found");
			return new ResponseEntity<Object>(objNode, HttpStatus.OK);
		}
		
		rentalDAO.editRental(newRental);
		
		log.info("/editRental - Edited Rental with ID: " + newRental.getId());
		objNode.put("message", "Rental was updated");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value="/editLock", method=RequestMethod.PATCH, produces = {"application/json"})
	public ResponseEntity<?> editLock(@RequestBody LockItem newLock) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();		
		
		LockItem lock = lockDAO.getLockItemById(newLock.getId());
		
		if (lock == null) {
			log.info("/editLock - LockItem was not found with ID: " + newLock.getId());
			objNode.put("message", "LockItem was not found");
			return new ResponseEntity<Object>(objNode, HttpStatus.OK);
		}
		
		lockDAO.editLockItem(newLock);
		
		log.info("/editLockItem - Edited LockItem with ID: " + newLock.getId());
		objNode.put("message", "LockItem was updated");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<Object> login(@RequestBody LoginUser loginUser) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		SystemUser sysUser = sysUserDAO.getSysUser(loginUser.getEmail());
		
		// Check if user exists and password is valid
		if(sysUser != null && BCrypt.checkpw(loginUser.getPassword(), sysUser.getPassword())) {
			objNode.put("valid", true);
			objNode.put("role", "Admin");
			objNode.put("token", "fakeToken"); // TODO: review this
		} else {
			objNode.put("valid", false);
		}
		
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
}

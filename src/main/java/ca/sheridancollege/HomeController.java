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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ca.sheridancollege.dao.*;
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
	KeyLockDAO keyLockDAO = new KeyLockDAO();
	RentalComponentDAO rentalComponentDAO = new RentalComponentDAO();
	
	@RequestMapping(value = "/addDummyData", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> addDummyData(Model model) {
		DummyDataGenerator dummyData = new DummyDataGenerator();
		
		dummyData.generateRandomBikes(10);
		dummyData.generateRandomCustomer(30);
		dummyData.generateRandomKeyLocks(20);
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
		return new ResponseEntity<Object>(bikes, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getLocks", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getLocks() {
		List<LockItem> lockItems = keyLockDAO.getAllLockItems();
		return new ResponseEntity<Object>(lockItems, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getKeys", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getKeys() {
		List<KeyItem> keyItems = keyLockDAO.getAllKeyItems();
		return new ResponseEntity<Object>(keyItems, HttpStatus.OK);
	}

	@RequestMapping(value = "/getCustomers", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getCustomers() {
		List<Customer> customers = custDAO.getAllCustomer();
		return new ResponseEntity<Object>(customers, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/getCustomer/{sheridanIdInput}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getCustomerByID(@PathVariable String sheridanIdInput) {
		int sheridanId = 0;
		try {
			sheridanId = Integer.parseInt(sheridanIdInput);
			Customer customer = custDAO.getCustomer(sheridanId);
			
			// if customer not found
			if (customer == null) {
				return new ResponseEntity<Object>(customer, HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<Object>(customer, HttpStatus.OK);
		} catch (NumberFormatException e) {
			// if input is invalid (cannot convert string to int)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Sheridan ID");
		}
	}
	
	@RequestMapping(value = "/getRental/{id}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getRentalById(@PathVariable int id) {
		Rental rental = rentalDAO.getRental(id);
		
		if (rental == null) {
			return new ResponseEntity<Object>(rental, HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<Object>(rental, HttpStatus.OK);
	}

	@RequestMapping(value = "/getRentals", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getRentals() {
		List<Rental> rentals = rentalDAO.getAllRentals();
		return new ResponseEntity<Object>(rentals, HttpStatus.OK);
	}
	
//	@RequestMapping(value = "/getActiveRentals", method = RequestMethod.GET, produces = { "application/json" })
//	public ResponseEntity<Object> getActiveRentals() {
//		ObjectMapper mapper = new ObjectMapper();
//		ArrayNode arrayNode = mapper.createArrayNode();
//
//		List<Rental> rentals = rentalDAO.getActiveRentals();
//
//		for (Rental r : rentals) {
//			ObjectNode objNode = mapper.createObjectNode();
//			objNode.put("rentalId", r.getId());
//			if  (r.getSignOutDate() != null) objNode.put("signOutDate", r.getSignOutDate().toString());
//			else objNode.put("signOutDate", "null");
//			
//			if  (r.getDueDate() != null) objNode.put("dueDate", r.getDueDate().toString());
//			else objNode.put("dueDate", "null");
//			
//			objNode.put("comment", r.getComment());
//			objNode.put("status", r.getRentalState());
//			
//			Customer customer = r.getCustomer();
//			
//			objNode.put("customerName", customer.getFirstName() + " " + r.getCustomer().getLastName());
//			objNode.put("sheridanId", customer.getSheridanId());
//			objNode.put("sheridanEmail", customer.getSheridanEmail());
//			objNode.put("personalEmail", customer.getPersonalEmail());
//			objNode.put("phone", customer.getPhone());
//			objNode.put("type", customer.getType().getCustomerType());
//			objNode.put("bikeId", r.getBike().getId());
//
//			arrayNode.add(objNode);
//		}
//		return new ResponseEntity<Object>(arrayNode, HttpStatus.OK);
//	}
//	
//	@RequestMapping(value = "/getActiveRental/{id}", method = RequestMethod.GET, produces = { "application/json" })
//	public ResponseEntity<Object> getActiveRental(@PathVariable int id) {
//		ObjectMapper mapper = new ObjectMapper();
//		ObjectNode objNode = mapper.createObjectNode();
//
//		Rental rental = rentalDAO.getRental(id);
//		
//		if(rental != null) {
//			objNode.put("rentalId", rental.getId());
//			if  (rental.getSignOutDate() != null) objNode.put("signOutDate", rental.getSignOutDate().toString());
//			else objNode.put("signOutDate", "null");
//			
//			if  (rental.getDueDate() != null) objNode.put("dueDate", rental.getDueDate().toString());
//			else objNode.put("dueDate", "null");
//			
//			objNode.put("comment", rental.getComment());
//			objNode.put("status", rental.getRentalState());
//			
//			Customer customer = rental.getCustomer();
//			
//			objNode.put("customerName", customer.getFirstName() + " " + rental.getCustomer().getLastName());
//			objNode.put("sheridanId", customer.getSheridanId());
//			objNode.put("sheridanEmail", customer.getSheridanEmail());
//			objNode.put("personalEmail", customer.getPersonalEmail());
//			objNode.put("phone", customer.getPhone());
//			objNode.put("type", customer.getType().getCustomerType());
//			objNode.put("bikeId", rental.getBike().getId());
//
//		} else {
//			objNode.put("message", "Rental not found");
//			return new ResponseEntity<Object>(objNode, HttpStatus.CONFLICT);
//		}
//		
//		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
//	}
	
	@RequestMapping(value = "/getArchivedRentals", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getArchiveRentals() {
		List<Rental> rentals = rentalDAO.getArchiveRentals();
		return new ResponseEntity<Object>(rentals, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/editBike", method = RequestMethod.PATCH, produces = {"application/json"})
	public ResponseEntity<?> editBike(@RequestBody Bike newBike) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
//		Bike bike = bikeDAO.getBikeById(newBike.getId());
//	    if (bike == null) {
//	    	objNode.put("message", "Bike was not found");
//	    	return new ResponseEntity<Object>(objNode, HttpStatus.CONFLICT);
//	    }
//	    newBike.setId(bike.getId());
	    bikeDAO.editBike(newBike);
	    
	    
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
	    	return ResponseEntity.status(HttpStatus.NO_CONTENT).body("This rental has already ended: " + newRental.getId());
	    }
	    
	    rentalDAO.returnRental(rentalToReturn, newRental);
	    
	    
		objNode.put("message", "Bike has been returned");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/newCustomer", method = RequestMethod.POST, 
			produces = {"application/json"}, consumes="application/json")
	public ResponseEntity<?> newCustomer(@RequestBody Customer customer) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		if(custDAO.getCustomer(customer.getSheridanId()) != null) {
			objNode.put("message", "Customer already exists");
	    	return new ResponseEntity<Object>(objNode, HttpStatus.CONFLICT);
		}
		customer.setWillRecvEmail(true);
		customer.setBlackListed(false);
		custDAO.addCustomer(customer);
		
		
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
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer does not exist: " + rental.getCustomer().getSheridanId());
		}
		
		// get all RentalComponents
		List<RentalComponent> rentalComponents = rental.getRentalComponents();
		List<RentalComponent> rentalComponentsUpdated = new ArrayList<RentalComponent>();
		
		// get all data for each rentalComponent. Returns BadRequest in case RentalComponent does
		// not start with valid character
		for (RentalComponent rc : rentalComponents) {
			String rentalComponentId = rc.getId();
			
			switch (rentalComponentId.charAt(0)) {
				case 'B':
					Bike bike = bikeDAO.getBikeById(rentalComponentId);
					if(bike == null) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bike does not exist: " + rentalComponentId);
					}
					rentalComponentsUpdated.add(bike);
					break;
				case 'L':
					LockItem lockItem = keyLockDAO.getLockItemById(rentalComponentId);
					if(lockItem == null) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bike does not exist: " + rentalComponentId);
					}
					rentalComponentsUpdated.add(lockItem);
					break;
				case 'K':
					KeyItem keyItem = keyLockDAO.getKeyItemById(rentalComponentId);
					if(keyItem == null) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bike does not exist: " + rentalComponentId);
					}
					rentalComponentsUpdated.add(keyItem);
					break;
	
				default:
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rental Component " + rentalComponentId + " is not valid");
			}
		}
		
		rental.setCustomer(customer);
		rental.setRentalComponents(rentalComponentsUpdated);
		rental.setSignOutDate(LocalDate.now());
		rental.setDueDate(LocalDate.now().plusDays(7));
		
		rentalDAO.addRental(rental);

		objNode.put("message", "Rental was added");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/newBike", method = RequestMethod.POST,
			produces = {"application/json"}, consumes = "application/json")
	public ResponseEntity<?> newBike(@RequestBody Bike newBike) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		String newBikeId = bikeDAO.addBike(newBike);
		
		objNode.put("message", "Bike was added with id " + newBikeId);
		objNode.put("id", newBikeId);
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}

	@RequestMapping(value="/editRental", method=RequestMethod.PATCH, produces = {"application/json"})
	public ResponseEntity<?> editRental(@RequestBody Rental newRental) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();		
		
		Rental rental = rentalDAO.getRental(newRental.getId());
		
		if (rental == null) {
			objNode.put("message", "Rental was not found");
			return new ResponseEntity<Object>(objNode, HttpStatus.OK);
		}
		
		rentalDAO.editRental(newRental);
		
		objNode.put("message", "Rental was updated");
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

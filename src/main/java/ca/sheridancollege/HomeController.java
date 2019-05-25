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

import java.util.List;
import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import ca.sheridancollege.dao.*;
import ca.sheridancollege.enums.BikeState;
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
	
	@RequestMapping(value = "/addDummyData", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> addDummyData(Model model) {
		DummyDataGenerator dummyData = new DummyDataGenerator();
		
		dummyData.generateRandomBikes(10);
		dummyData.generateRandomCustomer(30);
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
	
	@RequestMapping(value = "/getActiveRentals", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getActiveRentals() {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode arrayNode = mapper.createArrayNode();

		List<Rental> rentals = rentalDAO.getActiveRentals();

		for (Rental r : rentals) {
			ObjectNode objNode = mapper.createObjectNode();
			objNode.put("rentalId", r.getId());
			if  (r.getSignOutDate() != null) objNode.put("signOutDate", r.getSignOutDate().toString());
			else objNode.put("signOutDate", "null");
			
			if  (r.getDueDate() != null) objNode.put("dueDate", r.getDueDate().toString());
			else objNode.put("dueDate", "null");
			
			objNode.put("comment", r.getComment());
			objNode.put("status", r.getRentalState());
			
			Customer customer = r.getCustomer();
			
			objNode.put("customerName", customer.getFirstName() + " " + r.getCustomer().getLastName());
			objNode.put("sheridanId", customer.getSheridanId());
			objNode.put("sheridanEmail", customer.getSheridanEmail());
			objNode.put("personalEmail", customer.getPersonalEmail());
			objNode.put("phone", customer.getPhone());
			objNode.put("type", customer.getType().getCustomerType());
			objNode.put("bikeId", r.getBike().getId());

			arrayNode.add(objNode);
		}
		return new ResponseEntity<Object>(arrayNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getActiveRental/{id}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getActiveRental(@PathVariable int id) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();

		Rental rental = rentalDAO.getRental(id);
		
		if(rental != null) {
			objNode.put("rentalId", rental.getId());
			if  (rental.getSignOutDate() != null) objNode.put("signOutDate", rental.getSignOutDate().toString());
			else objNode.put("signOutDate", "null");
			
			if  (rental.getDueDate() != null) objNode.put("dueDate", rental.getDueDate().toString());
			else objNode.put("dueDate", "null");
			
			objNode.put("comment", rental.getComment());
			objNode.put("status", rental.getRentalState());
			
			Customer customer = rental.getCustomer();
			
			objNode.put("customerName", customer.getFirstName() + " " + rental.getCustomer().getLastName());
			objNode.put("sheridanId", customer.getSheridanId());
			objNode.put("sheridanEmail", customer.getSheridanEmail());
			objNode.put("personalEmail", customer.getPersonalEmail());
			objNode.put("phone", customer.getPhone());
			objNode.put("type", customer.getType().getCustomerType());
			objNode.put("bikeId", rental.getBike().getId());

		} else {
			objNode.put("message", "Rental not found");
			return new ResponseEntity<Object>(objNode, HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getArchivedRentals", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getArchiveRentals() {
		List<Rental> rentals = rentalDAO.getArchiveRentals();
		return new ResponseEntity<Object>(rentals, HttpStatus.OK);
	}
	
	@PutMapping("/bike/{id}")
	public ResponseEntity<Object> updateBike(@RequestBody Bike bike, @PathVariable int id) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		Bike existingBike = bikeDAO.getBikeById(id);

		if (existingBike == null) {
			objNode.put("message", "Bike was not found");
			return ResponseEntity.notFound().build();
		}
			
		bike.setId(id);
		bikeDAO.addBike(bike);
		
		objNode.put("message", "Bike was updated");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	

	
	@RequestMapping(value = "/editBike", method = RequestMethod.PATCH, produces = {"application/json"})
	public ResponseEntity<?> editBike(@RequestBody Bike newBike) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		Bike bike = bikeDAO.getBikeById(newBike.getId());
	    if (bike == null) {
	    	objNode.put("message", "Bike was not found");
	    	return new ResponseEntity<Object>(objNode, HttpStatus.CONFLICT);
	    }
	    newBike.setId(bike.getId());
	    bikeDAO.editBike(newBike);
	    
	    
		objNode.put("message", "Bike has been updated");
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/returnRental", method = RequestMethod.PATCH, produces = {"application/json"})
	public ResponseEntity<?> returnRental(@RequestBody Rental newRental) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		Rental rental = rentalDAO.getRental(newRental.getId());
	    if (rental == null) {
	    	objNode.put("message", "Rental was not found");
	    	return new ResponseEntity<Object>(objNode, HttpStatus.CONFLICT);
	    }
	    // If bike was returned before
	    else if ("Returned".equals(rental.getRentalState().toString()) || 
	    		"Returned Late".equals(rental.getRentalState().toString())) {
	    	objNode.put("message", "This rental has already ended");
	    	return new ResponseEntity<Object>(objNode, HttpStatus.CONFLICT);
	    }
	    rentalDAO.returnRental(newRental, rental);
	    
	    
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
		
		int sheridanId = rental.getCustomer().getSheridanId();
		Customer customer = custDAO.getCustomer(sheridanId);
		int bikeId = rental.getBike().getId();
		Bike bike = bikeDAO.getBikeById(bikeId);
		
		if(customer == null) {
			objNode.put("message", "Customer does not exist");
			return new ResponseEntity<Object>(HttpStatus.CONFLICT);
		} else if(bike == null) {
			objNode.put("message", "Bike does not exist");
			return new ResponseEntity<Object>(HttpStatus.CONFLICT);
		} else if(bike.getBikeState() != BikeState.AVAILABLE) {
			objNode.put("message", "Bike is not available");
			return new ResponseEntity<Object>(HttpStatus.CONFLICT);
		}
		
		rental.setCustomer(customer);
		rental.setBike(bike);
		rental.setSignOutDate(LocalDate.now());
		rental.setDueDate(LocalDate.now().plusDays(7));
		bike.setBikeState(BikeState.RENTED);
		rentalDAO.addRental(rental);
				
		objNode.put("message", "Rental was added");
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

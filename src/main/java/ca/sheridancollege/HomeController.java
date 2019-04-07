package ca.sheridancollege;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javafaker.Faker;
import com.fasterxml.jackson.databind.node.ArrayNode;

import ca.sheridancollege.dao.*;
import ca.sheridancollege.enums.CustomerType;
import ca.sheridancollege.enums.RentalState;
import ca.sheridancollege.utils.DummyDataGenerator;
import ca.sheridancollege.beans.*;
import ca.sheridancollege.beans.SystemUser;

/**
 * 
 * Bike JSON:
 * 
 * [ { "id": 1, "notes": "Scratched on body", "signOutDate": "3919-01-31
 * 00:00:00.0", "isRepairNeeded": false, "available": false }, { "id": 2,
 * "notes": "Scratched on body", "signOutDate": "3919-02-01 00:00:00.0",
 * "isRepairNeeded": false, "available": false } ]
 * 
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class HomeController {
	BikeDAO bikeDAO = new BikeDAO();
	CustomerDAO custDAO = new CustomerDAO();
	RentalDAO rentalDAO = new RentalDAO();
	SystemUserDAO sysUserDAO = new SystemUserDAO();
	

	@RequestMapping("/")
	public String home(Model model) {
		DummyDataGenerator dummyData = new DummyDataGenerator();
		
		dummyData.generateRandomBikes(10);
		dummyData.generateRandomCustomer(30);
		dummyData.generateRandomRentals(30);
		dummyData.generateRandomSystemUsers();
		
		return "Home";
	}

	@RequestMapping(value = "/getBikes", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getBikes() {

		ObjectMapper mapper = new ObjectMapper();
		ArrayNode arrayNode = mapper.createArrayNode();

		List<Bike> bikes = bikeDAO.getAllBikes();

		for (Bike b : bikes) {
			ObjectNode objNode = mapper.createObjectNode();
			objNode.put("id", b.getId());
			objNode.put("notes", b.getNotes());
			objNode.put("available", b.isAvailable());
			objNode.put("isRepairNeeded", b.isRepairNeeded());
			objNode.put("imgPath", b.getImgPath());

			arrayNode.add(objNode);
		}
		return new ResponseEntity<Object>(arrayNode, HttpStatus.OK);
	}

	/**
	 * 
	 * Cust JSON:
	 * 
	 * { "name":'testUser', "sheridanId":'991417298',
	 * "sheridanEmail":'testing@gmail.com', "personalEmail":'personal@gmail.com',
	 * "phone":'123456789' }
	 */

	@RequestMapping(value = "/getCustomers", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getCustomers() {
		List<Customer> c = custDAO.getAllCustomer();

		return new ResponseEntity<Object>(c, HttpStatus.OK);
	}

	@RequestMapping(value = "/getRentals", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getRentals() {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode arrayNode = mapper.createArrayNode();

		List<Rental> rentals = rentalDAO.getAllRentals();

		for (Rental r : rentals) {
			ObjectNode objNode = mapper.createObjectNode();
			objNode.put("id", r.getId());
			if  (r.getSignOutDate() != null) objNode.put("signOutDate", r.getSignOutDate().toString());
			else objNode.put("signOutDate", "null");
			
			if  (r.getDueDate() != null) objNode.put("dueDate", r.getDueDate().toString());
			else objNode.put("dueDate", "null");
			objNode.put("customerID", r.getCustomer().getSheridanId());
//			objNode.put("state", r.getState().toString());
			objNode.put("bikeID", r.getBike().getId());

			arrayNode.add(objNode);
		}
		return new ResponseEntity<Object>(arrayNode, HttpStatus.OK);
	}
	
	// See records of a certain bikes -> bike ID
	

	
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
	
	/*
	 * If found
	 * {
        "rentalId": 11,
        "signOutDate": "2019-04-06 16:42:24.518",
        "dueDate": "2019-04-16 16:42:24.518",
        "comment": "",
        "status": "Active",
        "customerName": "Tessie McCullough",
        "sheridanId": 999999900,
        "sheridanEmail": "TessieMcCullough@sheridan.ca",
        "personalEmail": "TessieMcCullough@gmail.com",
        "phone": "231.829.1717",
        "type": "Faculty",
        "bikeId": 1
     * }
     * 
     * if not found
     * {
        "errorMessage": "Rental not found"
     * }
	 */
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
			objNode.put("errorMessage", "Rental not found");
		}
		
		return new ResponseEntity<Object>(objNode, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getArchiveRentals", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<Object> getArchiveRentals() {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode arrayNode = mapper.createArrayNode();

		List<Rental> rentals = rentalDAO.getArchiveRentals();

		for (Rental r : rentals) {
			ObjectNode objNode = mapper.createObjectNode();
			objNode.put("id", r.getId());
			if  (r.getSignOutDate() != null) objNode.put("signOutDate", r.getSignOutDate().toString());
			else objNode.put("signOutDate", "null");
			
			if  (r.getDueDate() != null) objNode.put("dueDate", r.getDueDate().toString());
			else objNode.put("dueDate", "null");
			objNode.put("customerID", r.getCustomer().getSheridanId());
			objNode.put("state", r.getRentalState());
			objNode.put("bikeID", r.getBike().getId());

			arrayNode.add(objNode);
		}
		return new ResponseEntity<Object>(arrayNode, HttpStatus.OK);
	}
	
	@PutMapping("/bike/{id}")
	public ResponseEntity<Object> updateBike(@RequestBody Bike bike, @PathVariable int id) {

		Bike existingBike = bikeDAO.getBikeById(id);

		if (existingBike == null)
			return ResponseEntity.notFound().build();

		bike.setId(id);

		bikeDAO.addBike(bike);

		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<Object> login(@RequestBody LoginUser loginUser) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objNode = mapper.createObjectNode();
		
		SystemUser sysUser = sysUserDAO.getSysUser(loginUser.getEmail());
		
		// check if user exists and password is valid
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

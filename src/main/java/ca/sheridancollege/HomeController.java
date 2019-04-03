package ca.sheridancollege;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import ca.sheridancollege.dao.*;
import ca.sheridancollege.enums.RentalState;
import ca.sheridancollege.beans.*;

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

@RestController
public class HomeController {
	BikeDAO bikeDAO = new BikeDAO();
	CustomerDAO custDAO = new CustomerDAO();
	RentalDAO rentalDAO = new RentalDAO();

	@RequestMapping("/")
	public String home(Model model) {
		for (int i = 0; i < 10; i++) {
			Bike bike = new Bike("Scratched on body", true, true, null);

			if (Math.random() <= 0.5) {
				bike.setImgPath("1.jpg");
				bike.setAvailable(true);
			} else {
				bike.setImgPath("2.jpg");
				bike.setAvailable(false);
			}
			bikeDAO.addBike(bike);

			Customer cust = new Customer("FName" + i, "LName" + i, i + " Capstone Street", "email" + i + "@email.com",
					999999990 + i, "647999999" + i, true, true, "Some notes");
			custDAO.addCustomer(cust);
		}
		Customer cust = custDAO.getCustomer(2);
		
		Rental rent = new Rental(null, null, null, RentalState.ACTIVE, bikeDAO.getAllBikes().get(0));
		rent.setCustomer(cust);
		
		rentalDAO.addRental(rent);
		
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
			objNode.put("customerID", r.getCustomer().getId());
			objNode.put("state", r.getState().toString());
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
	
	
}

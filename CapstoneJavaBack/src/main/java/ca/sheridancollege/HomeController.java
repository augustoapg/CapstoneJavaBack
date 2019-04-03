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

import ca.sheridancollege.dao.Dao;
import ca.sheridancollege.beans.*;

/** 
 * 
 * Bike JSON:
 * 
 * [
	    {
	        "id": 1,
	        "notes": "Scratched on body",
	        "signOutDate": "3919-01-31 00:00:00.0",
	        "isRepairNeeded": false,
	        "available": false
	    },
	    {
	        "id": 2,
	        "notes": "Scratched on body",
	        "signOutDate": "3919-02-01 00:00:00.0",
	        "isRepairNeeded": false,
	        "available": false
	    }
   ]

 */

@RestController
public class HomeController {
	Dao dao = new Dao();
	
	@RequestMapping("/")
	public String home(Model model) {
		for (int i = 0; i < 10; i ++) {
			Bike bike = new Bike("Scratched on body", new Date(2019, 1, i), false, null);
			
			if (Math.random() <= 0.5) {
				bike.setImgPath("1.jpg");
			}
			else bike.setImgPath("2.jpg");
			dao.addBike(bike);
			
			Customer cust = new Customer("FName" + i, "LName" + i, i + " Capstone Street"
					, "email"+i+"@email.com", 999999990 + i, "647999999" + i, 
					true, true, "Some notes");
			dao.addCustomer(cust);
		}
		return "Home";
	}
	
	@RequestMapping(value="/getBikes", method=RequestMethod.GET, produces= {"application/json"}) 
	public ResponseEntity<Object> getBikes() {
		
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode arrayNode = mapper.createArrayNode();
		
		List<Bike> bikes = dao.getAllBikes();
		
		for(Bike b : bikes) {
			ObjectNode objNode = mapper.createObjectNode();
			objNode.put("id", b.getId());
			objNode.put("notes", b.getNotes());
			objNode.put("signOutDate", b.getSignOutDate().toString());
			objNode.put("isRepairNeeded", b.isRepairNeeded());
			if(b.getSignOutDate() != null) {
				objNode.put("available", false);
			} else {
				objNode.put("available", true);
			}
			arrayNode.add(objNode);
		}
		return new ResponseEntity<Object>(arrayNode, HttpStatus.OK);
	}
	
	/**
	 * 	  
	  Cust JSON: 
	  
		{
		  "name":'testUser',
		  "sheridanId":'991417298',
		  "sheridanEmail":'testing@gmail.com',
		  "personalEmail":'personal@gmail.com',
		  "phone":'123456789'
	  }
	 */
	
	@RequestMapping(value="/getCustomers", method=RequestMethod.GET, produces= {"application/json"}) 
	public ResponseEntity<Object> getCustomers() {
		List<Customer> c = dao.getAllCustomer();
		
		return new ResponseEntity<Object>(c, HttpStatus.OK);
	}
}






















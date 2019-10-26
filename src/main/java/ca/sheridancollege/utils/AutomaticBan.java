package ca.sheridancollege.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ca.sheridancollege.beans.Customer;
import ca.sheridancollege.beans.Rental;
import ca.sheridancollege.dao.CustomerDAO;
import ca.sheridancollege.dao.RentalDAO;

@Component
public class AutomaticBan {
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * this function will retrieve all late Rentals, get the Customers associated
	 * with those Rentals and black list them (ban).
	 * It will run once everyday automatically at 1AM (01:00:00 AM)
	 */
	@Scheduled(cron = "0 1 18 * * ?")
	public void automaticBanCustomers() {
		RentalDAO rentalDAO = new RentalDAO();
		CustomerDAO customerDAO = new CustomerDAO();
		log.info("Starting to run the automaticBanCustomers() function");
		List<Rental> lateRentals = rentalDAO.getLateRentals();
		
		for (Rental rental : lateRentals) {
			Customer customer = rental.getCustomer();
			
			try {
				if (!customer.isBlackListed()) {
					customer.setBlackListed(true);
					customerDAO.editCustomer(customer);
					log.info("Black listed Customer with ID: " + customer.getSheridanId());
				}
			} catch (Exception e) {
				log.error("Error black listing Customer in automaticBanCustomers: " + customer.getSheridanId());
			}
		}
	}
}

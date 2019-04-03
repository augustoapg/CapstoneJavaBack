package ca.sheridancollege.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ca.sheridancollege.beans.Customer;
import ca.sheridancollege.beans.Rental;

public class RentalDAO {
	SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

	public void addRental(Rental rental) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Rental r = new Rental(rental.getSignOutDate(), rental.getDueDate(),
				rental.getCustomer(), rental.getState(), 
				rental.getRentalItem());

		// http://www.java2s.com/Tutorials/Java/JPA/0920__JPA_ManyToOne_Join_Column.htm
		// When adding Customer to Rental:

		//		Rental r = new Rental();
		//		Customer customer = new Customer();
		//		customer.setRental(r);

		session.save(r);

		session.getTransaction().commit();
		session.close();
	}
	
//	public void addCustToRental(Rental rental, Customer cust) {
//		
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//
//		
//		// http://www.java2s.com/Tutorials/Java/JPA/0920__JPA_ManyToOne_Join_Column.htm
//		// When adding Customer to Rental:
//	
//		
//
//		session.getTransaction().commit();
//		session.close();
//		
//	}


	public void deleteRental(int id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Rental r = (Rental) session.get(Rental.class, id);

		session.delete(r);

		session.getTransaction().commit();
		session.close();
	}


	public Rental getRental(int id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Rental.byID");
		query.setParameter("id", id);

		List<Rental> rentals = (List<Rental>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!rentals.isEmpty()) {
			return rentals.get(0);
		}

		return null;
	}

	public List<Rental> getAllRentals() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Rental.all");

		List<Rental> rentals = (List<Rental>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return rentals;
	}

}

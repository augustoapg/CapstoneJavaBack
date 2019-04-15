package ca.sheridancollege.dao;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ca.sheridancollege.beans.Rental;

public class RentalDAO {
	SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

	public void addRental(Rental rental) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		rental.getBike().setAvailable(false);
		session.save(rental);

		session.getTransaction().commit();
		session.close();
	}

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

	public List<Rental> getActiveRentals() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Rental.active");
		List<Rental> rentals = (List<Rental>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return rentals;
	}

	public List<Rental> getArchiveRentals() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Rental.archive");
		List<Rental> rentals = (List<Rental>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return rentals;
	}

	public void returnRental(Rental newRental, Rental rental) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		rental.setComment(newRental.getComment());
		rental.setReturnedDate(LocalDate.now());
		
		session.update(rental);

		session.getTransaction().commit();
		session.close();
		
	}

	public void editRental(Rental newRental) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Rental rental = session.get(Rental.class, newRental.getId());
		rental.setDueDate(newRental.getDueDate());
		rental.setComment(newRental.getComment());
		
		session.getTransaction().commit();
		session.close();
	}
	
	
}












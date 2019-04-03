package ca.sheridancollege.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ca.sheridancollege.beans.Bike;
import ca.sheridancollege.beans.KeyLock;
import ca.sheridancollege.beans.Rental;
import ca.sheridancollege.beans.Customer;

public class Dao {

	SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

	// ------------------------------ BIKE -----------------------------------

	public void addBike(Bike bike) {

		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Bike b = new Bike(bike.getNotes(), bike.getSignOutDate(), bike.isRepairNeeded(), bike.getImgPath());

		session.save(b);

		session.getTransaction().commit();
		session.close();

	}

	public Bike getBikeById(int id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Bike.byID");
		query.setParameter("id", id);

		List<Bike> bikes = (List<Bike>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!bikes.isEmpty()) {
			return bikes.get(0);
		}

		return null;
	}

	public List<Bike> getAllBikes() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Bike.all");

		List<Bike> bikes = (List<Bike>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return bikes;
	}

	// ------------------------------ KeyLock -----------------------------------

//	// TODO: addKeyLock(KeyLock keyLock)
//	public void addKeyLock(KeyLock keyLock) {
//
//		// Open connection, use in every method that accesses DB
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//
//		KeyLock k = new KeyLock(keyLock.getNotes(), keyLock.getSignOutDate());
//
//		// Add to DB
//		session.save(k);
//
//		// Close connection, use in end of every method
//		session.getTransaction().commit();
//		session.close();
//
//	}
//
//	// TODO: KeyLock getKeyLock(int id)
//	public KeyLock getKeyLock(int id) {
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//
//		Query query = session.getNamedQuery("KeyLock.byID");
//		query.setParameter("id", id);
//
//		List<KeyLock> keyLocks = (List<KeyLock>) query.getResultList();
//
//		session.getTransaction().commit();
//		session.close();
//
//		if (!keyLocks.isEmpty()) {
//			return keyLocks.get(0);
//		}
//
//		return null;
//	}
//
//	// TODO: List<KeyLock> getAllKeyLocks()
//	public List<KeyLock> getAllKeyLocks() {
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//
//		Query query = session.getNamedQuery("KeyLock.all");
//
//		List<KeyLock> keyLocks = (List<KeyLock>) query.getResultList();
//
//		session.getTransaction().commit();
//		session.close();
//
//		return keyLocks;
//	}

	// ------------------------------ Rental -----------------------------------

	public void addRental(Rental rental) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Rental r = new Rental(rental.getDueDate(), rental.getRenter(), rental.getState(), rental.getRentalItem());

		// http://www.java2s.com/Tutorials/Java/JPA/0920__JPA_ManyToOne_Join_Column.htm
		// When adding Customer to Rental:

		//		Rental r = new Rental();
		//		Customer customer = new Customer();
		//		customer.setRental(r);

		session.save(r);

		session.getTransaction().commit();
		session.close();
	}

	// TODO: deleteRentalRecord
	public void deleteRental(int id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Rental r = (Rental) session.get(Rental.class, id);

		session.save(r);

		session.getTransaction().commit();
		session.close();
	}

	// TODO: getRentalRecord
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

	// TODO: getAllRentalRecords

	public List<Rental> getAllRentals() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Rental.all");

		List<Rental> rentals = (List<Rental>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return rentals;
	}

	// ------------------------------ Customer -----------------------------------

	public void addCustomer(Customer customer) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Customer c = new Customer(customer.getFirstName(), customer.getLastName(), customer.getAddress(),
				customer.getEmail(), customer.getOneCardNum(), customer.getPhone(), customer.getIsBlackListed(),
				customer.getWillRecvEmail(), customer.getNotes());

		session.save(c);

		session.getTransaction().commit();
		session.close();
	}

	// TODO: getCustomer
	public Customer getCustomer(int id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Customer.byID");
		query.setParameter("id", id);

		List<Customer> custs = (List<Customer>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!custs.isEmpty()) {
			return custs.get(0);
		}

		return null;
	}

	public List<Customer> getAllCustomer(int id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Customer.all");

		List<Customer> custs = (List<Customer>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return custs;
	}

	// ------------------------------ SystemUser -----------------------------------

	public void addSysUser(Customer customer) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Customer c = new Customer(customer.getFirstName(), customer.getLastName(), customer.getAddress(),
				customer.getEmail(), customer.getOneCardNum(), customer.getPhone(), customer.getIsBlackListed(),
				customer.getWillRecvEmail(), customer.getNotes());

		session.save(c);

		session.getTransaction().commit();
		session.close();
	}

	// TODO: getSysUser

	// ------------------------------ Visitor -----------------------------------

	// TODO: addVisitor

	// TODO: getVisitor

	// TODO: getAllVisitor
}

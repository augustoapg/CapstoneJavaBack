package ca.sheridancollege.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ca.sheridancollege.beans.Bike;
import ca.sheridancollege.beans.LockItem;
import ca.sheridancollege.beans.Payable;
import ca.sheridancollege.beans.Rental;

public class PayableDAO {

	SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
	RentalComponentDAO rentalComponentDAO = new RentalComponentDAO();

	public Payable getPayableById(int id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Payable.byID");
		query.setParameter("id", id);

		List<Payable> payables = (List<Payable>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!payables.isEmpty()) {
			return payables.get(0);
		}

		return null;
	}
	
	public List<Payable> getAllPayables() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Payable.all");

		List<Payable> payables = (List<Payable>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return payables;
	}
	
	public int addPayable(Payable payable, Rental rental) throws Exception {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		payable.setRental(rental);
		session.save(payable);
		
		session.getTransaction().commit();
		session.close();
		
		return payable.getPayable_id();
	}
	
	public List<Payable> getPayablesByCustId(int id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Payable.byCustId");
		query.setParameter("id", id);

		List<Payable> payables = (List<Payable>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!payables.isEmpty()) {
			return payables;
		}

		return null;
	}
	
	public List<Payable> getPayablesByRentalId(int id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Payable.byRentalId");
		query.setParameter("id", id);

		List<Payable> payables = (List<Payable>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!payables.isEmpty()) {
			return payables;
		}

		return null;
	}
	
	public void editPayable(Payable payable) throws Exception {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		// using try-catch-finally so that no matter what happens, the session will be closed at the end
		try {
			session.saveOrUpdate(payable);
		} catch (Exception e) {
			throw e;
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
	}
	
}

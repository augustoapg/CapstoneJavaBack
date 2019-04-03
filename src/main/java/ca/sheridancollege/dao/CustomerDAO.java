package ca.sheridancollege.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ca.sheridancollege.beans.Customer;

public class CustomerDAO {
	SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

	public void addCustomer(Customer customer) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Customer c = new Customer(customer.getFirstName(), customer.getLastName(), customer.getAddress(),
				customer.getSheridanEmail(), customer.getSheridanId(), customer.getPhone(), customer.isBlackListed(),
				customer.isWillRecvEmail(), customer.getNotes());

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

	public List<Customer> getAllCustomer() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Customer.all");

		List<Customer> custs = (List<Customer>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return custs;
	}

}

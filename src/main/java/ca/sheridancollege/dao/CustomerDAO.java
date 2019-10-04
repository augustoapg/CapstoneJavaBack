package ca.sheridancollege.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ca.sheridancollege.beans.Customer;

public class CustomerDAO {
	SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

	public void addCustomer(Customer customer) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		session.save(customer);

		session.getTransaction().commit();
		session.close();
	}

	public Customer getCustomer(int sheridanId) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Customer.byID");
		query.setParameter("sheridanId", sheridanId);

		List<Customer> custs = (List<Customer>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!custs.isEmpty()) {
			return custs.get(0);
		}

		return null;
	}
	
	public List<Customer> getCustomersByName(String name) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		List<Customer> result = new ArrayList<Customer>();
		
		
		if (name != null) {
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Customer> criteria = cb.createQuery(Customer.class);
			Root<Customer> root = criteria.from(Customer.class);
			if (name.contains(" ")) {
				String[] splittedName = name.split(" ");
				String firstName = name.split(" ")[0];
				String lastName = name.split(" ")[splittedName.length-1];
				criteria.where( cb.or(
						cb.like(root.get("firstName"), "%"+firstName+"%"), 
						cb.like(root.get("lastName"), "%"+lastName+"%")
					));
			} 
			
			else 
			
			{
				criteria.where( 
					cb.or(
						cb.like(root.get("firstName"), "%"+name+"%"), 
						cb.like(root.get("lastName"), "%"+name+"%")
					));
			}
			
			result.addAll(session.createQuery(criteria).getResultList());
		}

		session.getTransaction().commit();
		session.close();

		if (!result.isEmpty()) {
			
			return result;
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

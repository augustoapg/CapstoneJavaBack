package ca.sheridancollege.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import ca.sheridancollege.beans.Bike;
import ca.sheridancollege.beans.Customer;
import ca.sheridancollege.utils.RegexCheck;

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
	
	public List<Customer> searchCustomers(String keyword) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		List<Customer> result = new ArrayList<Customer>();
		
		
		if (!keyword.isEmpty() && keyword != null) {
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Customer> criteria = cb.createQuery(Customer.class);
			Root<Customer> root = criteria.from(Customer.class);
			
			String firstName = keyword;
			String lastName = keyword;
			int id = 0;
			
			if (RegexCheck.isNumeric(keyword)) 
			{
				id = Integer.parseInt(keyword);
				
				Criteria cri = session.createCriteria(Customer.class);
				cri.add(Restrictions.sqlRestriction("sheridanId::varchar(255) LIKE '%"+keyword+"%' "));
				
				result.addAll(cri.list());
			}
			
			else 
			{
				
				if (keyword.contains(" ")) {
					String[] splittedName = keyword.split(" ");
					firstName = keyword.split(" ")[0];
					lastName = keyword.split(" ")[splittedName.length-1];
					System.out.println(firstName + lastName);
				}
				
				criteria.where( cb.or(
						cb.like(root.get("sheridanEmail"), "%"+keyword+"%") ,
						cb.like(root.get("personalEmail"), "%"+keyword+"%") ,
						cb.like(root.get("firstName"), "%"+firstName+"%"), 
						cb.like(root.get("lastName"), "%"+lastName+"%")
					));
			
				result.addAll(session.createQuery(criteria).getResultList());
				
			}
			
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
	
	public void editCustomer(Customer cust) throws Exception {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		// using try-catch-finally so that no matter what happens, the session will be closed at the end
		try {
			Customer existingCust = getCustomer(cust.getSheridanId());
			String errorMessage = "";
			
			// verify if there is another bike (one with a different ID) with the new name already in the DB
			if (existingCust == null || existingCust.getSheridanId() == cust.getSheridanId()) {
				session.update(cust);
			} else {
				errorMessage = "There is already a customer" + cust.getSheridanId() + " registered)";
			}

			if (!errorMessage.isEmpty()) {
				throw new IllegalArgumentException(errorMessage);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	}

}

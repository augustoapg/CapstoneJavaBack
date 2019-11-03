package ca.sheridancollege.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import ca.sheridancollege.beans.PreDefinedPayable;
import ca.sheridancollege.utils.HibernateUtil;

public class PreDefinedPayableDAO {

	public int addPreDefinedPayable(PreDefinedPayable preDefinedPayable) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		try {
			PreDefinedPayable existingPayable = getPredefinedPayableByCategory(preDefinedPayable.getCategory());
			
			if (existingPayable == null) {
				session.save(preDefinedPayable);
			} else {
				throw new Exception("Pre Defined Payable already exists if category " + preDefinedPayable.getCategory());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		
		return preDefinedPayable.getId();
	}
	
	public PreDefinedPayable getPredefinedPayableByID(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("PreDefPayable.byID");
		query.setParameter("id", id);

		List<PreDefinedPayable> preDefinedPayables = (List<PreDefinedPayable>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!preDefinedPayables.isEmpty()) {
			return preDefinedPayables.get(0);
		}

		return null;
	}
	
	public PreDefinedPayable getPredefinedPayableByCategory(String category) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("PreDefPayable.byCategory");
		query.setParameter("category", category);

		List<PreDefinedPayable> preDefinedPayables = (List<PreDefinedPayable>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!preDefinedPayables.isEmpty()) {
			return preDefinedPayables.get(0);
		}

		return null;
	}
	
	public List<PreDefinedPayable> getAllPreDefinedPayables() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("PreDefPayable.all");
		List<PreDefinedPayable> preDefinedPayables = (List<PreDefinedPayable>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return preDefinedPayables;
	}
	
	public void editPreDefinedPayable(PreDefinedPayable preDefinedPayable) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		try {
			PreDefinedPayable existingPayable = getPredefinedPayableByCategory(preDefinedPayable.getCategory());
			
			if (existingPayable == null || existingPayable.getId() == preDefinedPayable.getId()) {
				session.update(preDefinedPayable);
			} else {
				throw new Exception("Pre Defined Payable already exists with category " + preDefinedPayable.getCategory());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	}
	
	public void deletePreDefinedPayable(PreDefinedPayable preDefinedPayable) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		session.delete(preDefinedPayable);
		
		session.getTransaction().commit();
		session.close();
	}
}

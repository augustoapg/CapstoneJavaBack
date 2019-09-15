package ca.sheridancollege.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ca.sheridancollege.beans.LockItem;
import ca.sheridancollege.beans.RentalComponent;

public class LockDAO {

	SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
	RentalComponentDAO rentalComponentDAO = new RentalComponentDAO();
	
	public String addLockItem(LockItem lockItem) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		if(lockItem.getId() == null) {
			lockItem.setId(rentalComponentDAO.getNewRentalComponentId("L"));			
		}
		
		session.save(lockItem);

		session.getTransaction().commit();
		session.close();
		
		return lockItem.getId();
	}
	
	public void editLockItem(LockItem lockItem) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		session.update(lockItem);

		session.getTransaction().commit();
		session.close();
	}
	
	public LockItem getLockItemById(String id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("LockItem.byID");
		query.setParameter("id", id);

		List<LockItem> lockItems = (List<LockItem>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!lockItems.isEmpty()) {
			return lockItems.get(0);
		}

		return null;
	}
	
	public List<LockItem> getAllLockItems() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("LockItem.all");

		List<LockItem> lockItems = (List<LockItem>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return lockItems;
	}
}

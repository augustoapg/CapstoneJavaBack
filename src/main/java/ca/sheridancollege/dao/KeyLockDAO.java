package ca.sheridancollege.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ca.sheridancollege.beans.KeyItem;
import ca.sheridancollege.beans.LockItem;
import ca.sheridancollege.beans.RentalComponent;
import ca.sheridancollege.enums.KeyState;

public class KeyLockDAO {

	SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
	RentalComponentDAO rentalComponentDAO = new RentalComponentDAO();
	
	public void addKeyItem(KeyItem keyItem) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		if(keyItem.getId() == null) {			
			keyItem.setId(rentalComponentDAO.getNewRentalComponentId("K"));
		}
		session.save(keyItem);

		session.getTransaction().commit();
		session.close();
	}
	
	public void addLockItem(LockItem lockItem) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		if(lockItem.getId() == null) {
			lockItem.setId(rentalComponentDAO.getNewRentalComponentId("L"));			
		}
		session.save(lockItem);

		session.getTransaction().commit();
		session.close();
	}
	
	public void addLockWithNumOfKeys(LockItem lockItem, int numOfKeys) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		String lockId = "";
		String keyId = "";
		List<KeyItem> keyItems = new ArrayList<KeyItem>();

		// create new id if not yet there, or get old id
		if(lockItem.getId() == null) {
			lockId = rentalComponentDAO.getNewRentalComponentId("L");
			lockItem.setId(lockId);
		} else {
			lockId = lockItem.getId();
		}

		// create keys and add them to DB
		// key ids will be Kxyz-i, where xyz will match the lock's id and i will be the copy number (ex: K002-3) 
		for(int i = 0; i < numOfKeys; i++) {
			keyId = lockId.replace('L', 'K') + "-" + i;
			KeyItem keyItem = new KeyItem(KeyState.AVAILABLE);
			keyItem.setId(keyId);
			addKeyItem(keyItem);
			keyItems.add(keyItem);
		}
		
		// add list of key items to lock
		lockItem.setKeyItems(keyItems);
		
		session.save(lockItem);

		session.getTransaction().commit();
		session.close();
	}
	
	public void editKeyItem(KeyItem keyItem) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		session.update(keyItem);

		session.getTransaction().commit();
		session.close();
	}
	
	public void editLockItem(LockItem lockItem) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		session.update(lockItem);

		session.getTransaction().commit();
		session.close();
	}

	public KeyItem getKeyItemById(String id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("KeyItem.byID");
		query.setParameter("id", id);

		List<KeyItem> keyItems = (List<KeyItem>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!keyItems.isEmpty()) {
			return keyItems.get(0);
		}

		return null;
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

	public List<KeyItem> getAllKeyItems() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("KeyItem.all");

		List<KeyItem> keyItems = (List<KeyItem>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return keyItems;
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

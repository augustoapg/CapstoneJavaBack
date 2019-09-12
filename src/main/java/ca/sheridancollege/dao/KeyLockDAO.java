package ca.sheridancollege.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ca.sheridancollege.beans.KeyItems;
import ca.sheridancollege.beans.LockItem;
import ca.sheridancollege.beans.RentalComponent;
import ca.sheridancollege.enums.KeyState;

public class KeyLockDAO {

	SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
	RentalComponentDAO rentalComponentDAO = new RentalComponentDAO();
		
	public List<LockItem> getAllKeysLocks() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("LockItem.all");

		List<LockItem> lockItems = (List<LockItem>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return lockItems;
	}
	
	public LockItem getLockByID(String keyID) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("LockItem.byID");
		query.setParameter("id", keyID);
		LockItem lockItem = (LockItem) query.getResultList().stream().findFirst().orElse(null);

		// List<KeyItem> keyItem = (List<KeyItem>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return lockItem;
	}
	
	public LockItem getLockByKeyID(String keyID) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("LockItem.byID");
		query.setParameter("id", keyID);
		KeyItems keyItem = (KeyItems) query.getResultList().stream().findFirst().orElse(null);;
		LockItem lockItem = keyItem.getLockItem();

		// List<KeyItem> keyItem = (List<KeyItem>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return lockItem;
	}
	
	
	public void addKeyItem(KeyItems keyItem) {
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
		List<KeyItems> keyItems = new ArrayList<KeyItems>();

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
			KeyItems keyItem = new KeyItems(KeyState.AVAILABLE);
			keyItem.setId(keyId);
			addKeyItem(keyItem);
			keyItems.add(keyItem);
			keyItem.setLockItem(lockItem);
		}
		
		// add list of key items to lock
		lockItem.setKeyItems(keyItems);
		
		session.save(lockItem);

		session.getTransaction().commit();
		session.close();
	}
	
	public void editKeyItem(KeyItems keyItem) {
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

	public KeyItems getKeyItemById(String id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("KeyItem.byID");
		query.setParameter("id", id);

		List<KeyItems> keyItems = (List<KeyItems>) query.getResultList();

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
	
	public String getLockItemIdByKeyId(String id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Query query = session.getNamedQuery("LockItem.getKeysByLockID");
		query.setParameter("keyItems_id", id);

		//TODO: List<String> lockItemId = (List<String>) query.getResultList();

		session.getTransaction().commit();
		session.close();

//		if (!lockItemId.isEmpty()) {
//			return lockItemId.get(0);
//		}

		return null;
	}

//	public List<KeyItems> getAllKeyItems() {
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//
//		Query query = session.getNamedQuery("KeyItem.all");
//
//		List<KeyItems> keyItems = (List<KeyItems>) query.getResultList();
//
//		session.getTransaction().commit();
//		session.close();
//
//		return keyItems;
//	}	
	
	
}

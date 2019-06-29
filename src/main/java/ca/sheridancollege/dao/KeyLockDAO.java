package ca.sheridancollege.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ca.sheridancollege.beans.KeyItem;
import ca.sheridancollege.beans.LockItem;

public class KeyLockDAO {

	SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
	
	public void addKeyItem(KeyItem keyItem) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		session.save(keyItem);

		session.getTransaction().commit();
		session.close();
	}
	
	public void addLockItem(LockItem lockItem) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

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

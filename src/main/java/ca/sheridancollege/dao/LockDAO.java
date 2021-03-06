package ca.sheridancollege.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import ca.sheridancollege.beans.LockItem;
import ca.sheridancollege.enums.LockState;
import ca.sheridancollege.utils.HibernateUtil;

public class LockDAO {
	
	public int addLockItem(LockItem lockItem) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		// using try-catch-finally so that no matter what happens, the session will be closed at the end
		try {
			LockItem existingLock = getLockItemByName(lockItem.getName());
			String errorMessage = "";
			
			// verify if there is a lock with this name already in the DB
			if (existingLock == null) {
				if (lockItem.getState() == null) {
					lockItem.setState(LockState.AVAILABLE);
				}
				session.save(lockItem);
			} else {
				errorMessage = "There is already a lock with name " + lockItem.getName() + " registered (Lock ID: " + existingLock.getId() + ")";
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
		
		return lockItem.getId();
	}
	
	public void editLockItem(LockItem lockItem) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		// using try-catch-finally so that no matter what happens, the session will be closed at the end
		try {
			LockItem existingLock = getLockItemByName(lockItem.getName());
			String errorMessage = "";
			
			// verify if there is another lock (with different ID) with this name already in the DB
			if (existingLock == null || lockItem.getId() == existingLock.getId()) {
				session.update(lockItem);
			} else {
				errorMessage = "There is already a lock with name " + lockItem.getName() + " registered (Lock ID: " + existingLock.getId() + ")";
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
	
	public LockItem getLockItemById(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
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
	
	public LockItem getLockItemByName(String name) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("LockItem.byName");
		query.setParameter("name", name);

		List<LockItem> lockItems = (List<LockItem>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!lockItems.isEmpty()) {
			return lockItems.get(0);
		}

		return null;
	}
	
	public List<LockItem> getAllLockItems() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("LockItem.all");

		List<LockItem> lockItems = (List<LockItem>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return lockItems;
	}
}

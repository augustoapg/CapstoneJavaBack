package ca.sheridancollege.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import ca.sheridancollege.beans.Basket;
import ca.sheridancollege.enums.BasketState;
import ca.sheridancollege.enums.LockState;
import ca.sheridancollege.utils.HibernateUtil;

public class BasketDAO {
	RentalComponentDAO rentalComponentDAO = new RentalComponentDAO();
	
	public int addBasket(Basket basket) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		// using try-catch-finally so that no matter what happens, the session will be closed at the end
		try {
			Basket existingBasket = getBasketByName(basket.getName());
			String errorMessage = "";
			
			// verify if there is a basket with this name already in the DB
			if (existingBasket == null) {
				if (basket.getState() == null) {
					basket.setState(BasketState.AVAILABLE);
				}
				session.save(basket);
			} else {
				errorMessage = "There is already a basket with name " + basket.getName() + " registered (Basket ID: " + existingBasket.getId() + ")";
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
		
		return basket.getId();
	}
	
	public void editBasket(Basket basket) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		// using try-catch-finally so that no matter what happens, the session will be closed at the end
		try {
			Basket existingBasket = getBasketByName(basket.getName());
			String errorMessage = "";
			
			// verify if there is another basket (with different ID) with this name already in the DB
			if (existingBasket == null || basket.getId() == existingBasket.getId()) {
				session.update(basket);
			} else {
				errorMessage = "There is already a Basket with name " + basket.getName() + " registered (Basket ID: " + existingBasket.getId() + ")";
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
	
	public Basket getBasketById(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Basket.byID");
		query.setParameter("id", id);

		List<Basket> baskets = (List<Basket>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!baskets.isEmpty()) {
			return baskets.get(0);
		}

		return null;
	}
	
	public Basket getBasketByName(String name) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Basket.byName");
		query.setParameter("name", name);

		List<Basket> baskets = (List<Basket>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!baskets.isEmpty()) {
			return baskets.get(0);
		}

		return null;
	}
	
	public List<Basket> getAllBaskets() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Basket.all");

		List<Basket> baskets = (List<Basket>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return baskets;
	}
}

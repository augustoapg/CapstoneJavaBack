package ca.sheridancollege.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import ca.sheridancollege.beans.Bike;
import ca.sheridancollege.enums.BikeState;
import ca.sheridancollege.enums.LockState;
import ca.sheridancollege.utils.HibernateUtil;

public class BikeDAO {

	public int addBike(Bike bike) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		// using try-catch-finally so that no matter what happens, the session will be closed at the end
		try {
			Bike existingBike = getBikeByName(bike.getName());
			String errorMessage = "";
			
			// verify if there is a bike with this name already in the DB
			if (existingBike == null) {
				if (bike.getState() == null) {
					bike.setState(BikeState.AVAILABLE);
				}
				session.save(bike);
			} else {
				errorMessage = "There is already a bike with name " + bike.getName() + " registered (Bike ID: " + existingBike.getId() + ")";
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
		
		return bike.getId();
	}

	public void editBike(Bike bike) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		// using try-catch-finally so that no matter what happens, the session will be closed at the end
		try {
			Bike existingBike = getBikeByName(bike.getName());
			String errorMessage = "";
			
			// verify if there is another bike (one with a different ID) with the new name already in the DB
			if (existingBike == null || existingBike.getId() == bike.getId()) {
				session.update(bike);
			} else {
				errorMessage = "There is already a bike with name " + bike.getName() + " registered (Bike ID: " + existingBike.getId() + ")";
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

	public Bike getBikeById(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Bike.byID");
		query.setParameter("id", id);

		List<Bike> bikes = (List<Bike>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!bikes.isEmpty()) {
			return bikes.get(0);
		}

		return null;
	}
	
	public Bike getBikeByName(String name) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Bike.byName");
		query.setParameter("name", name);

		List<Bike> bikes = (List<Bike>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!bikes.isEmpty()) {
			return bikes.get(0);
		}

		return null;
	}

	public List<Bike> getAllBikes() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Bike.all");

		List<Bike> bikes = (List<Bike>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return bikes;
	}
}

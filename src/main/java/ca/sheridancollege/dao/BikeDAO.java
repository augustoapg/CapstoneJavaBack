package ca.sheridancollege.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ca.sheridancollege.beans.Bike;
import ca.sheridancollege.beans.RentalComponent;

public class BikeDAO {
	SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
	RentalComponentDAO rentalComponentDAO = new RentalComponentDAO();

	public void addBike(Bike bike) {

		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		bike.setId(rentalComponentDAO.getNewRentalComponentId("B"));
		session.save(bike);

		session.getTransaction().commit();
		session.close();
	}

	public void editBike(Bike bike) {

		Session session = sessionFactory.openSession();
		session.beginTransaction();

		session.update(bike);

		session.getTransaction().commit();
		session.close();
	}

	public Bike getBikeById(int id) {
		Session session = sessionFactory.openSession();
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

	public List<Bike> getAllBikes() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Bike.all");

		List<Bike> bikes = (List<Bike>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return bikes;
	}
}

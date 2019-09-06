package ca.sheridancollege.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ca.sheridancollege.beans.RentalComponent;

public class RentalComponentDAO {
	
	SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
	
	public String getNewRentalComponentId(String prefix) {
		prefix = prefix.toUpperCase(); // ensure uppercase prefix (B, L, K)
		String newComponentId = prefix + "000"; // default value
		
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Query query = session.getNamedQuery("RentalComponent.getAllComponentsByPrefix");
		query.setParameter("id", prefix + "%");
		
		List<String> rentalComponentsIds = (List<String>)query.getResultList();

		session.getTransaction().commit();
		session.close();
		
		// if this type of component was added before
		if(!rentalComponentsIds.isEmpty()) {
			String lastIdAdded = rentalComponentsIds.get(rentalComponentsIds.size() - 1);
			int numberOfLastComponentAdded = Integer.parseInt(lastIdAdded.substring(1));
			newComponentId = prefix + String.format("%03d", (numberOfLastComponentAdded + 1));
		}
		
		return newComponentId;
	}
	
	public RentalComponent getRentalComponent(String id) throws IllegalArgumentException {
	
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Query query = null;
		
		switch (id.charAt(0)) {
		case 'B':
			query = session.getNamedQuery("Bike.byID");
			query.setParameter("id", id);
			break;
		case 'L':
			query = session.getNamedQuery("LockItem.byID");
			query.setParameter("id", id);
			break;
		case 'K':
			query = session.getNamedQuery("KeyItem.byID");
			query.setParameter("id", id);
			break;

		default:
			throw new IllegalArgumentException("Rental Component " + id + " is not valid");
		}
		
		List<RentalComponent> rentalComponents = (List<RentalComponent>)query.getResultList();
		
		session.getTransaction().commit();
		session.close();
		
		if(!rentalComponents.isEmpty()) {
			return rentalComponents.get(0);
		}
		return null;
	}
}

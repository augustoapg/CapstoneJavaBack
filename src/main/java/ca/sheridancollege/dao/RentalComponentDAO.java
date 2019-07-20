package ca.sheridancollege.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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

}

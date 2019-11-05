package ca.sheridancollege.dao;



import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import ca.sheridancollege.beans.Waiver;
import ca.sheridancollege.utils.HibernateUtil;

public class WaiverDAO {
	
	public void addWaiver(Waiver waiver) throws Exception {
		ZonedDateTime today = ZonedDateTime.now(ZoneId.of("America/Toronto"));
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		try {
			// delete waiver in DB before adding new one
			Waiver existingWaiver = getWaiver();
			if (existingWaiver != null) {
				deleteWaiver(existingWaiver);
			}
			waiver.setCreatedOn(today.toLocalDate());
			session.save(waiver);
		} catch (Exception e) {
			throw e;
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	}
	
	public Waiver getWaiver() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Query query = session.getNamedQuery("Waiver.all");
		List<Waiver> waivers = (List<Waiver>) query.getResultList();

		session.getTransaction().commit();
		session.close();
		
		if (!waivers.isEmpty()) {
			return waivers.get(0);
		}
		
		return null;
	}

	public void deleteWaiver(Waiver waiver) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		session.delete(waiver);
		
		session.getTransaction().commit();
		session.close();
	}
}

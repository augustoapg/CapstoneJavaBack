package ca.sheridancollege.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Creates a singleton to create and return an unique SessionFactory object
 * throughout the entire application
 */
public class HibernateUtil {
	
	private static SessionFactory sessionFactory;
	
	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();			
		}
		
		return sessionFactory;
	}
}

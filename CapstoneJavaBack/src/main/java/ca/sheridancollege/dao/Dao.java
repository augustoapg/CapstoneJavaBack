package ca.sheridancollege.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Dao {

	SessionFactory sessionFactory = new Configuration()
			.configure("hibernate.cfg.xml")
			.buildSessionFactory();
	
	
}

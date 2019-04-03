package ca.sheridancollege.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SystemUser {
	
	SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

	// ------------------------------ SystemUser -----------------------------------

	//		public void addSysUser(Customer customer) {
	//			Session session = sessionFactory.openSession();
	//			session.beginTransaction();
		//
	//			SystemUser
	//			session.save(c);
		//
	//			session.getTransaction().commit();
	//			session.close();
	//		}

		// TODO: getSysUser

}

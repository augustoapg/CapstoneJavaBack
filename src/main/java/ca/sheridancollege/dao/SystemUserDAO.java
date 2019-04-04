package ca.sheridancollege.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ca.sheridancollege.beans.Customer;
import ca.sheridancollege.beans.SystemUser;

public class SystemUserDAO {
	
	SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
	
	

	// ------------------------------ SystemUser -----------------------------------

	public void addSysUser(SystemUser systemUser) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		session.save(systemUser);

		session.getTransaction().commit();
		session.close();
	}
	
	public SystemUser getSysUser(String username) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("SystemUser.byUsername");
		query.setParameter("username", username);

		List<SystemUser> sysUsers = (List<SystemUser>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!sysUsers.isEmpty()) {
			return sysUsers.get(0);
		}

		return null;
	}
}

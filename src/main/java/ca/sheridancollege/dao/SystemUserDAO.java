package ca.sheridancollege.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import ca.sheridancollege.beans.SystemUser;
import ca.sheridancollege.utils.HibernateUtil;

public class SystemUserDAO {
	// ------------------------------ SystemUser -----------------------------------

	public void addSysUser(SystemUser systemUser) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		session.save(systemUser);

		session.getTransaction().commit();
		session.close();
	}
	
	public SystemUser getSysUser(String username) {
		Session session = HibernateUtil.getSessionFactory().openSession();
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

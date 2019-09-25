package ca.sheridancollege.dao;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ca.sheridancollege.beans.Bike;
import ca.sheridancollege.beans.LockItem;
import ca.sheridancollege.beans.Rental;
import ca.sheridancollege.beans.RentalComponent;
import ca.sheridancollege.enums.BikeState;
import ca.sheridancollege.enums.LockState;

public class RentalDAO {
	SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
	
	BikeDAO bikeDAO = new BikeDAO();
	LockDAO keyLockDAO = new LockDAO();

	public void addRental(Rental rental) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		List<RentalComponent> rentalComponents = rental.getRentalComponents();
		
		// update all rentalComponents status according to their type
		for (RentalComponent rentalComponent : rentalComponents) {
			int rentalComponentId = rentalComponent.getId();
					
			if(rentalComponent instanceof Bike) {
				Bike bike = (Bike)rentalComponent;
				System.out.println("updating bike status: " + rentalComponentId);
				bike.setState(BikeState.RENTED);
				bikeDAO.editBike(bike);
			} else if (rentalComponent instanceof LockItem) {
				LockItem lockItem = (LockItem)rentalComponent;
				System.out.println("updating lock status: " + rentalComponentId);
				lockItem.setState(LockState.RENTED);
				keyLockDAO.editLockItem(lockItem);
			}
		}
		// set updated list of components back to rental object
		rental.setRentalComponents(rentalComponents);
		session.save(rental);

		session.getTransaction().commit();
		session.close();
	}

	public void deleteRental(int id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Rental r = (Rental) session.get(Rental.class, id);

		session.delete(r);

		session.getTransaction().commit();
		session.close();
	}


	public Rental getRental(int id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Rental.byID");
		query.setParameter("id", id);
		List<Rental> rentals = (List<Rental>) query.getResultList();

		System.out.println(rentals);
		session.getTransaction().commit();
		session.close();

		if (!rentals.isEmpty()) {
			return rentals.get(0);
		}

		return null;
	}

	public List<Rental> getAllRentals() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Rental.all");

		List<Rental> rentals = (List<Rental>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return rentals;
	}
	
	public Rental getActiveRental(int id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Rental.active");
		query.setParameter("id", id);
		List<Rental> rentals = (List<Rental>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!rentals.isEmpty()) {
			return rentals.get(0);
		}

		return null;
	}

	public List<Rental> getActiveRentals() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Rental.allActive");
		List<Rental> rentals = (List<Rental>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return rentals;
	}
	
	public Rental getArchiveRental(int id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Rental.archive");
		query.setParameter("id", id);
		List<Rental> rentals = (List<Rental>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!rentals.isEmpty()) {
			return rentals.get(0);
		}

		return null;
	}

	public List<Rental> getArchiveRentals() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Rental.allArchive");
		List<Rental> rentals = (List<Rental>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return rentals;
	}

	public void returnRental(Rental rental, Rental newRental) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		rental.setComment(newRental.getComment());
		rental.setReturnedDate(LocalDate.now());
		
		List<RentalComponent> rentalComponents = rental.getRentalComponents();
		
		// update all rentalComponents status according to their type
		// NOTE: Only update component status to available if current status is RENTED. This
		// is done to prevent overwriting another states such as MISSING, IN_MAINTENANCE, etc
		for (RentalComponent rentalComponent : rentalComponents) {
					
			if (rentalComponent instanceof Bike) {
				Bike bike = (Bike)rentalComponent;
				if(bike.getState() == BikeState.RENTED) {
					bike.setState(BikeState.AVAILABLE);
					bikeDAO.editBike(bike);
				}
			} else if (rentalComponent instanceof LockItem) {
				LockItem lockItem = (LockItem)rentalComponent;
				if(lockItem.getState() == LockState.RENTED) {
					lockItem.setState(LockState.AVAILABLE);
					keyLockDAO.editLockItem(lockItem);						
				}
			}
		}
		
		session.update(rental);

		session.getTransaction().commit();
		session.close();
	}

	public void editRental(Rental newRental) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Rental rental = session.get(Rental.class, newRental.getId());
		rental.setDueDate(newRental.getDueDate());
		rental.setComment(newRental.getComment());
		
		session.getTransaction().commit();
		session.close();
	}
}












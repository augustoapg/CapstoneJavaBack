package ca.sheridancollege.dao;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.sheridancollege.beans.Bike;
import ca.sheridancollege.beans.KeyItem;
import ca.sheridancollege.beans.LockItem;
import ca.sheridancollege.beans.Rental;
import ca.sheridancollege.beans.RentalComponent;
import ca.sheridancollege.enums.BikeState;
import ca.sheridancollege.enums.KeyState;
import ca.sheridancollege.enums.LockState;

public class RentalDAO {
	SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
	
	BikeDAO bikeDAO = new BikeDAO();
	KeyLockDAO keyLockDAO = new KeyLockDAO();

	public void addRental(Rental rental) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		List<RentalComponent> rentalComponents = rental.getRentalComponents();
		
		// update all rentalComponents status according to their type
		for (RentalComponent rentalComponent : rentalComponents) {
			String rentalComponentId = rentalComponent.getId();
					
			switch (rentalComponentId.charAt(0)) {
				case 'B':
					Bike bike = (Bike)rentalComponent;
					System.out.println("updating bike status: " + bike.getId());
					bike.setBikeState(BikeState.RENTED);
					bikeDAO.editBike(bike);
					break;
				case 'L':
					LockItem lockItem = (LockItem)rentalComponent;
					System.out.println("updating lock status: " + lockItem.getId());
					lockItem.setLockState(LockState.RENTED);
					keyLockDAO.editLockItem(lockItem);
					break;
				case 'K':
					KeyItem keyItem = (KeyItem)rentalComponent;
					System.out.println("updating key status: " + keyItem.getId());
					keyItem.setKeyState(KeyState.RENTED);
					keyLockDAO.editKeyItem(keyItem);
					break;
				default:
					break;
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
			String rentalComponentId = rentalComponent.getId();
					
			switch (rentalComponentId.charAt(0)) {
				case 'B':
					Bike bike = (Bike)rentalComponent;
					if(bike.getBikeState() == BikeState.RENTED) {
						bike.setBikeState(BikeState.AVAILABLE);
						bikeDAO.editBike(bike);
					}
					break;
				case 'L':
					LockItem lockItem = (LockItem)rentalComponent;
					if(lockItem.getLockState() == LockState.RENTED) {
						lockItem.setLockState(LockState.AVAILABLE);
						keyLockDAO.editLockItem(lockItem);						
					}
					break;
				case 'K':
					KeyItem keyItem = (KeyItem)rentalComponent;
					if(keyItem.getKeyState() == KeyState.RENTED) {
						keyItem.setKeyState(KeyState.AVAILABLE);
						keyLockDAO.editKeyItem(keyItem);
					}
					break;
				default:
					break;
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












package ca.sheridancollege.dao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import ca.sheridancollege.beans.Bike;
import ca.sheridancollege.beans.LockItem;
import ca.sheridancollege.beans.Rental;
import ca.sheridancollege.beans.RentalComponent;
import ca.sheridancollege.enums.BikeState;
import ca.sheridancollege.enums.LockState;
import ca.sheridancollege.utils.HibernateUtil;

public class RentalDAO {
	BikeDAO bikeDAO = new BikeDAO();
	LockDAO keyLockDAO = new LockDAO();

	public void addRental(Rental rental) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		try {
			List<RentalComponent> rentalComponents = rental.getRentalComponents();
			
			// update all rentalComponents status according to their type
			for (RentalComponent rentalComponent : rentalComponents) {
				if(rentalComponent instanceof Bike) {
					Bike bike = (Bike)rentalComponent;
					bike.setState(BikeState.RENTED);
					bikeDAO.editBike(bike);
				} else if (rentalComponent instanceof LockItem) {
					LockItem lockItem = (LockItem)rentalComponent;
					lockItem.setState(LockState.RENTED);
					keyLockDAO.editLockItem(lockItem);
				}
			}
			
			// set updated list of components back to rental object
			rental.setRentalComponents(rentalComponents);
			session.save(rental);
		} catch (Exception e) {
			throw e;
		} finally {
			// closes connection even in case of an error
			session.getTransaction().commit();
			session.close();			
		}
	}

	public void deleteRental(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Rental r = (Rental) session.get(Rental.class, id);

		session.delete(r);

		session.getTransaction().commit();
		session.close();
	}


	public Rental getRental(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
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
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Rental.all");

		List<Rental> rentals = (List<Rental>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return rentals;
	}
	
	public Rental getActiveRental(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
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

	public Rental getActiveRentalByCustID(int sheridanId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Rental.activeByCust");
		query.setParameter("id", sheridanId);
		List<Rental> rentals = (List<Rental>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!rentals.isEmpty()) {
			return rentals.get(0);
		}

		return null;
	}


	public List<Rental> getActiveRentals() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Rental.allActive");
		List<Rental> rentals = (List<Rental>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return rentals;
	}
	
	public List<Rental> getLateRentals() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Rental.allLate");
		query.setParameter("today", LocalDate.now());
		List<Rental> rentals = (List<Rental>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return rentals;
	}
	
	public Rental getArchivedRental(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Rental.archived");
		query.setParameter("id", id);
		List<Rental> rentals = (List<Rental>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!rentals.isEmpty()) {
			return rentals.get(0);
		}

		return null;
	}

	public List<Rental> getArchivedRentalsByCustID(int sheridanId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Rental.archivedByCust");
		query.setParameter("id", sheridanId);
		List<Rental> rentals = (List<Rental>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		if (!rentals.isEmpty()) {
			return rentals;
		}

		return null;
	}
	
	public List<Rental> getArchivedRentals() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("Rental.allArchived");
		List<Rental> rentals = (List<Rental>) query.getResultList();

		session.getTransaction().commit();
		session.close();

		return rentals;
	}

	public void returnRental(Rental rental, Rental newRental) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		try {
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
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.getTransaction().commit();
			session.close();			
		}
	}

	public void editRental(Rental newRental) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		try {
			Rental rental = session.get(Rental.class, newRental.getId());
			rental.setDueDate(newRental.getDueDate());
			rental.setComment(newRental.getComment());			
		} catch (Exception e) {
			throw e;
		} finally {
			session.getTransaction().commit();
			session.close();			
		}
		
		
	}
	
	public List<Rental> getRentalsByDate(String type, String stDate, String enDate) throws Exception{

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		List<Rental> rentals = new ArrayList<Rental>();
		
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			//convert String to LocalDate
			LocalDate frmDate = LocalDate.parse(stDate, formatter);
			LocalDate endDate = LocalDate.parse(enDate, formatter);
			
			if (frmDate.isAfter(endDate)) {
				throw new IllegalArgumentException("Start date must be before the End date");
			}
			
			String namedQuery = getNamedQueryRentals(type);
			
			Query query = session.getNamedQuery(namedQuery);
			
			query.setParameter("stDate", frmDate);
			query.setParameter("edDate", endDate);
			
			rentals.addAll(query.getResultList());
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.getTransaction().commit();
			session.close();			
		}
		
		if(rentals.isEmpty()) {
			System.out.println("EMPTY 437");
			return null;
		}
		
		return rentals;
	}
	
	
	public String getNamedQueryRentals(String type) {
		String namedQuery = "";
		
		switch (type.toLowerCase()) {
		
			// Archive Rentals By Due Date
			case "archiveduedate":
				namedQuery = "Rental.ArchiveByDueDate";
				break;
			
			// Archive Rentals By Signout Date
			case "archivesignout":
				namedQuery = "Rental.ArchiveBySignOutDate";
				break;
			
			// Archive Rentals By Return Date
			case "archivereturn":
				namedQuery = "Rental.ArchiveByReturnDate";
				break;
				
			// Active Rentals By Due Date
			case "activeduedate":
				namedQuery = "Rental.ActiveByDueDate";
				break;
				
			// Active Rentals By Signout Date
			case "activesignout":
				namedQuery = "Rental.ActiveBySignOutDate";
				break;
				
			case "allsignout":
				namedQuery = "Rental.betweenSignOutDate";
			default:	
		}
		
		return namedQuery;
		
	}
	
}












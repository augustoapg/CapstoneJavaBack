package ca.sheridancollege.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity

@NamedQuery(name="Rental.byID", query="from Rental where id=:id")
@NamedQuery(name="Rental.all", query="from Rental")
@NamedQuery(name="Rental.active", query="from Rental where returnedDate is null and id=:id")
@NamedQuery(name="Rental.allActive", query="from Rental where returnedDate is null")
@NamedQuery(name="Rental.activeByCust", query="from Rental WHERE " +
		"returnedDate is null and customer_id=:id")

@NamedQuery(name="Rental.archived", query="from Rental where returnedDate is not null and id=:id")
@NamedQuery(name="Rental.allArchived", query="from Rental where returnedDate is not null")
@NamedQuery(name="Rental.archivedByCust", query="from Rental WHERE " +
		"returnedDate is not null and customer_id=:id")

@NamedQuery(name="Rental.ArchiveByDueDate", query="from Rental AS r WHERE (r.dueDate BETWEEN :stDate AND :edDate) "
														+ "AND (returnedDate is not null)")
@NamedQuery(name="Rental.ArchiveBySignOutDate", query="from Rental AS r WHERE (r.signOutDate BETWEEN :stDate AND :edDate) "
														+ "AND (returnedDate is not null)")
@NamedQuery(name="Rental.ArchiveByReturnDate", query="from Rental AS r WHERE (r.returnedDate BETWEEN :stDate AND :edDate) "
														+ "AND (returnedDate is not null)")

@NamedQuery(name="Rental.ActiveByDueDate", query="from Rental AS r WHERE r.dueDate BETWEEN :stDate AND :edDate "
														+ "AND (returnedDate is null)")
@NamedQuery(name="Rental.ActiveBySignOutDate", query="from Rental AS r WHERE (r.signOutDate BETWEEN :stDate AND :edDate) "
														+ "AND (returnedDate is null)")
@NamedQuery(name="Rental.allLate", query="from Rental where returnedDate is null AND dueDate < :today")
@NamedQuery(name="Rental.betweenSignOutDate", query="from Rental where signOutDate BETWEEN :stDate AND :edDate")

public class Rental implements Serializable {
	
	@Id
	@GeneratedValue
	private int id;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate signOutDate;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate dueDate;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate returnedDate;
	@ManyToOne
	@JoinColumn(name="CUSTOMER_ID")
	private Customer customer;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	@Fetch(FetchMode.JOIN)
	private List<RentalComponent> rentalComponents;
	private String comment;
	
	
	public Rental(LocalDate signOutDate, LocalDate dueDate, LocalDate returnedDate, Customer customer, List<RentalComponent> rentalComponents, String comment) {
		this.signOutDate = signOutDate;
		this.dueDate = dueDate;
		this.returnedDate = returnedDate;
		this.customer = customer;
		this.rentalComponents = rentalComponents;
		this.comment = comment;
	}
	
	public String getRentalState() {
		// all rentals should have signOutDate and dueDate
		if(this.signOutDate != null && this.dueDate != null) {
			if(this.returnedDate == null) {
				ZonedDateTime today = ZonedDateTime.now(ZoneId.of("America/Toronto"));
				return (this.dueDate.isAfter(today.toLocalDate())) ? "Active" : "Late";
			} else {
				return (this.returnedDate.isBefore(this.dueDate)) ? "Returned" : "Returned Late";
			}
		}
		return "Invalid";
	}
}

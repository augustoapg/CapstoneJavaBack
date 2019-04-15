package ca.sheridancollege.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import ca.sheridancollege.enums.RentalState;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity

@NamedQuery(name="Rental.byID", query="from Rental where id=:id")
@NamedQuery(name="Rental.all", query="from Rental")
@NamedQuery(name="Rental.active", query="from Rental where returnedDate is null")
@NamedQuery(name="Rental.archive", query="from Rental where returnedDate is not null")
@NamedQuery(name="Rental.update", query="UPDATE Rental SET comment =: comment, "
		+ "returnedDate =: returnedDate WHERE id =: id")
public class Rental implements Serializable {
	
	@Id
	@GeneratedValue
	private int id;
	private Date signOutDate;
	private Date dueDate;
	private Date returnedDate;
	@ManyToOne
	@JoinColumn(name="CUSTOMER_ID")
	private Customer customer;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Bike bike;
	private String comment;
	
	
	// @ManyToOne Refer back to
	// https://en.wikibooks.org/wiki/Java_Persistence/ManyToOne
	
	
	public Rental(Date signOutDate, Date dueDate, Date returnedDate, Customer customer, Bike bike, String comment) {
		this.signOutDate = signOutDate;
		this.dueDate = dueDate;
		this.returnedDate = returnedDate;
		this.customer = customer;
		this.bike = bike;
		this.comment = comment;
	}
	
	public String getRentalState() {
		// all rentals should have signOutDate and dueDate
		if(this.signOutDate != null && this.dueDate != null) {
			if(this.returnedDate == null) {
				return (this.dueDate.after(new Date())) ? "Active" : "Late";
			} else {
				return (this.returnedDate.before(this.dueDate)) ? "Returned" : "Returned Late";
			}
		}
		return "Invalid";
	}
}

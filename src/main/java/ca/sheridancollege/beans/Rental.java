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
public class Rental implements Serializable {
	
	@Id
	@GeneratedValue
	private int id;
	private Date signOutDate;
	private Date dueDate;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CUSTOMER_ID")
	private Customer customer;
	private RentalState state;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Bike bike;
	
	
	// @ManyToOne Refer back to
	// https://en.wikibooks.org/wiki/Java_Persistence/ManyToOne
	
	
	public Rental(Date signOutDate, Date dueDate, Customer customer, RentalState state, Bike bike) {
		this.signOutDate = signOutDate;
		this.dueDate = dueDate;
		this.customer = customer;
		this.state = state;
		this.bike = bike;
	}
	
	
}

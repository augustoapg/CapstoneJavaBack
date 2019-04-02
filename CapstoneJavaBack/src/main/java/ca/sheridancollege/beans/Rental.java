package ca.sheridancollege.beans;

import java.io.Serializable;
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
	private Date dueDate;
	private Customer renter;
	private RentalState state;
	private List<RentalComponent> rentalItem;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Bike bike;
	
	
	// @ManyToOne Refer back to
	// https://en.wikibooks.org/wiki/Java_Persistence/ManyToOne
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CUSTOMER_ID")
	private Customer customer;
	
	public Rental(Date dueDate, Customer renter, RentalState state, List<RentalComponent> rentalItem) {
		this.dueDate = dueDate;
		this.renter = renter;
		this.state = state;
		this.rentalItem = rentalItem;
	}
	
	
}

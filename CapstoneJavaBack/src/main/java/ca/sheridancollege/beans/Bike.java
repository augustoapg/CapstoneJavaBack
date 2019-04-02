package ca.sheridancollege.beans;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import lombok.*;

@Data
@Entity

@NamedQuery(name="Bike.byID", query="from Bike where id=:id")
@NamedQuery(name="Bike.all", query="from Bike")
public class Bike extends RentalComponent {
	
	private Date signOutDate;
	private boolean isRepairNeeded;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Rental rental;

	public Bike(String notes, boolean isRepairNeeded, Date signOutDate) {
		super(notes);
		this.isRepairNeeded = isRepairNeeded;
		this.signOutDate = signOutDate;
	}

	public String getNotes() {
		return super.getNotes();
	}
}

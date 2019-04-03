package ca.sheridancollege.beans;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import ca.sheridancollege.enums.RentalState;
import lombok.*;


@NoArgsConstructor
@Data
@Entity

@NamedQuery(name="Bike.byID", query="from Bike where id=:id")
@NamedQuery(name="Bike.all", query="from Bike")
public class Bike extends RentalComponent {
	
	private Date signOutDate;
	private boolean isRepairNeeded;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Rental rental;
	
	private String imgPath;

	public Bike(String notes, Date signOutDate, boolean isRepairNeeded, String imgPath) {
		super(notes);
		this.isRepairNeeded = isRepairNeeded;
		this.signOutDate = signOutDate;
		this.imgPath = imgPath;
	}

}

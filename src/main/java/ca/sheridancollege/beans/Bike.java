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
public class Bike {
	
	@Id
	@GeneratedValue
	private int id;
	private String notes;
	private boolean isRepairNeeded;
	private boolean available;
	private String imgPath;

	public Bike(String notes, boolean isRepairNeeded, boolean available, String imgPath) {
		this.notes = notes;
		this.isRepairNeeded = isRepairNeeded;
		this.available = available;
		this.imgPath = imgPath;
	}

}

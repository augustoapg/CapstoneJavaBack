package ca.sheridancollege.beans;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import ca.sheridancollege.enums.BikeState;
import ca.sheridancollege.enums.RentalState;
import lombok.*;


@NoArgsConstructor
@Data
@Entity

@NamedQuery(name="Bike.byID", query="from Bike where id=:id")
@NamedQuery(name="Bike.all", query="from Bike")
public class Bike extends RentalComponent {
	
	private String notes;
	private BikeState bikeState;
	private String imgPath;
	private String manufacturer;
	private String productCode;
	private int serialNumber;
	
	public Bike(String notes, BikeState bikeState, String imgPath, String manufacturer,
			String productCode, int serialNumber) {
		this.notes = notes;
		this.bikeState = bikeState;
		this.imgPath = imgPath;
		this.manufacturer = manufacturer;
		this.productCode = productCode;
		this.serialNumber = serialNumber;
	}
}

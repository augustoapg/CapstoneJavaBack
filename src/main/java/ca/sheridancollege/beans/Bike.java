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
@AllArgsConstructor
@Data
@Entity

@NamedQuery(name="Bike.byID", query="from Bike where id=:id")
@NamedQuery(name="Bike.all", query="from Bike")
public class Bike extends RentalComponent {
	
	private String notes;
	private BikeState state;
	private String imgPath;
	private String manufacturer;
	private String productCode;
	private String serialNumber;
	private String model;
}

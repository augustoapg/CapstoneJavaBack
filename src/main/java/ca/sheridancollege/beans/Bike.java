package ca.sheridancollege.beans;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import ca.sheridancollege.enums.BikeState;
import lombok.*;


@NoArgsConstructor
@Data
@Entity
@NamedQuery(name="Bike.byID", query="from Bike where id=:id")
@NamedQuery(name="Bike.all", query="from Bike")
public class Bike extends RentalComponent {
	
	private BikeState state;
	private String imgPath;
	private String manufacturer;
	private String productCode;
	private String serialNumber;
	private String model;
	
	public Bike(String name, String notes, BikeState state, String imgPath, String manufacturer, String productCode,
			String serialNumber, String model) {
		super(name, notes);
		this.state = state;
		this.imgPath = imgPath;
		this.manufacturer = manufacturer;
		this.productCode = productCode;
		this.serialNumber = serialNumber;
		this.model = model;
	}
}

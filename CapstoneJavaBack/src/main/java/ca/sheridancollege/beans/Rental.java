package ca.sheridancollege.beans;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import ca.sheridancollege.enums.RentalState;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
public class Rental {
	@Id
	@GeneratedValue
	private int rentalId;
	private Date dueDate;
	private Customer renter;
	private RentalState state;
	private List<RentalComponent> rentalItem;
}

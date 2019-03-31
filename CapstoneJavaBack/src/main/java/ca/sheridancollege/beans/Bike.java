package ca.sheridancollege.beans;

import javax.persistence.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
public class Bike extends RentalComponent {
	private boolean isRepairNeeded;

	public Bike(boolean isRepairNeeded) {
		this.isRepairNeeded = isRepairNeeded;
	}
}

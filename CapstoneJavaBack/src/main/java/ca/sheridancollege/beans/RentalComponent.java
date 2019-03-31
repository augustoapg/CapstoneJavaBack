package ca.sheridancollege.beans;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class RentalComponent {
	@Id
	@GeneratedValue
	private int id;
	private String notes;
}

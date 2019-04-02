package ca.sheridancollege.beans;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity

@NamedQuery(name="KeyLock.byID", query="from KeyLock where id=:id")
@NamedQuery(name="KeyLock.all", query="from KeyLock")
public class KeyLock extends RentalComponent {
	
	private Date signOutDate;

	public KeyLock(String notes, Date signOutDate) {
		super(notes);
		this.signOutDate = signOutDate;
	}

}

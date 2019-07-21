package ca.sheridancollege.beans;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import ca.sheridancollege.enums.KeyState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class KeyItem extends RentalComponent implements Serializable {
	
	private KeyState keyState;
}

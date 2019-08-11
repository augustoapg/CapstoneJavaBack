package ca.sheridancollege.beans;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import ca.sheridancollege.enums.KeyState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@NamedQuery(name="KeyItem.all", query="from KeyItem")
public class KeyItem extends RentalComponent implements Serializable {
	
	private KeyState keyState;
}

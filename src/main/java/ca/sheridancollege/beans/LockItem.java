package ca.sheridancollege.beans;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import ca.sheridancollege.enums.LockState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@NamedQuery(name="LockItem.all", query="from LockItem")
public class LockItem extends RentalComponent implements Serializable {

	@OneToOne(cascade = CascadeType.ALL)
	private KeyItem keyItem;
	private LockState lockState;
}

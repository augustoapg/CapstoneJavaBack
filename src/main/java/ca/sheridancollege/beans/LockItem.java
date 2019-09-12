package ca.sheridancollege.beans;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.sheridancollege.enums.KeyState;
import ca.sheridancollege.enums.LockState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "lockitem")
@NamedQuery(name="LockItem.all", query="SELECT l FROM LockItem l JOIN FETCH l.keyItems")
@NamedQuery(name="LockItem.byID", query="SELECT l FROM LockItem l JOIN FETCH l.keyItems WHERE l.id = :id")
//@NamedQuery(name="LockItem.keysByID", query=
//		"SELECT l FROM LockItem l JOIN FETCH l.keyItems WHERE k.id = :id")

public class LockItem extends RentalComponent implements Serializable {

	//@JsonIgnore
	@OneToMany(mappedBy="lockItem",cascade = CascadeType.ALL)
	@Fetch(FetchMode.JOIN)
	private List<KeyItems> keyItems;

	private LockState lockState;

	public boolean allKeysAreMissing() {
		for (KeyItems keyItem : keyItems) {
			if (keyItem.getKeyState() != KeyState.MISSING) {
				return false;
			}
		}
		return true;
	}
	
	public KeyItems getKeyItemByID(String keyId) {
		for (KeyItems keyItem : keyItems) {
			if (keyItem.getId() == keyId) {
				return keyItem;
			}
		}
		return null;
	}
}

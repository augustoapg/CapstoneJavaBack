package ca.sheridancollege.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ca.sheridancollege.enums.LockState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@NamedQuery(name="LockItem.all", query="from LockItem")
@NamedQuery(name="LockItem.byID", query="from LockItem where id=:id")
@NamedQuery(name="LockItem.byName", query="from LockItem where name=:name")
public class LockItem extends RentalComponent implements Serializable {
	private int keyNum;
	private LockState state;
	
	public LockItem(String name, String notes, int keyNum, LockState state) {
		super(name, notes);
		this.keyNum = keyNum;
		this.state = state;
	}
}

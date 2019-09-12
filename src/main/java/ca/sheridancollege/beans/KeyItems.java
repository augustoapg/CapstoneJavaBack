package ca.sheridancollege.beans;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.sheridancollege.enums.KeyState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
//@NamedQuery(name="KeyItem.all", query="SELECT l FROM LockItem l JOIN FETCH l.keyItems")
//@NamedQuery(name="KeyItem.byID", query="from KeyItems where id=:id")
//@NamedQuery(name="KeyItem.lockByID", query=
//		"FROM KeyItems k JOIN FETCH k.lockItem lockItem"
//		+ " where k.id=:id")
public class KeyItems extends RentalComponent implements Serializable {
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lockItem_Id")
    private LockItem lockItem;
	private String lockID;
	private KeyState keyState;
	
	public KeyItems(KeyState keyState) {
		this.keyState = keyState;
	}

	public LockItem getLockItem() {
		return lockItem;
	}
	
	public String getLockID() {
		return this.lockID;
	}

	public void setLockItem(LockItem lock) {
		this.lockItem = lock;
		if (lock != null) {
			this.lockID = lock.getId();
		} else {
			this.lockID = null;
		}
		
	}

	public KeyState getKeyState() {
		return keyState;
	}

	public void setKeyState(KeyState keyState) {
		this.keyState = keyState;
	}
	
	
	
}

package ca.sheridancollege.beans;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import ca.sheridancollege.enums.LockState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class LockItem implements Serializable {
	@Id
	private String id;
	@OneToOne(cascade = CascadeType.ALL)
	private KeyItem keyItem;
	private LockState lockState;
}

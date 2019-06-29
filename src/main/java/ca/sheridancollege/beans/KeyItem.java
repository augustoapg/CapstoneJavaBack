package ca.sheridancollege.beans;

import java.io.Serializable;

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
public class KeyItem implements Serializable {
	@Id
	private String id;
	private KeyState keyState;
}

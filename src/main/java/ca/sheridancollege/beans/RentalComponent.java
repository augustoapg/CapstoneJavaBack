package ca.sheridancollege.beans;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQuery;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NamedQuery(name="RentalComponent.getAllComponentsByPrefix", query="SELECT id FROM RentalComponent WHERE id like :id")
public abstract class RentalComponent {

	@Id
	private String id;
}

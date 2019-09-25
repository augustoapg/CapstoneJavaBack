package ca.sheridancollege.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQuery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NamedQuery(name="RentalComponent.getAllComponentsByPrefix", query="SELECT id FROM RentalComponent WHERE id like :id")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Bike.class, name = "Bike"),
    @JsonSubTypes.Type(value = LockItem.class, name = "LockItem")}
)
public abstract class RentalComponent {

	@Id
	@GeneratedValue
	private int id;
	private String name;
	private String notes;
	
	// constructor used by child classes
	public RentalComponent(String name, String notes) {
		this.name = name;
		this.notes = notes;
	}
}

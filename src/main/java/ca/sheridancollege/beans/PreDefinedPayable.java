package ca.sheridancollege.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@NamedQuery(name="PreDefPayable.byID", query="from PreDefinedPayable where id = :id;")
@NamedQuery(name="PreDefPayable.byCategory", query="from PreDefinedPayable where category = :category;")
@NamedQuery(name="PreDefPayable.all", query="from PreDefinedPayable;")
public class PreDefinedPayable {

	@Id
	@GeneratedValue
	private int id;
	private String category;
	private double value;
	
	public PreDefinedPayable(String category, double value) {
		this.category = category;
		this.value = value;
	}
}

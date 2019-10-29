package ca.sheridancollege.beans;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;

import ca.sheridancollege.enums.BasketState;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@NamedQuery(name="Basket.byID", query="from Basket where id=:id")
@NamedQuery(name="Basket.byName", query="from Basket where name=:name")
@NamedQuery(name="Basket.all", query="from Basket")
public class Basket extends RentalComponent {
	private BasketState state;

	public Basket(String name, String notes, BasketState state) {
		super(name, notes);
		this.state = state;
	}
}

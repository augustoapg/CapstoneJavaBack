package ca.sheridancollege.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity


@NamedQuery(name="Payable.byID", query="from Payable where id=:id")
@NamedQuery(name="Payable.all", query="from Payable")
@NamedQuery(name="Payable.byCustId", query="from Payable where rental.customer.sheridanId=:id")
@NamedQuery(name="Payable.byRentalId", query="from Payable where rental.id=:id")
public class Payable {
	
	@Id
	@GeneratedValue
	int payable_id;
	
	String category;
	double value;
	boolean isPaid;
	@ManyToOne
	@JoinColumn(name="RENTAL_ID")
	private Rental rental;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payable other = (Payable) obj;
		if (payable_id != other.payable_id)
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + payable_id;
		return result;
	}
}

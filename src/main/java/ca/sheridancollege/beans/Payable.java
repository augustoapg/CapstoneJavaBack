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
	
}

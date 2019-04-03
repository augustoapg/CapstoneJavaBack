package ca.sheridancollege.beans;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity

@NamedQuery(name="Customer.byID", query="from Customer where id=:id")
@NamedQuery(name="Customer.all", query="from Customer")
public class Customer implements Serializable {
	@Id
	@GeneratedValue
	private int id;
	
	private String firstName;
	private String lastName;
	private String address;
	private String sheridanEmail;
	private int sheridanId;
	private String phone;
	private boolean isBlackListed;
	private boolean willRecvEmail;
	private String notes;
	
	public Customer(String firstName, String lastName, String address, String sheridanEmail, int sheridanId, String phone,
			boolean isBlackListed, boolean willRecvEmail, String notes) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.sheridanEmail = sheridanEmail;
		this.sheridanId = sheridanId;
		this.phone = phone;
		this.isBlackListed = isBlackListed;
		this.willRecvEmail = willRecvEmail;
		this.notes = notes;
	}

	
}

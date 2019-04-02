package ca.sheridancollege.beans;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import lombok.Data;

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
	private String email;
	private int oneCardNum;
	private int phone;
	private boolean isBlackListed;
	private boolean willRecvEmail;
	private String notes;
	
	public Customer(String firstName, String lastName, String address, String email, int oneCardNum, int phone,
			boolean isBlackListed, boolean willRecvEmail, String notes) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.email = email;
		this.oneCardNum = oneCardNum;
		this.phone = phone;
		this.isBlackListed = isBlackListed;
		this.willRecvEmail = willRecvEmail;
		this.notes = notes;
	}
	
	public boolean getIsBlackListed() {
		return this.isBlackListed;
	}
	
	public boolean getWillRecvEmail() {
		return this.willRecvEmail;
	}
	
}

package ca.sheridancollege.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;


// VERY unsure about this 


import lombok.Data;

@Data
@Entity

@NamedQuery(name="SystemUser.byID", query="from SystemUser where id=:id")
@NamedQuery(name="SystemUser.all", query="from SystemUser")
public class SystemUser {
	
	@Id
	@GeneratedValue
	private int id;
	
	private String username;
	private String password;
	
	public SystemUser(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}	
	

}

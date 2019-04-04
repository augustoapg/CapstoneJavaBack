package ca.sheridancollege.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import lombok.AllArgsConstructor;

// VERY unsure about this 


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name="SystemUser.byUsername", query="from SystemUser where username=:username")
@NamedQuery(name="SystemUser.all", query="from SystemUser")
public class SystemUser {
	
	@Id	
	private String username;
	private String password;
}

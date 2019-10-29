package ca.sheridancollege.beans;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import com.fasterxml.jackson.annotation.JsonFormat;

import ca.sheridancollege.enums.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

@NamedQuery(name="Customer.byID", query="from Customer where sheridanId=:sheridanId")
@NamedQuery(name="Customer.all", query="from Customer")
public class Customer implements Serializable {
	@Id
	private int sheridanId;
	private String firstName;
	private String lastName;
	private String address;
	private String sheridanEmail;
	private String personalEmail;
	private Long phone;
	private CustomerType type;
	private boolean isBlackListed;
	private boolean willRecvEmail;
	private String notes;
	private String emergencyContactFirstName;
	private String emergencyContactLastName;
	private Long emergencyContactPhone;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate createdOn;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate endOfProgram;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate lastWaiverSignedAt;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate waiverExpirationDate;
}

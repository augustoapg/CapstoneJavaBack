package ca.sheridancollege.beans;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@NamedQuery(name="Waiver.all", query="from Waiver")
public class Waiver {
	@Id
	private LocalDate createdOn;
	@Column(length = 16382)
	private String waiver;
}

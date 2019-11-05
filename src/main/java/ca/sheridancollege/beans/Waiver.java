package ca.sheridancollege.beans;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate createdOn;
	@Column(columnDefinition="TEXT")
	private String waiver;
}

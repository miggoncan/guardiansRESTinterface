package es.us.alumn.miggoncan2.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
@Table(
	name = "doctor",
	uniqueConstraints = 
		@UniqueConstraint(columnNames = {"first_name", "last_names"})
)
public class Doctor {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@Column(name = "first_name", nullable = false)
	private String firstName;
	
	@Column(name = "last_names", nullable = false)
	private String lastNames;
	
	private String email;
	
	@OneToOne(optional = true, mappedBy = "doctor")
	@JsonManagedReference
	private Absence absence;
	
	
	public Doctor(String firsName, String lastNames, String email) {
		this.firstName = firsName;
		this.lastNames = lastNames;
		this.email = email;
	}
	
	public Doctor() {}
}

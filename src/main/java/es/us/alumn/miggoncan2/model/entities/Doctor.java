package es.us.alumn.miggoncan2.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

// TODO Add test for the constraints

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
	@NotBlank
	private String firstName;
	
	@Column(name = "last_names", nullable = false)
	@NotBlank
	private String lastNames;
	
	@Email
	@NotBlank
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
	
	@Override
	public String toString() {
		return Doctor.class.getSimpleName() 
				+ "("
					+ "id=" + this.id + ", "
					+ "firstName=" + this.firstName + ", "
					+ "lastNames=" + this.lastNames + ", "
					+ "email=" + this.email + ", "
					+ "absence=" + this.absence
				+ ")";
	}
}

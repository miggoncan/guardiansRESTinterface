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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import es.us.alumn.miggoncan2.model.entities.serializers.DoctorSerializer;
import lombok.Data;

/**
 * This {@link Entity} represents the information of a Doctor that will be
 * stored in the database
 * 
 * @author miggoncan
 */
@Data
@Entity
@Table(
	name = "doctor",
	uniqueConstraints = 
		@UniqueConstraint(columnNames = {"first_name", "last_names"})
)
@JsonSerialize(using = DoctorSerializer.class)
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

	public Doctor() {
	}

	// The toString method from @Data is not used as it can create an infinite loop
	// between Absence#toString and this method
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

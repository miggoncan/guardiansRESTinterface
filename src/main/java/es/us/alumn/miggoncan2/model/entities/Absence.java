package es.us.alumn.miggoncan2.model.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

import es.us.alumn.miggoncan2.model.entityvalidation.ValidAbsenceDates;
import lombok.Data;

/**
 * This Entity represents the Absence of a Doctor during a certain period
 * 
 * @author miggoncan
 *
 */
@Data
@Entity
@ValidAbsenceDates
public class Absence {
	/**
	 * doctor_id is the primary key of the Doctor with this Absence
	 */
	@Id
	private Long doctorId;
	@MapsId
	@OneToOne(optional = false)
	@JsonBackReference
	private Doctor doctor;
	
	@Column(nullable = false)
	@NotNull
	private Date start;
	
	@Column(nullable = false)
	@NotNull
	private Date end;
	
	
	public Absence(Date start, Date end) {
		this.start = start;
		this.end = end;
	}
	
	public Absence() {}
	
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
		if (doctor != null) {
			this.doctorId = doctor.getId();
		} else  {
			this.doctorId = null;
		}
	}
	
	@Override
	public String toString() {
		return Absence.class.getSimpleName() 
				+ "("
					+ "doctorId=" + this.doctorId + ", "
					+ "start=" + this.start + ", "
					+ "end=" + this.end
				+ ")";
	}
}

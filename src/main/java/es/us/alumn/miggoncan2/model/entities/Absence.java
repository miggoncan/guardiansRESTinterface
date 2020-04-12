package es.us.alumn.miggoncan2.model.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

/**
 * This Entity represents the Absence of a Doctor during a certain period
 * 
 * @author miggoncan
 *
 */
@Data
@Entity
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
	private Date start;
	
	@Column(nullable = false)
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
}

package es.us.alumn.miggoncan2.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class ShiftConfiguration {
	@Id
	private Long doctorId;
	@MapsId
	@OneToOne
	private Doctor doctor;
	
	@Column(nullable = false)
	private Integer maxShifts;
	
	@Column(nullable = false)
	private Integer minShifts;
	
	@Column(nullable = false)
	private Boolean doesConsultations;
	
	public ShiftConfiguration(Integer maxShifts, Integer minShifts, Boolean doesConsultation) {
		this.maxShifts = maxShifts;
		this.minShifts = minShifts;
		this.doesConsultations = doesConsultation;
	}
	
	public ShiftConfiguration() {}
	
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
		if (doctor != null) {
			this.doctorId = doctor.getId();
		} else  {
			this.doctorId = null;
		}
	}
}

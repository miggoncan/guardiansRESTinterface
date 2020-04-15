package es.us.alumn.miggoncan2.model.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import es.us.alumn.miggoncan2.model.entityvalidation.ValidShiftConfiguration;
import lombok.Data;

@Data
@Entity
@ValidShiftConfiguration
public class ShiftConfiguration {
	@Id
	private Long doctorId;
	@MapsId
	@OneToOne
	private Doctor doctor;
	
	@Column(nullable = false)
	@PositiveOrZero
	private Integer maxShifts;
	
	@Column(nullable = false)
	@PositiveOrZero
	private Integer minShifts;
	
	@Column(nullable = false)
	@NotNull
	private Boolean doesConsultations;
	
	@ManyToMany
	private List<AllowedShift> unwantedShifts;
	
	@ManyToMany
	private List<AllowedShift> unavailableShifts;
	
	@ManyToMany
	private List<AllowedShift> wantedShifts;
	
	@ManyToMany
	private List<AllowedShift> mandatoryShifts;
	
	
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

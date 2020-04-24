package es.us.alumn.miggoncan2.model.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import es.us.alumn.miggoncan2.model.entities.serializers.ShiftConfigurationSerializer;
import es.us.alumn.miggoncan2.model.validation.ValidShiftConfiguration;
import lombok.Data;

/**
 * The ShiftConfiguration entities represent the configuration that will be
 * applied whenever a shifts are scheduled.
 * 
 * Note ShiftConfiguration is a weak entity. Hence, the id of the {@link Doctor}
 * associated to this ShiftConfiguration is used as the primary key
 * 
 * There are some constraints that every ShiftConfiguration has to meet, besides
 * the ones declared within the attributes. See {@link ValidShiftConfiguration}
 * 
 * As {@link #unwantedShifts}, {@link #unavailableShifts}, {@link #wantedShifts}
 * and {@link #mandatoryShifts} will be frequently used in the documentation,
 * they will be referred to as shiftPreferences
 * 
 * @author miggoncan
 */
@Data
@Entity
@ValidShiftConfiguration
@JsonSerialize(using = ShiftConfigurationSerializer.class)
public class ShiftConfiguration {
	@Id
	private Long doctorId;
	@MapsId
	@OneToOne
	private Doctor doctor;

	/**
	 * maxShifts represents the maximum number of shifts the associated
	 * {@link Doctor} is allowed to have during a month
	 * 
	 * This number does not take into account the number of cycle-shifts the
	 * associated {@link Doctor} has to have
	 * 
	 * @see ShiftCycle
	 */
	@Column(nullable = false)
	@PositiveOrZero
	@NotNull
	private Integer maxShifts;

	/**
	 * minShifts represents the minimum number of shifts the associated
	 * {@link Doctor} has to have within a month
	 * 
	 * This number does not take into account the number of cycle-shifts the
	 * associated {@link Doctor} has to have
	 * 
	 * @see ShiftCycle
	 */
	@Column(nullable = false)
	@PositiveOrZero
	@NotNull
	private Integer minShifts;

	/**
	 * This Boolean will be true if some/all shifts assigned to the associated
	 * {@link Doctor} could be consultations, or just regular shifts
	 */
	@Column(nullable = false)
	@NotNull
	private Boolean doesConsultations;

	/**
	 * unwantedShifts indicates the shifts the associated {@link Doctor} would
	 * rather not have assigned
	 */
	@ManyToMany
	private Set<AllowedShift> unwantedShifts;

	/**
	 * unavailableShifts indicates the shifts the associated {@link Doctor} cannot
	 * have, under any circumstances
	 */
	@ManyToMany
	private Set<AllowedShift> unavailableShifts;

	/**
	 * wantedShifts indicates the shifts the associated {@link Doctor} would like to
	 * have
	 */
	@ManyToMany
	private Set<AllowedShift> wantedShifts;

	/**
	 * mandatoryShifts indicates the shifts the associated {@link Doctor} HAS to
	 * have
	 */
	@ManyToMany
	private Set<AllowedShift> mandatoryShifts;

	public ShiftConfiguration(Integer maxShifts, Integer minShifts, Boolean doesConsultation) {
		this.maxShifts = maxShifts;
		this.minShifts = minShifts;
		this.doesConsultations = doesConsultation;
	}

	public ShiftConfiguration() {
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
		if (doctor != null) {
			this.doctorId = doctor.getId();
		} else {
			this.doctorId = null;
		}
	}
}

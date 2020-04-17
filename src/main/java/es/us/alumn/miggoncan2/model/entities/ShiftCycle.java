package es.us.alumn.miggoncan2.model.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
@Entity
public class ShiftCycle {
	// TODO Add constraint to make sure shiftNumbers in all ShiftCycles make an increasing sequence starting from 1
	@Id
	@Positive
	private Integer shiftNumber;
	
	@ManyToMany
	@NotEmpty
	private List<Doctor> doctors;
	
	// TODO Add constraint to make sure there is only one ShiftCycle with isNextShiftInCycle set to true
	
	/**
	 * This field will indicate which doctors will have the first cycle-shift the
	 * next time shifts are scheduled
	 */
	@Column(nullable = false)
	@NotNull
	private Boolean isNextShiftInCycle;
	
	
	public ShiftCycle(Integer shiftNumber, List<Doctor> doctors, Boolean isNextShiftInCycle) {
		this.shiftNumber = shiftNumber;
		this.doctors = doctors;
		this.isNextShiftInCycle = isNextShiftInCycle;
	}
	
	public ShiftCycle() {}
}

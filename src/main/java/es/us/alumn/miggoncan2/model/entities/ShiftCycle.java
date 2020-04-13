package es.us.alumn.miggoncan2.model.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
@Entity
public class ShiftCycle {
	@Id
	private Integer shiftNumber;
	
	@ManyToMany
	@NotEmpty
	private List<Doctor> doctors;
	
	/**
	 * This field will indicate which doctors will have the first 
	 * cycle-shift the next time shifts are scheduled
	 */
	private Boolean isNextShiftInCycle;
	
	
	public ShiftCycle(Integer shiftNumber, List<Doctor> doctors, Boolean isNextShiftInCycle) {
		this.shiftNumber = shiftNumber;
		this.doctors = doctors;
		this.isNextShiftInCycle = isNextShiftInCycle;
	}
	
	public ShiftCycle() {}
}

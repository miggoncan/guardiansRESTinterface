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

/**
 * {@link Doctor}s have some periodic shifts. This is, if some {@link Doctor}s
 * have a shift today, after a certain number of days, they will have another
 * one. This kind of shifts will be refered to as "cycle-shift", and should not
 * be confused with regular shifts. A "regular shift", or "shift" in short,
 * refers to the shifts that will vary from month to month and that do not occur
 * periodically.
 * 
 * This way, the ShiftCycle {@link Entity} represents one recurring cycle-shift
 * taken by several {@link Doctor}s
 * 
 * @author miggoncan
 */
@Data
@Entity
public class ShiftCycle {
	// TODO Change shiftNumber and isNextShiftInCycle for a referenceDate (Date object)
	/**
	 * shiftNumber number this cycle-shift represents within all shift-cycles
	 * 
	 * For example, if we had the following cycle: 
	 * 		Day 1: Sebastian and Diana 
	 * 		Day 2: Alex and Rasim 
	 * 		Day 3: Irati and Laura 
	 * Then, the shiftNumber of the cycle-shift "Sebastian and Diana" would be 1 
	 * (as of "Day 1")
	 * 
	 * Moreover, the shiftNumbers should start from 1 and be increased one unit with
	 * every cycle-shift (just as seen in the example above)
	 */
	@Id
	@Positive
	private Integer shiftNumber;

	/**
	 * The {@link List} of {@link Doctor}s that have this cycle-shift
	 */
	@ManyToMany
	@NotEmpty
	private List<Doctor> doctors;

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

	public ShiftCycle() {
	}
}

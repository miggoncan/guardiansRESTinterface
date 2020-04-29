package es.us.alumn.miggoncan2.model.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Range;

import es.us.alumn.miggoncan2.model.entities.primarykeys.DayMonthYearPK;
import es.us.alumn.miggoncan2.model.validation.ValidDayMonthYear;
import lombok.Data;

/**
 * {@link Doctor}s have some periodic shifts. This is, if some {@link Doctor}s
 * have a shift today, after a certain number of days, they will have another
 * one. This kind of shifts will be referred to as "cycle-shift", and should not
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
@IdClass(DayMonthYearPK.class)
@ValidDayMonthYear
public class ShiftCycle {
	/**
	 * All three field {@link #day}, {@link #month} and {@link #year} are used to 
	 * identify from which day the cycle-shifts should be calculated
	 * 
	 * For example, if we had started on Frebuary of 2020 with the following cycle: 
	 * 		1/2/2020: Sebastian and Diana 
	 * 		2/2/2020: Alex and Rasim 
	 * 		3/2/2020: Irati and Laura 
	 * Then, the reference of the cycle-shift "Sebastian and Diana" would be:
	 * 		day=1
	 * 		month=2 
	 * 		year=2020 
	 */
	@Id
	@Range(min = 1, max = 31)
	private Integer day;
	@Id
	@Range(min = 1, max = 12)
	private Integer month;
	@Id
	@Range(min = 1970)
	private Integer year;
	
	/**
	 * The {@link List} of {@link Doctor}s that have this cycle-shift
	 */
	@ManyToMany
	@NotEmpty
	private List<Doctor> doctors;

	public ShiftCycle(Integer referenceDay, Integer referenceMonth, Integer referenceYear, 
			List<Doctor> doctors) {
		this.day = referenceDay;
		this.month = referenceMonth;
		this.year = referenceYear;
		this.doctors = doctors;
	}

	public ShiftCycle() {
	}
}

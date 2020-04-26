package es.us.alumn.miggoncan2.model.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import es.us.alumn.miggoncan2.model.entities.primarykeys.CalendarPK;
import lombok.Data;

// TODO validate Calendar: all dayConfigurations have to be in order, and all days have to be supplied
// TODO test Calendar

/**
 * The Calendar {@link Entity} represents a certain month o a certain year, and
 * might have some specific configuration besides the specified in
 * {@link ShiftConfiguration} and {@link ShiftCycle}.
 * 
 * Note the primary key used for this {@link Entity} is composite, so the
 * {@link IdClass} annotation is used.
 * 
 * @see DayConfiguration
 * @see CalendarPK
 * 
 * @author miggoncan
 */
@Data
@Entity
@IdClass(CalendarPK.class)
public class Calendar {
	@Id
	@Range(min = 1, max = 12)
	private Integer month;
	@Id
	@Range(min = 1970)
	private Integer year;

	@OneToMany(mappedBy = "calendar")
	@JsonManagedReference
	private List<DayConfiguration> dayConfigurations;

	public Calendar(Integer month, Integer year) {
		this.month = month;
		this.year = year;
	}

	public Calendar() {
	}

	/**
	 * @return a CalendarPK instance that uniquely identifies this {@link Entity}
	 */
	public CalendarPK getPK() {
		return new CalendarPK(month, year);
	}
}

package guardians.model.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import guardians.model.entities.primarykeys.CalendarPK;
import guardians.model.validation.annotations.ValidCalendar;
import lombok.Data;

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
@ValidCalendar
public class Calendar {
	@Id
	@Range(min = 1, max = 12)
	private Integer month;
	@Id
	@Range(min = 1970)
	private Integer year;

	@OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Set<DayConfiguration> dayConfigurations;

	public Calendar(Integer month, Integer year) {
		this.month = month;
		this.year = year;
	}

	public Calendar() {
	}
}

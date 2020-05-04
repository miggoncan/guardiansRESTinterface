package guardians.model.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Range;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import guardians.model.entities.primarykeys.DayMonthYearPK;
import guardians.model.validation.annotations.ValidDayMonthYear;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * {@link Doctor}s have some periodic shifts. This is, if some {@link Doctor}s
 * have a shift today, after a certain number of days, they will have another
 * one. This kind of shifts will be referred to as "cycle-shift", and should not
 * be confused with regular shifts. A "regular shift", or "shift" in short,
 * refers to the shifts that will vary from month to month and that do not occur
 * periodically.
 * 
 * This way, the CycleShift {@link Entity} represents one recurring cycle-shift
 * taken by several {@link Doctor}s
 * 
 * @author miggoncan
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@IdClass(DayMonthYearPK.class)
@ValidDayMonthYear
// This is the name shown when a collection of cycleShifts is __embedded in a 
// JSON document
@Relation(collectionRelation = "cycleShifts")
public class CycleShift extends AbstractDay {
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
	// WRITE_ONLY prevent this attribute from being serialized, but allow it to be DEserialized
	@JsonProperty(access = Access.WRITE_ONLY)
	private List<Doctor> doctors;
	// When serializing to JSON, we want the doctors to be serialized as their
	// corresponding EntityModels (this is, including their links). Moreover, as we
	// don't want the doctors to be serialized twice, the actual persisted list of
	// Doctors is ignored
	// TODO Do put requests have to include the __embedded or doctors property?
	@Transient
	@JsonUnwrapped
	private CollectionModel<EntityModel<Doctor>> doctorEntities;

	public CycleShift(Integer referenceDay, Integer referenceMonth, Integer referenceYear, 
			List<Doctor> doctors) {
		this.day = referenceDay;
		this.month = referenceMonth;
		this.year = referenceYear;
		this.doctors = doctors;
	}

	public CycleShift() {
	}
}

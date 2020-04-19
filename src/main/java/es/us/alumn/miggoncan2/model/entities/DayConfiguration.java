package es.us.alumn.miggoncan2.model.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import es.us.alumn.miggoncan2.model.entities.primarykeys.DayConfigurationPK;
import lombok.Data;

// TODO validate dayConfiguration: the day has to be valid for the given month and there cannot be clashes between unwanted, wanted, .. shifts
// TODO change List<Doctor> to Set<Doctor>
// TODO test dayConfiguration

@Data
@Entity
@IdClass(DayConfigurationPK.class)
public class DayConfiguration {
	@Id
	@Range(min = 1, max = 31)
	private Integer day;
	@Id
	@Range(min = 1, max = 12)
	@Column(name = "calendar_month")
	private Integer month;
	@Id
	@Range(min = 1970)
	@Column(name = "calendar_year")
	private Integer year;
	@MapsId
	@ManyToOne
	@JsonBackReference
	private Calendar calendar;
	
	@Column(nullable = false)
	@NotNull
	private Boolean isWorkingDay;
	
	@Column(nullable = false)
	@PositiveOrZero
	@NotNull
	private Integer numShifts;
	
	@Column(nullable = false)
	@PositiveOrZero
	@NotNull
	private Integer numConsultations;
	
	@ManyToMany
	private List<Doctor> unwantedShifts;
	
	@ManyToMany
	private List<Doctor> unavailableShifts;
	
	@ManyToMany
	private List<Doctor> wantedShifts;
	
	@ManyToMany
	private List<Doctor> mandatoryShifts;
	
	@OneToMany(mappedBy = "dayConfiguration")
	@JsonManagedReference
	private List<CycleChange> cycleChanges;
	
	
	public DayConfiguration(Integer day, Boolean isWorkingDay, 
			Integer numShifts, Integer numConsultations) {
		this.day = day;
		this.isWorkingDay = isWorkingDay;
		this.numShifts = numShifts;
		this.numConsultations = numConsultations;
	}
	
	public DayConfiguration() {}
	
	@Override
	public String toString() {
		return DayConfiguration.class.getSimpleName()
				+ "("
					+ "day=" + day + ", "
					+ "month=" + month + ", "
					+ "year=" + year + ", "
					+ "isWorkingDay=" + isWorkingDay + ", "
					+ "numShifts=" + numShifts + ", "
					+ "numConsultations=" + numConsultations + ", "
					+ "unwantedShifts=" + unwantedShifts + ", "
					+ "unavailableShifts=" + unavailableShifts + ", "
					+ "wantedShifts=" + wantedShifts + ", "
					+ "mandatoryShifts=" + mandatoryShifts + ", "
					+ "cycleChanges=" + cycleChanges
				+ ")";
	}
	
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
		if (calendar != null) {
			this.month = calendar.getMonth();
			this.year = calendar.getYear();
		}
	}
	
	public DayConfigurationPK getPK() {
		return new DayConfigurationPK(day, month, year);
	}
}

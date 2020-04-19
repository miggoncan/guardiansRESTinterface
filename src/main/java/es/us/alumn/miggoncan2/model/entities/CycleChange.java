package es.us.alumn.miggoncan2.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonBackReference;

import es.us.alumn.miggoncan2.model.entities.primarykeys.DayConfigurationPK;
import lombok.Data;

//TODO add validation to cycleChange: the giver and the receiver cannot be the same doctor
// TODO test cyclechange

@Data
@Entity
@IdClass(DayConfigurationPK.class)
public class CycleChange {
	@Id
	@Range(min = 1, max = 31)
	@Column(name = "day_configuration_day")
	private Integer day;
	@Id
	@Range(min = 1, max = 12)
	@Column(name = "day_configuration_calendar_month")
	private Integer month;
	@Id
	@Range(min = 1970)
	@Column(name = "day_configuration_calendar_year")
	private Integer year;
	@ManyToOne
	@MapsId
	@JsonBackReference
	private DayConfiguration dayConfiguration;
	
	/**
	 * The cycle giver is the Doctor that gives their cycle-shift to another Doctor
	 */
	@ManyToOne
	private Doctor cycleGiver; 
	
	/**
	 * The cycle receiver is the Doctor that will take the cycle-shift
	 */
	@ManyToOne
	private Doctor cycleReceiver;
	
	
	public CycleChange(Doctor cycleGiver, Doctor cycleReceiver) {
		this.cycleGiver = cycleGiver;
		this.cycleReceiver = cycleReceiver;
	}
	
	public CycleChange() {}
	
	@Override
	public String toString() {
		return CycleChange.class.getSimpleName()
				+ "("
					+ "day=" + day + ", "
					+ "cycleGiver=" + cycleGiver + ", "
					+ "cycleReceiver=" + cycleReceiver
				+ ")";
	}
	
	public void setDayConfiguration(DayConfiguration dayConfiguration) {
		this.dayConfiguration = dayConfiguration;
		if (dayConfiguration != null) {
			this.day = dayConfiguration.getDay();
			this.month = dayConfiguration.getMonth();
			this.year = dayConfiguration.getYear();
		}
	}
}

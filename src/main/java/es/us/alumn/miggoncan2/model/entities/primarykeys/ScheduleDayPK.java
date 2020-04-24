package es.us.alumn.miggoncan2.model.entities.primarykeys;

import java.io.Serializable;

import javax.persistence.Entity;

import es.us.alumn.miggoncan2.model.entities.ScheduleDay;
import lombok.Data;

/**
 * This class represents the primary key of the {@link Entity}
 * {@link ScheduleDay}
 * 
 * A {@link ScheduleDay} is uniquely identified with a day, a month and a year
 * 
 * @author miggoncan
 *
 */
@Data
public class ScheduleDayPK implements Serializable {
	private static final long serialVersionUID = 7202793146379391747L;

	private Integer day;
	private Integer month;
	private Integer year;

	public ScheduleDayPK(Integer day, Integer month, Integer year) {
		this.day = day;
		this.month = month;
		this.year = year;
	}

	public ScheduleDayPK() {
	}
}

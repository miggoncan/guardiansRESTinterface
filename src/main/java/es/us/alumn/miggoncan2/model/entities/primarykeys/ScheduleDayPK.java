package es.us.alumn.miggoncan2.model.entities.primarykeys;

import java.io.Serializable;

import lombok.Data;

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
	
	public ScheduleDayPK() {}
}

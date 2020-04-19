package es.us.alumn.miggoncan2.model.entities.primarykeys;

import java.io.Serializable;

import lombok.Data;

/**
 * This class represents the primary key of the Entity DayConfiguration
 * 
 * @author miggoncan
 */
@Data
public class DayConfigurationPK implements Serializable {
	private static final long serialVersionUID = 6732382410268399527L;
	
	private Integer day;
	private Integer month;
	private Integer year;
	
	
	public DayConfigurationPK(Integer day, Integer month, Integer year) {
		this.day = day;
		this.month = month;
		this.year = year;
	}
	
	public DayConfigurationPK() {}
}

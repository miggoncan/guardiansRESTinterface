package es.us.alumn.miggoncan2.model.entities.primarykeys;

import java.io.Serializable;

import lombok.Data;

/**
 * This class represents the primary key for the Entity Calendar
 * 
 * @author miggoncan
 */
@Data
public class CalendarPK implements Serializable {
	private static final long serialVersionUID = 66688158711309197L;
	
	private Integer month;
	private Integer year;
	
	
	public CalendarPK(Integer month, Integer year) {
		this.month = month;
		this.year = year;
	}
	
	public CalendarPK() {}
}
package es.us.alumn.miggoncan2.model.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import es.us.alumn.miggoncan2.model.entities.primarykeys.CalendarPK;
import lombok.Data;

// TODO check all days given in days exist in this month
// TODO test Schedule

@Data
@Entity
@IdClass(CalendarPK.class)
public class Schedule {
	@Id
	@Column(name = "calendar_month")
	@Range(min = 1, max = 12)
	private Integer month;
	@Id
	@Column(name = "calendar_year")
	@Range(min = 1970)
	private Integer year;
	@MapsId
	@OneToOne
	@NotNull
	private Calendar calendar;
	
	@Enumerated(EnumType.STRING)
	private ScheduleStatus status;
	
	@OneToMany(mappedBy = "schedule")
	@NotEmpty
	@JsonManagedReference
	private List<ScheduleDay> days;
	
	
	public Schedule(ScheduleStatus status) {
		this.status = status;
	}
	
	public Schedule() {}
	
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
		if (calendar != null) {
			this.month = calendar.getMonth();
			this.year = calendar.getYear();
		}
	}
}

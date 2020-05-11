package guardians.controllers.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import guardians.controllers.CalendarController;
import guardians.controllers.ScheduleController;
import guardians.controllers.exceptions.InvalidEntityException;
import guardians.model.entities.Schedule;
import guardians.model.entities.Schedule.ScheduleStatus;

/**
 * This class is responsible of converting a {@link Schedule} into its
 * {@link EntityModel} representation. This is, adding links to the
 * {@link Schedule}
 * 
 * @author miggoncan
 */
@Component
public class ScheduleAssembler implements RepresentationModelAssembler<Schedule, EntityModel<Schedule>> {

	@Value("${api.links.calendar}")
	private String calendarLink;

	@Value("${api.links.scheduleStatus}")
	private String scheduleStatusLink;

	@Value("${api.links.confirmSchedule}")
	private String confirmSchedulLink;

	@Override
	public EntityModel<Schedule> toModel(Schedule entity) {
		YearMonth yearMonth = null;
		try {
			yearMonth = YearMonth.of(entity.getYear(), entity.getMonth());
		} catch (DateTimeParseException e) {
			throw new InvalidEntityException("Trying to map to an EntityModel an invalid Schedule. "
					+ "Its month and year are: " + entity.getMonth() + "/" + entity.getYear());
		}
		EntityModel<Schedule> schedule = new EntityModel<>(entity,
				linkTo(methodOn(ScheduleController.class).getScheduleRequest(yearMonth)).withSelfRel(),
				linkTo(methodOn(CalendarController.class).getCalendar(yearMonth)).withRel(calendarLink));

		if (entity.getStatus() == ScheduleStatus.PENDING_CONFIRMATION) {
			schedule.add(linkTo(methodOn(ScheduleController.class).changeStatus(yearMonth,
					ScheduleStatus.CONFIRMED.toString().toLowerCase())).withRel(confirmSchedulLink));
		}
		
		return schedule;
	}

}

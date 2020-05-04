package guardians.controllers.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import guardians.controllers.CalendarController;
import guardians.controllers.RootController;
import guardians.controllers.ScheduleController;
import guardians.model.entities.Calendar;

/**
 * CalendarAssembler is reponsible for converting {@link Calendar} instances
 * into their {@link EntityModel} representation. This is, adding to
 * corresponding links to it
 * 
 * @author miggoncan
 */
@Component
public class CalendarAssembler implements RepresentationModelAssembler<Calendar, EntityModel<Calendar>> {

	@Value("${api.links.root}")
	private String rootLink;

	@Value("${api.links.calendars}")
	private String calendarsLink;

	@Value("${api.links.schedule}")
	private String scheduleLink;

	@Override
	public EntityModel<Calendar> toModel(Calendar entity) {
		YearMonth yearMonth = YearMonth.of(entity.getYear(), entity.getMonth());
		return new EntityModel<Calendar>(entity, 
				linkTo(methodOn(CalendarController.class).getCalendar(yearMonth)).withSelfRel(),
				linkTo(methodOn(CalendarController.class).getCalendars()).withRel(calendarsLink),
				linkTo(methodOn(ScheduleController.class).getSchedule(yearMonth)).withRel(scheduleLink));
	}

	@Override
	public CollectionModel<EntityModel<Calendar>> toCollectionModel(Iterable<? extends Calendar> entities) {
		List<EntityModel<Calendar>> calendars = new LinkedList<>();
		for (Calendar entity : entities) {
			calendars.add(this.toModel(entity));
		}
		return new CollectionModel<>(calendars, 
				linkTo(methodOn(CalendarController.class).getCalendars()).withSelfRel(),
				linkTo(methodOn(RootController.class).getRootLinks()).withRel(rootLink));
	}
}

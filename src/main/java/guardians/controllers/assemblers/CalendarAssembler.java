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
import guardians.model.dtos.general.CalendarPublicDTO;

/**
 * CalendarAssembler is reponsible for converting {@link CalendarPublicDTO}
 * instances into their {@link EntityModel} representation. This is, adding to
 * corresponding links to it
 * 
 * @author miggoncan
 */
@Component
public class CalendarAssembler
		implements RepresentationModelAssembler<CalendarPublicDTO, EntityModel<CalendarPublicDTO>> {

	@Value("${api.links.root}")
	private String rootLink;

	@Value("${api.links.calendars}")
	private String calendarsLink;

	@Value("${api.links.schedule}")
	private String scheduleLink;

	@Override
	public EntityModel<CalendarPublicDTO> toModel(CalendarPublicDTO entity) {
		YearMonth yearMonth = YearMonth.of(entity.getYear(), entity.getMonth());
		return new EntityModel<CalendarPublicDTO>(entity,
				linkTo(methodOn(CalendarController.class).getCalendar(yearMonth)).withSelfRel(),
				linkTo(methodOn(CalendarController.class).getCalendars()).withRel(calendarsLink),
				linkTo(methodOn(ScheduleController.class).getScheduleRequest(yearMonth)).withRel(scheduleLink));
	}

	@Override
	public CollectionModel<EntityModel<CalendarPublicDTO>> toCollectionModel(
			Iterable<? extends CalendarPublicDTO> entities) {
		List<EntityModel<CalendarPublicDTO>> calendars = new LinkedList<>();
		for (CalendarPublicDTO entity : entities) {
			calendars.add(this.toModel(entity));
		}
		return new CollectionModel<>(calendars, linkTo(methodOn(CalendarController.class).getCalendars()).withSelfRel(),
				linkTo(methodOn(RootController.class).getRootLinks()).withRel(rootLink));
	}
}

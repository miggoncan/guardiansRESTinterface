package guardians.controllers.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import guardians.controllers.CycleShiftsController;
import guardians.model.entities.CycleShift;

/**
 * This class is reponsible for converting a {@link CycleShift} into its
 * {@link EntityModel} representation. This is, adding links to it.
 * 
 * @author miggoncan
 */
@Component
public class CycleShiftAssembler implements RepresentationModelAssembler<CycleShift, EntityModel<CycleShift>> {

	@Autowired
	private DoctorAssembler doctorAssembler;
	
	@Value("${api.links.cycleshifts}")
	private String cycleShiftsLink;

	@Override
	public EntityModel<CycleShift> toModel(CycleShift entity) {
		Integer day = entity.getDay();
		Integer month = entity.getMonth();
		Integer year = entity.getYear();
		// Add the embedded Doctors to the entity
		entity.setDoctorEntities(doctorAssembler.toCollectionModel(entity.getDoctors()));
		return new EntityModel<CycleShift>(entity,
				linkTo(methodOn(CycleShiftsController.class).getCycleShift(LocalDate.of(year, month, day)))
						.withSelfRel(),
				linkTo(methodOn(CycleShiftsController.class).getCycleShifts()).withRel(cycleShiftsLink));
	}

	@Override
	public CollectionModel<EntityModel<CycleShift>> toCollectionModel(Iterable<? extends CycleShift> entities) {
		List<EntityModel<CycleShift>> cycleShifts = new LinkedList<>();
		for (CycleShift entity : entities) {
			cycleShifts.add(this.toModel(entity));
		}
		return new CollectionModel<>(cycleShifts,
				linkTo(methodOn(CycleShiftsController.class).getCycleShifts()).withSelfRel());
	}

}

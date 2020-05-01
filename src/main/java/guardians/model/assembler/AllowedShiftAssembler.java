package es.us.alumn.miggoncan2.model.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import es.us.alumn.miggoncan2.controllers.AllowedShiftsController;
import es.us.alumn.miggoncan2.controllers.ShiftConfigurationController;
import es.us.alumn.miggoncan2.model.entities.AllowedShift;

/**
 * AllowedShiftAssembler is responsible for converting {@link AllowedShift}s
 * into their {@link EntityModel} representation. This is, adding the necessary
 * links to them.
 * 
 * @author miggoncan
 */
@Component
public class AllowedShiftAssembler implements RepresentationModelAssembler<AllowedShift, EntityModel<AllowedShift>> {

	@Value("${api.links.allowedshifts}")
	private String allowedShiftsLink;
	
	@Value("${api.links.shiftconfs}")
	private String shiftConfsLink;

	@Override
	public EntityModel<AllowedShift> toModel(AllowedShift entity) {
		return new EntityModel<AllowedShift>(entity, 
				linkTo(methodOn(AllowedShiftsController.class).getAllowedShift(entity.getId())).withSelfRel(),
				linkTo(methodOn(AllowedShiftsController.class).getAllowedShifts()).withRel(allowedShiftsLink));
	}

	@Override
	public CollectionModel<EntityModel<AllowedShift>> toCollectionModel(Iterable<? extends AllowedShift> entities) {
		List<EntityModel<AllowedShift>> allowedShifts = new LinkedList<>();
		for (AllowedShift entity : entities) {
			allowedShifts.add(this.toModel(entity));
		}
		return new CollectionModel<>(allowedShifts,
				linkTo(methodOn(AllowedShiftsController.class).getAllowedShifts()).withSelfRel(),
				linkTo(methodOn(ShiftConfigurationController.class).getShitfConfigurations()).withRel(shiftConfsLink));
	}

}

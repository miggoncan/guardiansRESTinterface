package es.us.alumn.miggoncan2.model.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.LinkedList;
import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import es.us.alumn.miggoncan2.controllers.DoctorController;
import es.us.alumn.miggoncan2.controllers.RootController;
import es.us.alumn.miggoncan2.controllers.ShiftConfigurationController;
import es.us.alumn.miggoncan2.model.entities.ShiftConfiguration;

/**
 * DoctorAssembler is responsible for converting a {@link ShiftConfiguration}
 * object to its {@link EntityModel} representation. This is, adding links to
 * it.
 * 
 * @author miggoncan
 *
 */
@Component
public class ShiftConfigurationAssembler
		implements RepresentationModelAssembler<ShiftConfiguration, EntityModel<ShiftConfiguration>> {

	@Override
	public EntityModel<ShiftConfiguration> toModel(ShiftConfiguration entity) {
		return new EntityModel<ShiftConfiguration>(entity,
				linkTo(methodOn(ShiftConfigurationController.class).getShitfConfiguration(entity.getDoctorId()))
						.withSelfRel(),
				// TODO there has to be a way to not use hard-coded strings
				linkTo(methodOn(ShiftConfigurationController.class).getShitfConfigurations()).withRel("config-cas"),
				linkTo(methodOn(DoctorController.class).getDoctor(entity.getDoctorId())).withRel("facultativo"),
				linkTo(methodOn(ShiftConfigurationController.class).getAllowedShifts()).withRel("dias_elegibles"));
	}

	@Override
	public CollectionModel<EntityModel<ShiftConfiguration>> toCollectionModel(
			Iterable<? extends ShiftConfiguration> entities) {
		List<EntityModel<ShiftConfiguration>> shiftConfs = new LinkedList<>();
		for (ShiftConfiguration entity : entities) {
			shiftConfs.add(this.toModel(entity));
		}
		return new CollectionModel<>(shiftConfs,
				linkTo(methodOn(ShiftConfigurationController.class).getShitfConfigurations()).withSelfRel(),
				linkTo(methodOn(RootController.class).getRootLinks()).withRel("raiz"));
	}
}

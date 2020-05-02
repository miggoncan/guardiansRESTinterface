package guardians.controllers.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import guardians.controllers.AllowedShiftsController;
import guardians.controllers.DoctorController;
import guardians.controllers.RootController;
import guardians.controllers.ShiftConfigurationController;
import guardians.model.entities.ShiftConfiguration;

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
	
	@Value("${api.links.root}")
	private String rootLink;
	
	@Value("${api.links.shiftconfs}")
	private String shiftConfsLink;
	
	@Value("${api.links.doctor}")
	private String doctorLink;
	
	@Value("${api.links.allowedshifts}")
	private String allowedShiftsLink;

	@Override
	public EntityModel<ShiftConfiguration> toModel(ShiftConfiguration entity) {
		return new EntityModel<ShiftConfiguration>(entity,
				linkTo(methodOn(ShiftConfigurationController.class).getShitfConfiguration(entity.getDoctorId()))
						.withSelfRel(),
				linkTo(methodOn(ShiftConfigurationController.class).getShitfConfigurations()).withRel(shiftConfsLink),
				linkTo(methodOn(DoctorController.class).getDoctor(entity.getDoctorId())).withRel(doctorLink),
				linkTo(methodOn(AllowedShiftsController.class).getAllowedShifts()).withRel(allowedShiftsLink));
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
				linkTo(methodOn(RootController.class).getRootLinks()).withRel(rootLink));
	}
}

package guardians.controllers.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import guardians.controllers.DoctorController;
import guardians.controllers.RootController;
import guardians.controllers.ShiftConfigurationController;
import guardians.model.dtos.general.DoctorPublicDTO;

/**
 * DoctorAssembler is responsible for converting a {@link DoctorPublicDTO} object to its
 * {@link EntityModel} representation. This is, adding links to it.
 * 
 * @author miggoncan
 */
@Component
public class DoctorAssembler implements RepresentationModelAssembler<DoctorPublicDTO, EntityModel<DoctorPublicDTO>> {

	@Value("${api.links.root}")
	private String rootLink;

	@Value("${api.links.doctors}")
	private String doctorsLink;

	@Value("${api.links.shiftconf}")
	private String shiftConfLink;

	@Override
	public EntityModel<DoctorPublicDTO> toModel(DoctorPublicDTO entity) {
		return new EntityModel<>(entity,
				linkTo(methodOn(DoctorController.class).getDoctor(entity.getId())).withSelfRel(),
				linkTo(methodOn(DoctorController.class).getDoctors(null)).withRel(doctorsLink),
				linkTo(methodOn(ShiftConfigurationController.class).getShitfConfiguration(entity.getId()))
						.withRel(shiftConfLink));
	}

	@Override
	public CollectionModel<EntityModel<DoctorPublicDTO>> toCollectionModel(Iterable<? extends DoctorPublicDTO> entities) {
		List<EntityModel<DoctorPublicDTO>> doctors = new LinkedList<>();
		for (DoctorPublicDTO entity : entities) {
			doctors.add(this.toModel(entity));
		}
		return new CollectionModel<>(doctors, 
				linkTo(methodOn(DoctorController.class).getDoctors(null)).withSelfRel(),
				linkTo(methodOn(RootController.class).getRootLinks()).withRel(rootLink));
	}
}

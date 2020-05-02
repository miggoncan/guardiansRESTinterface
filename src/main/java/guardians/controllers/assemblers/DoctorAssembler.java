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
import guardians.model.entities.Doctor;

/**
 * DoctorAssembler is responsible for converting a {@link Doctor} object to its
 * {@link EntityModel} representation. This is, adding links to it.
 * 
 * @author miggoncan
 */
@Component
public class DoctorAssembler implements RepresentationModelAssembler<Doctor, EntityModel<Doctor>> {
	
	@Value("${api.links.root}")
	private String rootLink;
	
	@Value("${api.links.doctors}")
	private String doctorsLink;
	
	@Value("${api.links.shiftconf}")
	private String shiftConfLink;

	@Override
	public EntityModel<Doctor> toModel(Doctor enitity) {
		return new EntityModel<Doctor>(enitity,
				linkTo(methodOn(DoctorController.class).getDoctor(enitity.getId())).withSelfRel(),
				linkTo(methodOn(DoctorController.class).getDoctors()).withRel(doctorsLink),
				linkTo(methodOn(ShiftConfigurationController.class).getShitfConfiguration(enitity.getId()))
						.withRel(shiftConfLink));
	}

	@Override
	public CollectionModel<EntityModel<Doctor>> toCollectionModel(Iterable<? extends Doctor> entities) {
		List<EntityModel<Doctor>> doctors = new LinkedList<>();
		for (Doctor entity : entities) {
			doctors.add(this.toModel(entity));
		}
		return new CollectionModel<>(doctors, linkTo(methodOn(DoctorController.class).getDoctors()).withSelfRel(),
				linkTo(methodOn(RootController.class).getRootLinks()).withRel(rootLink));
	}
}

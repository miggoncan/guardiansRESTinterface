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
import es.us.alumn.miggoncan2.model.entities.Doctor;

/**
 * DoctorAssembler is responsible for converting a {@link Doctor} object to its
 * {@link EntityModel} representation. This is, adding links to it.
 * 
 * @author miggoncan
 */
@Component
public class DoctorAssembler implements RepresentationModelAssembler<Doctor, EntityModel<Doctor>> {

	@Override
	public EntityModel<Doctor> toModel(Doctor enitity) {
		return new EntityModel<Doctor>(enitity,
				linkTo(methodOn(DoctorController.class).getDoctor(enitity.getId())).withSelfRel(),
				linkTo(methodOn(DoctorController.class).getDoctors()).withRel("facultativos"),
				linkTo(methodOn(ShiftConfigurationController.class).getShitfConfiguration(enitity.getId()))
						.withRel("config-ca"));
	}

	@Override
	public CollectionModel<EntityModel<Doctor>> toCollectionModel(Iterable<? extends Doctor> entities) {
		List<EntityModel<Doctor>> doctors = new LinkedList<>();
		for (Doctor entity : entities) {
			doctors.add(this.toModel(entity));
		}
		return new CollectionModel<>(doctors, linkTo(methodOn(DoctorController.class).getDoctors()).withSelfRel(),
				linkTo(methodOn(RootController.class).getRootLinks()).withRel("raiz"));
	}
}

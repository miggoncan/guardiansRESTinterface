package guardians.controllers.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import guardians.model.entities.CycleShift;

/**
 * TODO
 * 
 * @author miggoncan
 */
@Component
public class CycleShiftAssembler implements RepresentationModelAssembler<CycleShift, EntityModel<CycleShift>> {

	@Override
	public EntityModel<CycleShift> toModel(CycleShift entity) {
		// TODO Auto-generated method stub
		return null;
	}

}

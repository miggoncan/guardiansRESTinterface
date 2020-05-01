package es.us.alumn.miggoncan2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.us.alumn.miggoncan2.controllers.exceptions.AllowedShiftNotFoundException;
import es.us.alumn.miggoncan2.model.assembler.AllowedShiftAssembler;
import es.us.alumn.miggoncan2.model.entities.AllowedShift;
import es.us.alumn.miggoncan2.model.repositories.AllowedShiftRepository;

/**
 * The purpose of the AllowedShiftsController will be serving the list of
 * {@link AllowedShift}s
 * 
 * @author miggoncan
 */
@RestController
@RequestMapping("/allowed-shifts")
public class AllowedShiftsController {

	@Autowired
	AllowedShiftRepository allowedShiftRepository;

	@Autowired
	AllowedShiftAssembler allowedShiftAssembler;

	/**
	 * This method handles GET requests for the {@link AllowedShift}s
	 * 
	 * @return The collection of all {@link AllowedShift} available in the database
	 */
	@GetMapping("")
	public CollectionModel<EntityModel<AllowedShift>> getAllowedShifts() {
		return allowedShiftAssembler.toCollectionModel(allowedShiftRepository.findAll());
	}

	/**
	 * This method handles GET requests for one specific {@link AllowedShift}
	 * 
	 * @param allowedShiftId The id of the wanted {@link AllowedShift}
	 * @return the found {@link AllowedShift}
	 * @throws AllowedShiftNotFoundException if the provided allowedShiftId does not
	 *                                       match any existing {@link AllowedShift}
	 */
	@GetMapping("/{allowedShiftId}")
	public EntityModel<AllowedShift> getAllowedShift(@PathVariable Integer allowedShiftId) {
		AllowedShift allowedShift = allowedShiftRepository.findById(allowedShiftId)
				.orElseThrow(() -> new AllowedShiftNotFoundException(allowedShiftId));
		return allowedShiftAssembler.toModel(allowedShift);
	}
}

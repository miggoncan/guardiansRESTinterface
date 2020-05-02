package guardians.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import guardians.controllers.assemblers.AllowedShiftAssembler;
import guardians.controllers.exceptions.AllowedShiftNotFoundException;
import guardians.model.entities.AllowedShift;
import guardians.model.repositories.AllowedShiftRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * The purpose of the AllowedShiftsController will be serving the list of
 * {@link AllowedShift}s
 * 
 * @author miggoncan
 */
@RestController
@RequestMapping("/allowed-shifts")
@Slf4j
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
		log.info("Request received: returning all available allowed shifts");
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
		log.info("Request received: looking for the allowed shift with id " + allowedShiftId);;
		AllowedShift allowedShift = allowedShiftRepository.findById(allowedShiftId)
				.orElseThrow(() -> new AllowedShiftNotFoundException(allowedShiftId));
		return allowedShiftAssembler.toModel(allowedShift);
	}
}

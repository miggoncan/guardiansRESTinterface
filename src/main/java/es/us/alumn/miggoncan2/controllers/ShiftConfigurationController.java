package es.us.alumn.miggoncan2.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import es.us.alumn.miggoncan2.controllers.exceptions.ShiftConfigurationNotFoundException;
import es.us.alumn.miggoncan2.model.assembler.ShiftConfigurationAssembler;
import es.us.alumn.miggoncan2.model.entities.AllowedShift;
import es.us.alumn.miggoncan2.model.entities.Doctor;
import es.us.alumn.miggoncan2.model.entities.ShiftConfiguration;
import es.us.alumn.miggoncan2.model.repositories.AllowedShiftRepository;
import es.us.alumn.miggoncan2.model.repositories.ShiftConfigurationRepository;

/**
 * The ShiftConfigurationController will handle all requests related to the
 * shift configuration of doctors
 * 
 * @author miggoncan
 */
@Controller
public class ShiftConfigurationController {
	@Autowired
	ShiftConfigurationRepository shiftConfigurationRepository;

	@Autowired
	AllowedShiftRepository allowedShiftRepository;

	@Autowired
	ShiftConfigurationAssembler shiftConfigurationAssembler;

	/**
	 * This method handles GET requests for the complete list of
	 * {@link ShiftConfiguration}
	 * 
	 * @return A collection with all the {@link ShiftConfiguration} available in the
	 *         database
	 */
	@GetMapping("/facultativos/config-cas")
	@ResponseBody
	public CollectionModel<EntityModel<ShiftConfiguration>> getShitfConfigurations() {
		return shiftConfigurationAssembler.toCollectionModel(shiftConfigurationRepository.findAll());
	}

	/**
	 * This method handles GET requests for the {@link ShiftConfiguration} of one
	 * precise {@link Doctor}
	 * 
	 * @param doctorId The id of the {@link Doctor}
	 * @return The corresponding {@link ShiftConfiguration}
	 */
	@GetMapping("/facultativos/config-cas/{doctorId}")
	@ResponseBody
	public EntityModel<ShiftConfiguration> getShitfConfiguration(@PathVariable Long doctorId) {
		ShiftConfiguration shiftConfig = shiftConfigurationRepository.findById(doctorId)
				.orElseThrow(() -> new ShiftConfigurationNotFoundException(doctorId));
		return shiftConfigurationAssembler.toModel(shiftConfig);
	}

	/**
	 * This method handles GET requests for the {@link AllowedShift}
	 * 
	 * @return The collection of all {@link AllowedShift} available in the database
	 */
	@GetMapping("/dias-elegibles")
	@ResponseBody
	public List<AllowedShift> getAllowedShifts() {
		// TODO create assemble for AllowedShift
		return allowedShiftRepository.findAll();
	}
}

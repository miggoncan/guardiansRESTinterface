package es.us.alumn.miggoncan2.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.us.alumn.miggoncan2.controllers.exceptions.ShiftConfigurationNotFoundException;
import es.us.alumn.miggoncan2.model.assembler.ShiftConfigurationAssembler;
import es.us.alumn.miggoncan2.model.entities.Doctor;
import es.us.alumn.miggoncan2.model.entities.ShiftConfiguration;
import es.us.alumn.miggoncan2.model.repositories.ShiftConfigurationRepository;

/**
 * The ShiftConfigurationController will handle all requests related to the
 * shift configuration of doctors
 * 
 * @author miggoncan
 */
@RestController
@RequestMapping("/doctors/shift-configs")
public class ShiftConfigurationController {
	@Autowired
	ShiftConfigurationRepository shiftConfigurationRepository;

	@Autowired
	ShiftConfigurationAssembler shiftConfigurationAssembler;

	/**
	 * This method handles GET requests for the complete list of
	 * {@link ShiftConfiguration}
	 * 
	 * @return A collection with all the {@link ShiftConfiguration} available in the
	 *         database
	 */
	@GetMapping("")
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
	@GetMapping("/{doctorId}")
	public EntityModel<ShiftConfiguration> getShitfConfiguration(@PathVariable Long doctorId) {
		ShiftConfiguration shiftConfig = shiftConfigurationRepository.findById(doctorId)
				.orElseThrow(() -> new ShiftConfigurationNotFoundException(doctorId));
		return shiftConfigurationAssembler.toModel(shiftConfig);
	}
}

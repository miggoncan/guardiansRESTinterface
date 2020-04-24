package es.us.alumn.miggoncan2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import es.us.alumn.miggoncan2.controllers.exceptions.DoctorNotFoundException;
import es.us.alumn.miggoncan2.model.assembler.DoctorAssembler;
import es.us.alumn.miggoncan2.model.entities.Doctor;
import es.us.alumn.miggoncan2.model.entities.ShiftConfiguration;
import es.us.alumn.miggoncan2.model.repositories.DoctorRepository;

/**
 * The DoctorController will handle all requests related to the {@link Doctor}
 * themselves, but not their {@link ShiftConfiguration}
 * 
 * @author miggoncan
 */
@Controller
public class DoctorController {
	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private DoctorAssembler doctorAssembler;

	// TODO Add links to the responses
	// TODO Map all needed methods

	// TODO there has to be a way to not use hard-coded resource URIs
	/**
	 * Handle requests for all the existing {@link Doctor} in the database. Only the
	 * {@link Doctor} entities themselves will be returned. To get their shift
	 * configuration
	 * 
	 * @see ShiftConfigurationController#getShitfConfigurations()
	 * 
	 * @return The existing doctors
	 */
	@GetMapping("/facultativos")
	@ResponseBody
	public CollectionModel<EntityModel<Doctor>> getDoctors() {
		return doctorAssembler.toCollectionModel(doctorRepository.findAll());
	}

	/**
	 * Handle the request for the information related to a single {@link Doctor},
	 * provided their id
	 * 
	 * @param doctorId The unique identifiers used to search for the {@link Doctor}
	 * @return The {@link Doctor} found
	 * @throws DoctorNotFoundException if the {@link Doctor} was not found in the
	 *                                 database
	 */
	@GetMapping("/facultativos/{doctorId}")
	@ResponseBody
	public EntityModel<Doctor> getDoctor(@PathVariable Long doctorId) {
		Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new DoctorNotFoundException(doctorId));
		return doctorAssembler.toModel(doctor);
	}
}

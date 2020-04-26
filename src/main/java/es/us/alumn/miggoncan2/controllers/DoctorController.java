package es.us.alumn.miggoncan2.controllers;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.us.alumn.miggoncan2.controllers.exceptions.DoctorAlreadyExistsException;
import es.us.alumn.miggoncan2.controllers.exceptions.DoctorNotFoundException;
import es.us.alumn.miggoncan2.controllers.exceptions.InvalidAbsenceException;
import es.us.alumn.miggoncan2.model.assembler.DoctorAssembler;
import es.us.alumn.miggoncan2.model.entities.Absence;
import es.us.alumn.miggoncan2.model.entities.Doctor;
import es.us.alumn.miggoncan2.model.entities.ShiftConfiguration;
import es.us.alumn.miggoncan2.model.repositories.AbsenceRepository;
import es.us.alumn.miggoncan2.model.repositories.DoctorRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * The DoctorController will handle all requests related to the {@link Doctor}
 * themselves, but not their {@link ShiftConfiguration}
 * 
 * @author miggoncan
 */
@Slf4j
@RestController
@RequestMapping("/doctors")
public class DoctorController {
	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private AbsenceRepository absenceRepository;

	@Autowired
	private DoctorAssembler doctorAssembler;

	@Autowired
	private Validator validator;

	// TODO Map all needed methods

	/**
	 * Handle requests for all the existing {@link Doctor} in the database. Only the
	 * {@link Doctor} entities themselves will be returned. To get their shift
	 * configuration
	 * 
	 * @see ShiftConfigurationController#getShitfConfigurations()
	 * 
	 * @return The existing doctors
	 */
	@GetMapping("")
	public CollectionModel<EntityModel<Doctor>> getDoctors() {
		log.info("Request received: returning all available doctors");
		return doctorAssembler.toCollectionModel(doctorRepository.findAll());
	}

	/**
	 * Handle the creation of a new {@link Doctor}
	 * 
	 * The {@link Doctor} cannot already exist. Moreover this {@link Doctor} might
	 * have an {@link Absence}, and both have to be valid
	 * 
	 * @param newDoctor The {@link Doctor} that will be persisted
	 * @return The created {@link Doctor} (including the assigned id)
	 * @throws {@link DoctorAlreadyExistsException}
	 * @throws {@link InvalidAbsenceException}
	 */
	@PostMapping("")
	public EntityModel<Doctor> newDoctor(@RequestBody @Valid Doctor newDoctor) {
		log.info("Request received: trying to create a new Doctor: " + newDoctor);
		// The Doctor cannot already exist
		String firstName = newDoctor.getFirstName();
		String lastNames = newDoctor.getLastNames();
		if (doctorRepository.findByFirstNameAndLastNames(firstName, lastNames).isPresent()) {
			log.info("The provided Doctor already exists. Throwing DoctorAlreadyExistsException");
			throw new DoctorAlreadyExistsException(firstName, lastNames);
		}

		// The Absence is kept separately as it has to be persisted after the Doctor
		Absence newAbsence = newDoctor.getAbsence();
		newDoctor.setAbsence(null);

		// The Absence has to be valid
		if (newAbsence != null) {
			Set<ConstraintViolation<Absence>> violations = validator.validate(newAbsence);
			if (!violations.isEmpty()) {
				log.info("The Doctor has an invalid Absence. Throwing InvalidAbsenceException");
				throw new InvalidAbsenceException(violations);
			}
		}

		log.info("Validation complete. Attempting to save Doctor");
		// First, persist the Doctor, then the Absence
		Doctor savedDoctor = doctorRepository.save(newDoctor);
		log.info("Doctor saved: " + savedDoctor);
		if (newAbsence != null) {
			log.info("The Doctor has an Absence. Attempting to persist it");
			newAbsence.setDoctor(savedDoctor);
			newAbsence = absenceRepository.save(newAbsence);
			log.info("Absence saved: " + newAbsence);
			newDoctor.setAbsence(newAbsence);
		}
		
		return doctorAssembler.toModel(savedDoctor);
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
	@GetMapping("/{doctorId}")
	public EntityModel<Doctor> getDoctor(@PathVariable Long doctorId) {
		log.info("Request received: looking for Doctor with id " + doctorId);
		Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new DoctorNotFoundException(doctorId));
		return doctorAssembler.toModel(doctor);
	}
	
	/**
	 * Handle the request to update the information of a {@link Doctor}
	 * 
	 * Note: Every attribute of the {@link Doctor} is updated, even the {@link Absence}.
	 * If the {@link Absence} is missing, it will be deleted (if it exists)
	 *  
	 * @param doctorId The id of the {@link Doctor} to update
	 * @param newDoctor The values of newDoctor will be used to change the current {@link Doctor}
	 * @return The {@link Doctor} that has been persisted
	 */
	@PutMapping("/{doctorId}")
	public EntityModel<Doctor> updateDoctor(@PathVariable Long doctorId, @RequestBody @Valid Doctor newDoctor) {
		log.info("Request received: update Doctor with id: " + doctorId + " to be: " + newDoctor);
		if (!doctorRepository.findById(doctorId).isPresent()) {
			log.info("The selected doctorId was not found. Throwing DoctorNotFoundException");
			throw new DoctorNotFoundException(doctorId);
		}
		// The Doctor cannot already exist
		String firstName = newDoctor.getFirstName();
		String lastNames = newDoctor.getLastNames();
		if (doctorRepository.findByFirstNameAndLastNames(firstName, lastNames).isPresent()) {
			log.info("The provided Doctor already exists. Throwing DoctorAlreadyExistsException");
			throw new DoctorAlreadyExistsException(firstName, lastNames);
		}
		
		// The Absence is kept separately as it has to be persisted after the Doctor
		Absence newAbsence = newDoctor.getAbsence();
		newDoctor.setAbsence(null);

		// The Absence has to be valid
		if (newAbsence != null) {
			Set<ConstraintViolation<Absence>> violations = validator.validate(newAbsence);
			if (!violations.isEmpty()) {
				log.info("The Doctor has an invalid Absence. Throwing InvalidAbsenceException");
				throw new InvalidAbsenceException(violations);
			}
		}
		
		log.info("Attemting to persist the Doctor");
		newDoctor.setId(doctorId);
		Doctor savedDoctor = doctorRepository.save(newDoctor);
		log.info("Doctor persisted: " + savedDoctor);
		
		if (newAbsence == null) {
			log.info("The provided Absence is null, so trying to delete the Doctor's Absence if it exists");
			if (absenceRepository.findById(doctorId).isPresent()) {
				absenceRepository.deleteById(doctorId);
			}
		} else {
			log.info("An Absence was provided. Trying to persist it");
			newAbsence.setDoctor(savedDoctor);
			Absence savedAbsence = absenceRepository.save(newAbsence);
			log.info("Absence persisted: " + savedAbsence);
			savedDoctor.setAbsence(savedAbsence);
		}
		
		return doctorAssembler.toModel(savedDoctor);
	}
}

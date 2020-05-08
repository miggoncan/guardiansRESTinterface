package guardians.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import guardians.controllers.assemblers.DoctorAssembler;
import guardians.controllers.exceptions.DoctorAlreadyExistsException;
import guardians.controllers.exceptions.DoctorDeletedException;
import guardians.controllers.exceptions.DoctorNotFoundException;
import guardians.controllers.exceptions.InvalidAbsenceException;
import guardians.controllers.exceptions.InvalidEntityException;
import guardians.model.entities.Absence;
import guardians.model.entities.Doctor;
import guardians.model.entities.ShiftConfiguration;
import guardians.model.entities.Doctor.DoctorStatus;
import guardians.model.repositories.AbsenceRepository;
import guardians.model.repositories.DoctorRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * The DoctorController will handle all requests related to the {@link Doctor}
 * themselves, but not their {@link ShiftConfiguration}
 * 
 * @author miggoncan
 */
@Slf4j
@RestController
@RequestMapping("/guardians/doctors")
public class DoctorController {
	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private AbsenceRepository absenceRepository;

	@Autowired
	private DoctorAssembler doctorAssembler;

	@Autowired
	private Validator validator;

	/**
	 * Handle requests for all the existing {@link Doctor} in the database. Only the
	 * {@link Doctor} entities themselves will be returned. To get their shift
	 * configuration
	 * 
	 * @param email An optional email used to query for a {@link Doctor}
	 * 
	 * @see ShiftConfigurationController#getShitfConfigurations()
	 * 
	 * @return The existing doctors
	 */
	@GetMapping("")
	public CollectionModel<EntityModel<Doctor>> getDoctors(@RequestParam(required = false) Optional<String> email) {
		// TODO validate email?
		List<Doctor> doctors;
		if (email.isPresent()) {
			log.info("Request received: return the doctor with email: " + email);

			Doctor doctor = doctorRepository.findByEmail(email.get()).orElseThrow(() -> {
				log.info("The email could not be found. Thorwing DoctorNotFoundException");
				return new DoctorNotFoundException(email.get());
			});
			doctors = Arrays.asList(doctor);
		} else {
			log.info("Request received: returning all available doctors");
			doctors = doctorRepository.findAll();
		}

		return doctorAssembler.toCollectionModel(doctors);
	}

	/**
	 * Handle the creation of a new {@link Doctor}
	 * 
	 * The {@link Doctor} cannot already exist. Moreover this {@link Doctor} might
	 * have an {@link Absence}, and both have to be valid
	 * 
	 * @param newDoctor The {@link Doctor} that will be persisted
	 * @param startDateStr The date this {@link Doctor} will have their first
	 *                  cycle-shift. E.g. 2020-06-26
	 * @return The created {@link Doctor} (including the assigned id)
	 * @throws {@link DoctorAlreadyExistsException}
	 * @throws {@link InvalidAbsenceException}
	 */
	@PostMapping("")
	public EntityModel<Doctor> newDoctor(@RequestBody @Valid Doctor newDoctor, @RequestParam(name = "startDate") String startDateStr) {
		log.info("Request received: trying to create a new Doctor: " + newDoctor + " to start on " + startDateStr);
		
		LocalDate startDate;
		try {
			startDate = LocalDate.parse(startDateStr);
		} catch (DateTimeParseException e) {
			log.info("The provided startDate is not in a valid format. Throwing InvalidEntityException");
			throw new InvalidEntityException("The startDate has to be in a valid format. E.g. 2020-06-26");
		}
		
		// The email cannot already be registered
		String email = newDoctor.getEmail();
		if (doctorRepository.findByEmail(email).isPresent()) {
			log.info("The provided Doctor already exists. Throwing DoctorAlreadyExistsException");
			throw new DoctorAlreadyExistsException(email);
		}

		// The Absence has to be valid
		Absence newAbsence = newDoctor.getAbsence();
		if (newAbsence != null) {
			Set<ConstraintViolation<Absence>> violations = validator.validate(newAbsence);
			if (!violations.isEmpty()) {
				log.info("The Doctor has an invalid Absence. Throwing InvalidAbsenceException");
				throw new InvalidAbsenceException(violations);
			}
		}

		newDoctor.setStartDate(startDate);

		log.info("Validation complete. Attempting to save Doctor");
		// Persist the Doctor and its Absence
		Doctor savedDoctor = doctorRepository.save(newDoctor);
		log.info("Doctor saved: " + savedDoctor);

		return doctorAssembler.toModel(savedDoctor);
	}

	/**
	 * Handle the request for the information related to a single {@link Doctor},
	 * provided their id
	 * 
	 * @param doctorId The unique identifier used to search for the {@link Doctor}
	 * @return The {@link Doctor} found
	 * @throws DoctorNotFoundException if the {@link Doctor} was not found in the
	 *                                 database
	 */
	@GetMapping("/{doctorId}")
	public EntityModel<Doctor> getDoctor(@PathVariable Long doctorId) {
		log.info("Request received: looking for Doctor with id " + doctorId);
		Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> {
			log.info("The requested doctor could not be found. Throwing DoctorNotFoundException");
			return new DoctorNotFoundException(doctorId);
		});
		return doctorAssembler.toModel(doctor);
	}

	/**
	 * Handle the request to update the information of a {@link Doctor}
	 * 
	 * Note: Every attribute of the {@link Doctor} is updated, even the
	 * {@link Absence}. If the {@link Absence} is missing, it will be deleted (if it
	 * exists)
	 * 
	 * @param doctorId  The id of the {@link Doctor} to update
	 * @param newDoctor The values of newDoctor will be used to change the current
	 *                  {@link Doctor}
	 * @return The {@link Doctor} that has been persisted
	 */
	@PutMapping("/{doctorId}")
	public EntityModel<Doctor> updateDoctor(@PathVariable Long doctorId, @RequestBody @Valid Doctor newDoctor) {
		log.info("Request received: update Doctor with id: " + doctorId + " to be: " + newDoctor);
		Optional<Doctor> doctor = doctorRepository.findById(doctorId);
		if (!doctor.isPresent()) {
			log.info("The selected doctorId was not found. Throwing DoctorNotFoundException");
			throw new DoctorNotFoundException(doctorId);
		}

		if (doctor.get().getStatus() == DoctorStatus.DELETED) {
			log.info("The selected Doctor is deleted, so it cannot be modified. Throwing DoctorDeletedException");
			throw new DoctorDeletedException(doctorId);
		}
		// The email cannot already be registered
		String email = newDoctor.getEmail();
		Optional<Doctor> existantDoctor = doctorRepository.findByEmail(email); 
		if (existantDoctor.isPresent() && existantDoctor.get().getId() != doctorId) {
			log.info("A doctor already has the provided email. Throwing DoctorAlreadyExistsException");
			throw new DoctorAlreadyExistsException(email);
		}
		newDoctor.setId(doctorId);

		// The Absence has to be valid
		Absence newAbsence = newDoctor.getAbsence();
		if (newAbsence != null) {
			newAbsence.setDoctor(newDoctor);
			Set<ConstraintViolation<Absence>> violations = validator.validate(newAbsence);
			if (!violations.isEmpty()) {
				log.info("The Doctor has an invalid Absence. Throwing InvalidAbsenceException");
				throw new InvalidAbsenceException(violations);
			}
			log.info("The received Absence is valid");
		} else {
			log.info("The provided Absence is null: trying to delete the Doctor's Absence if it exists");
			// TODO the absence is not correctly deleted
			if (absenceRepository.findById(doctorId).isPresent()) {
				absenceRepository.deleteById(doctorId);
			}
		}

		log.info("Attemting to persist the Doctor");
		Doctor savedDoctor = doctorRepository.save(newDoctor);
		log.info("Doctor persisted: " + savedDoctor);

		return doctorAssembler.toModel(savedDoctor);
	}

	/**
	 * Handle the request to delete a {@link Doctor}, provided its id
	 * 
	 * @param doctorId The unique identifier used to search for the {@link Doctor}
	 * @throws DoctorNotFoundException
	 */
	@DeleteMapping("/{doctorId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Doctor> deleteDoctor(@PathVariable Long doctorId) {
		log.info("Request received: delete doctor with id " + doctorId);
		Optional<Doctor> doctor = doctorRepository.findById(doctorId);
		if (!doctor.isPresent()) {
			log.info("The doctor could not be found. Throwing DoctorNotFoundException");
			throw new DoctorNotFoundException(doctorId);
		}
		doctor.get().setStatus(DoctorStatus.DELETED);
		doctorRepository.save(doctor.get());
		log.info("The doctor was successfully marked as deleted");
		return ResponseEntity.noContent().build();
	}
}

package guardians.controllers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import guardians.controllers.assemblers.CycleShiftAssembler;
import guardians.controllers.exceptions.CycleShiftNotFoundException;
import guardians.controllers.exceptions.InvalidEntityException;
import guardians.model.entities.CycleShift;
import guardians.model.entities.primarykeys.DayMonthYearPK;
import guardians.model.repositories.CycleShiftRepository;
import lombok.extern.slf4j.Slf4j;

// TODO test CycleShiftController

/**
 * This class will handle all requests related to {@link CycleShift}s
 * 
 * @author miggoncan
 */
@RestController
@RequestMapping("/cycle-shifts")
@Slf4j
public class CycleShiftsController {
	@Autowired
	private CycleShiftRepository cycleShiftRepository;

	@Autowired
	private CycleShiftAssembler cycleShiftAssembler;

	@Autowired
	private Validator validator;

	/**
	 * This method will handle requests for all the available {@link CycleShift}s
	 * 
	 * @return The found {@link CycleShift}s in the database
	 */
	@GetMapping("")
	public CollectionModel<EntityModel<CycleShift>> getCycleShifts() {
		log.info("Request received: returning all available cycleShifts");
		return cycleShiftAssembler.toCollectionModel(cycleShiftRepository.findAll());
	}

	/**
	 * This method will handle requests to update all the {@link CycleShift}s This
	 * is, eliminating the current ones and replacing them with the provided ones.
	 * 
	 * Only update the cycle shifts if they are valid. This is, not just being
	 * validated by the @Valid annotation, but all of the provided shifts have to
	 * have consecutive reference days. For example, if the smallest reference date
	 * provided is 15/4/2020 and there are 10 newCycleShifts, there has to exist a
	 * CycleShift with reference date 16/4/2020, 17/4/2020, 18/4/2020, ... 24/4/2020
	 * 
	 * @return The updated {@link CycleShift}s
	 */
	@PutMapping("")
	public CollectionModel<EntityModel<CycleShift>> putCycleShifts(
			@RequestBody @Valid List<CycleShift> newCycleShifts) {
		log.info("Request received: update the cycle shifts with: " + newCycleShifts);

		// To check if all the needed newCycleShifts have been provided, sort them, and
		// then make sure the reference date of the n-th CycleShift is one day larger
		// than the reference date of the n-1-th CycleShift
		Collections.sort(newCycleShifts);
		LocalDate prevDate = null;
		LocalDate currDate = null;
		Integer day;
		Integer month;
		Integer year;
		for (CycleShift cycleShift : newCycleShifts) {
			// First, validate the cycleShift (even though the @Valid annotation
			// documentation explicitly says that the validation behavior is applied
			// recursively, it doesn't seem to validate each CycleShift
			Set<ConstraintViolation<CycleShift>> constraintViolations = validator.validate(cycleShift);
			if (!constraintViolations.isEmpty()) {
				log.info("At least, one of the received cycle shifts is invalid: " + constraintViolations);
				throw new ConstraintViolationException(constraintViolations);
			}
			
			prevDate = currDate;

			day = cycleShift.getDay();
			month = cycleShift.getMonth();
			year = cycleShift.getYear();
			currDate = LocalDate.of(year, month, day);

			log.debug("The previous date is: " + prevDate + " the current date is: " + currDate);
			if (prevDate != null && !prevDate.plusDays(1).equals(currDate)) {
				log.info("Not all reference dates where found. Throwing InvalidEntityException");
				throw new InvalidEntityException("All the reference dates within a shift cycle have to be consecutive. "
						+ "Missing the cycle shift for " + prevDate.plusDays(1));
			}
		}

		log.info("Deleting all previous cycle shifts");
		cycleShiftRepository.deleteAllInBatch();

		List<CycleShift> savedCycleShifts = cycleShiftRepository.saveAll(newCycleShifts);
		log.info("The newly persisted cycle shifts are: " + savedCycleShifts);

		return cycleShiftAssembler.toCollectionModel(savedCycleShifts);
	}

	/**
	 * This method will handle get requests of a single {@link CycleShift},
	 * identified by its reference dates
	 * 
	 * @param referenceDate the date used to identify the {@link CycleShift}
	 * @return The found {@link CycleShift}
	 * @throws CycleShiftNotFoundException whenever the requested {@link CycleShift}
	 *                                     could no be found
	 */
	@GetMapping("/{referenceDate}")
	public EntityModel<CycleShift> getCycleShift(
			@PathVariable @DateTimeFormat(iso = ISO.DATE) LocalDate referenceDate) {
		log.info("Request received: get cycle shift with reference date: " + referenceDate);
		Integer day = referenceDate.getDayOfMonth();
		Integer month = referenceDate.getMonthValue();
		Integer year = referenceDate.getYear();
		CycleShift cycleShift = cycleShiftRepository.findById(new DayMonthYearPK(day, month, year)).orElseThrow(() -> {
			return new CycleShiftNotFoundException(day, month, year);
		});
		return cycleShiftAssembler.toModel(cycleShift);
	}
}

package guardians.controllers;

import java.time.YearMonth;
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

import guardians.controllers.assemblers.CalendarAssembler;
import guardians.controllers.exceptions.CalendarAlreadyExistsException;
import guardians.controllers.exceptions.CalendarNotFoundException;
import guardians.controllers.exceptions.InvalidDayConfigurationException;
import guardians.model.entities.Calendar;
import guardians.model.entities.DayConfiguration;
import guardians.model.entities.primarykeys.CalendarPK;
import guardians.model.repositories.CalendarRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * The CalendarController will handle all requests related to {@link Calendar}
 * 
 * @author miggoncan
 */
@RestController
@RequestMapping("/guardians/calendars")
@Slf4j
public class CalendarController {
	@Autowired
	private CalendarRepository calendarRepository;

	@Autowired
	private CalendarAssembler calendarAssembler;
	
	@Autowired
	private Validator validator;

	/**
	 * This method will handle requests for the whole {@link Calendar} list
	 * 
	 * @return The calendars found in the database
	 */
	@GetMapping("")
	public CollectionModel<EntityModel<Calendar>> getCalendars() {
		log.info("Request received: returning all available calendars");
		return calendarAssembler.toCollectionModel(calendarRepository.findAll());
	}

	/**
	 * This method will handle requests to create a new {@link Calendar}
	 * 
	 * @param newCalendar the calendar to be created
	 * @return The created {@link Calendar}
	 * @throws CalendarAlreadyExistsException if a calendar already exists for the
	 *                                        given month and year
	 */
	@PostMapping("")
	public EntityModel<Calendar> newCalendar(@RequestBody @Valid Calendar newCalendar) {
		log.info("Request received: create a new calendar: " + newCalendar);
		CalendarPK pk = new CalendarPK(newCalendar.getMonth(), newCalendar.getYear());
		if (calendarRepository.findById(pk).isPresent()) {
			log.info("The calendar already exists. Throwing CalendarAlreadyExistsException");
			throw new CalendarAlreadyExistsException(newCalendar.getMonth(), newCalendar.getYear());
		}

		Set<ConstraintViolation<DayConfiguration>> constraintViolations;
		for (DayConfiguration day : newCalendar.getDayConfigurations()) {
			constraintViolations = this.validator.validate(day);
			if (!constraintViolations.isEmpty()) {
				log.info("One of the given day configurations is not valid. Throwing InvalidDayConfigurationException");
				throw new InvalidDayConfigurationException(constraintViolations);
			}
		}

		log.info("Validation complete. Attempting to persist the calendar");
		Calendar savedCalendar = calendarRepository.save(newCalendar);
		log.info("The persisted calendar is: " + savedCalendar);

		return calendarAssembler.toModel(savedCalendar);
	}

	/**
	 * This method will handle request to a single {@link Calendar}
	 * 
	 * @param yearMonth The year and month of the calendar to get
	 * @return The found {@link Calendar}
	 * @throws CalendarNotFoundException if the {@link Calendar} was not found
	 */
	@GetMapping("/{yearMonth}")
	public EntityModel<Calendar> getCalendar(@PathVariable YearMonth yearMonth) {
		log.info("Request received: get calendar of " + yearMonth);
		CalendarPK pk = new CalendarPK(yearMonth.getMonthValue(), yearMonth.getYear());

		Calendar calendar = calendarRepository.findById(pk).orElseThrow(() -> {
			log.info("The requested calendar could not be found. Throwing CalendarNotFoundException");
			return new CalendarNotFoundException(yearMonth.getMonthValue(), yearMonth.getYear());
		});

		return calendarAssembler.toModel(calendar);
	}

	/**
	 * This method will handle requests to update an existing {@link Calendar}
	 * 
	 * @param yearMonth the year and month of the calendar to be updated
	 * @param calendar  The {@link Calendar} used to replace the previous one
	 * @return The persisted {@link Calendar}
	 * @throws CalendarNotFoundException if the {@link Calendar} doesn't already
	 *                                   exist
	 */
	@PutMapping("/{yearMonth}")
	public EntityModel<Calendar> updateCalendar(@PathVariable YearMonth yearMonth,
			@RequestBody @Valid Calendar calendar) {
		log.info("Request received: update the calendar of " + yearMonth + " with :" + calendar);

		CalendarPK pk = new CalendarPK(yearMonth.getMonthValue(), yearMonth.getYear());
		if (!calendarRepository.findById(pk).isPresent()) {
			log.info("Could not find the requested calendar. Throwing CalendarNotFoundException");
			throw new CalendarNotFoundException(yearMonth.getMonthValue(), yearMonth.getYear());
		}
		// TODO validate calendar days

		Calendar savedCalendar = calendarRepository.save(calendar);
		return calendarAssembler.toModel(savedCalendar);
	}
}

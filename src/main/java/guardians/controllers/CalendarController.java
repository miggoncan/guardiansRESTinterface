package guardians.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import guardians.model.entities.Calendar;
import guardians.model.repositories.CalendarRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * The CalendarController will handle all requests related to {@link Calendar}
 * 
 * @author miggoncan
 */
@RestController
@RequestMapping("/calendars")
@Slf4j
public class CalendarController {
	@Autowired
	CalendarRepository calendarRepository;

	/**
	 * This method will handle requests for the whole {@link Calendar} list
	 * 
	 * @return The calendars found in the database
	 */
	@GetMapping("")
	public List<Calendar> getCalendars() {
		log.info("Request received: returning all available calendars");
		return calendarRepository.findAll();
	}
}

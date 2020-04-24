package es.us.alumn.miggoncan2.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import es.us.alumn.miggoncan2.model.entities.Calendar;
import es.us.alumn.miggoncan2.model.entities.Schedule;
import es.us.alumn.miggoncan2.model.repositories.CalendarRepository;

/**
 * The CalendarController will handle all requests related to {@link Calendar}
 * and {@link Schedule}
 * 
 * @author miggoncan
 */
@Controller
public class CalendarController {
	@Autowired
	CalendarRepository calendarRepository;

	/**
	 * This method will handle requests for the whole {@link Calendar} list
	 * 
	 * @return The calendars found in the database
	 */
	@GetMapping("/calendarios")
	@ResponseBody
	public List<Calendar> getDoctors() {
		return calendarRepository.findAll();
	}
}

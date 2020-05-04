package guardians.controllers;

import java.time.YearMonth;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import guardians.controllers.exceptions.ScheduleNotFoundException;
import guardians.model.entities.Schedule;
import guardians.model.entities.Schedule.ScheduleStatus;
import guardians.model.entities.primarykeys.CalendarPK;
import guardians.model.repositories.CalendarRepository;
import guardians.model.repositories.ScheduleRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * This class will handle requests related to {@link Schedule}s and
 * {@link ScheduleStatus}
 * 
 * @author miggoncan
 */
@RestController
@RequestMapping("/calendars")
@Slf4j
public class ScheduleController {

	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@Autowired
	private CalendarRepository calendarRepository;

	@GetMapping("/{yearMonth}/schedule")
	public Schedule getSchedule(@PathVariable YearMonth yearMonth) {
		log.info("Request received: get schedule of " + yearMonth);
		CalendarPK pk = new CalendarPK(yearMonth.getMonthValue(), yearMonth.getYear());
		
		if (!calendarRepository.findById(pk).isPresent()) {
			log.info("The calendar of the given month was not found. Throwing ScheduleNotFoundException");
			throw new ScheduleNotFoundException(yearMonth.getMonthValue(), yearMonth.getYear()); 
		}
		
		Schedule schedule;
		Optional<Schedule> optSchedule = scheduleRepository.findById(pk);
		if (optSchedule.isPresent()) {
			schedule = optSchedule.get();
			log.info("The schedule found is: " + schedule);
		} else {
			log.info("The schedule has not already been created. Returning a new NOT_CREATED schedule");
			schedule = new Schedule(ScheduleStatus.NOT_CREATED);
			schedule.setYear(yearMonth.getYear());
			schedule.setMonth(yearMonth.getMonthValue());
		}
		
		return schedule;
	}
	
	// TODO map all requests
	// TODO assemble calendar resource
}

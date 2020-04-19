package es.us.alumn.miggoncan2.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import es.us.alumn.miggoncan2.model.entities.Calendar;
import es.us.alumn.miggoncan2.model.repositories.CalendarRepository;

@Controller
public class CalendarController {
	@Autowired
	CalendarRepository calendarRepository;
	
	@GetMapping("/calendarios")
	@ResponseBody
	public List<Calendar> getDoctors() {
		return calendarRepository.findAll();
	}
}

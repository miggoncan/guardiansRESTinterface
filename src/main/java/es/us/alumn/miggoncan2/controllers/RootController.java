package es.us.alumn.miggoncan2.controllers;

import java.util.Collections;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO Use the log in all classes

/**
 * This controller will receive GET requests to the root URI of this application
 * and respond with links to the existing resources
 * 
 * @author miggoncan
 */
@RestController
public class RootController {
	
	@Value("${api.links.doctors}")
	String doctorsLink;
	
	@Value("${api.links.shiftconfs}")
	String shiftConfsLink;
	
	@Value("${api.links.calendars}")
	String calendarsLink;

	/**
	 * Get requests to the base URL will return links to the main resources of the
	 * application
	 * 
	 * @return A CollectionModel containing only links
	 */
	@GetMapping("/")
	public CollectionModel<Object> getRootLinks() {
		return new CollectionModel<Object>(Collections.emptyList(),
				linkTo(methodOn(RootController.class).getRootLinks()).withSelfRel(),
				linkTo(methodOn(DoctorController.class).getDoctors()).withRel(doctorsLink),
				linkTo(methodOn(ShiftConfigurationController.class).getShitfConfigurations()).withRel(shiftConfsLink),
				linkTo(methodOn(CalendarController.class).getCalendars()).withRel(calendarsLink));
	}

}

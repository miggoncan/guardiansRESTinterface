package guardians.controllers;

import java.util.Collections;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * This controller will receive GET requests to the root URI of this application
 * and respond with links to the existing resources
 * 
 * @author miggoncan
 */
@RestController
@Slf4j
@RequestMapping("/guardians")
public class RootController {
	
	@Value("${api.links.doctors}")
	private String doctorsLink;
	
	@Value("${api.links.shiftconfs}")
	private String shiftConfsLink;
	
	@Value("${api.links.calendars}")
	private String calendarsLink;
	
	@Value("${api.links.cycleshifts}")
	private String cycleShiftsLink;

	/**
	 * Get requests to the base URL will return links to the main resources of the
	 * application
	 * 
	 * @return A CollectionModel containing only links
	 */
	@GetMapping("/")
	public CollectionModel<Object> getRootLinks() {
		log.info("Request received: get root");
		return new CollectionModel<Object>(Collections.emptyList(),
				linkTo(methodOn(RootController.class).getRootLinks()).withSelfRel(),
				linkTo(methodOn(DoctorController.class).getDoctors(null)).withRel(doctorsLink),
				linkTo(methodOn(ShiftConfigurationController.class).getShitfConfigurations()).withRel(shiftConfsLink),
				linkTo(methodOn(CalendarController.class).getCalendars()).withRel(calendarsLink));
	}

}

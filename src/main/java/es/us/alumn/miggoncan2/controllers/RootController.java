package es.us.alumn.miggoncan2.controllers;

import java.util.Collections;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// TODO Use the log in all classes
// TODO Changes URIs to English

/**
 * This controller will receive GET requests to the root URI of this application
 * and respond with links to the existing resources
 * 
 * @author miggoncan
 */
@Controller
public class RootController {

	/**
	 * Get requests to the base URL will return links to the main resources of the
	 * application
	 * 
	 * @return A CollectionModel containing only links
	 */
	@GetMapping("/")
	@ResponseBody
	public CollectionModel<Object> getRootLinks() {
		return new CollectionModel<Object>(Collections.emptyList(),
				linkTo(methodOn(RootController.class).getRootLinks()).withSelfRel(),
				linkTo(methodOn(DoctorController.class).getDoctors()).withRel("facultativos"),
				linkTo(methodOn(ShiftConfigurationController.class).getShitfConfigurations()).withRel("config-cas"));
	}

}

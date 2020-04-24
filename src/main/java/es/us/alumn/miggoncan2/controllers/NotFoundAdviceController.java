package es.us.alumn.miggoncan2.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.us.alumn.miggoncan2.controllers.exceptions.NotFoundException;

/**
 * This Controller will catch the thrown {@link NotFoundException} and respond
 * with an HTTP not found message
 * 
 * @author miggoncan
 */
@ControllerAdvice
public class NotFoundAdviceController {

	/**
	 * This method catches all exceptions extending {@link NotFoundException}
	 * 
	 * @param e The caught exception
	 * @return The String that should be returned in the HTTP response body
	 */
	@ResponseBody
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String notFoundHandler(NotFoundException e) {
		return e.getMessage();
	}
}

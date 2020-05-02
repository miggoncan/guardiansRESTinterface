package guardians.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import guardians.controllers.exceptions.AlreadyExistsException;
import guardians.controllers.exceptions.DoctorDeletedException;
import guardians.controllers.exceptions.InvalidEntityException;
import guardians.controllers.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * This Controller will catch the thrown {@link RuntimeException} declared in
 * the package controllers.exceptions thrown will handling requests
 * 
 * @author miggoncan
 */
@RestControllerAdvice
@Slf4j
public class MyAdviceController {

	/**
	 * This method catches all exceptions extending {@link NotFoundException}
	 * 
	 * @param e The caught exception
	 * @return The String that should be returned in the HTTP response body
	 */
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String notFoundHandler(NotFoundException e) {
		log.info("Cauth NotFoundException: " + e.getMessage());
		return e.getMessage();
	}

	/**
	 * This method catches all exceptions extending {@link AlreadyExistsException}
	 * 
	 * @param e The caught exception
	 * @return The String that should be returned in the HTTP response body
	 */
	@ExceptionHandler(AlreadyExistsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String alreadyExistsHandler(AlreadyExistsException e) {
		log.info("Cauth AlreadyExistsException: " + e.getMessage());
		return e.getMessage();
	}

	/**
	 * This method catches all exceptions extending {@link InvalidEntityException}
	 * 
	 * @param e The caught exception
	 * @return The String that should be returned in the HTTP response body
	 */
	@ExceptionHandler(InvalidEntityException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String invalidEntityHandler(InvalidEntityException e) {
		log.info("Cauth InvalidEntityException: " + e.getMessage());
		return e.getMessage();
	}
	
	/**
	 * This method catches the exception {@link DoctorDeletedException}
	 * 
	 * @param e The caught exception
	 * @return The String that should be returned in the HTTP response body
	 */
	@ExceptionHandler(DoctorDeletedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	String doctorDeleterHandler(DoctorDeletedException e) {
		log.info("Cauth DoctorDeletedException: " + e.getMessage());
		return e.getMessage();
	}
}
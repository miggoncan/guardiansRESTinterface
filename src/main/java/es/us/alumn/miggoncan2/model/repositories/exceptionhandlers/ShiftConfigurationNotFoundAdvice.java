package es.us.alumn.miggoncan2.model.repositories.exceptionhandlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.us.alumn.miggoncan2.model.repositories.exceptions.ShiftConfigurationNotFoundException;

@ControllerAdvice
public class ShiftConfigurationNotFoundAdvice {

		@ResponseBody
		@ExceptionHandler(ShiftConfigurationNotFoundException.class)
		@ResponseStatus(HttpStatus.NOT_FOUND)
		String shiftConfigurationNotFoundHandler(ShiftConfigurationNotFoundException e) {
			return e.getMessage();
		}
}

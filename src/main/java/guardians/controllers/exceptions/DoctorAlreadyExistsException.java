package es.us.alumn.miggoncan2.controllers.exceptions;

import es.us.alumn.miggoncan2.model.entities.Doctor;

/**
 * This exception will be thrown whenever a new {@link Doctor} is to be created,
 * but it already exists in the database
 * 
 * @author miggoncan
 */
public class DoctorAlreadyExistsException extends AlreadyExistsException {
	private static final long serialVersionUID = -6150817314131506857L;

	public DoctorAlreadyExistsException(String firstName, String lastNames) {
		super("The Doctor " + firstName + " " + lastNames + " already exists");
	}
}

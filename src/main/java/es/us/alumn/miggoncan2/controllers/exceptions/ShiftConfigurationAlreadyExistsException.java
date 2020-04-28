package es.us.alumn.miggoncan2.controllers.exceptions;

import es.us.alumn.miggoncan2.model.entities.Doctor;
import es.us.alumn.miggoncan2.model.entities.ShiftConfiguration;

/**
 * This exception will be thrown whenever a new {@link ShiftConfiguration} for a
 * {@link Doctor} is to be created, but this {@link Doctor} already has one
 * 
 * @author miggoncan
 */
public class ShiftConfigurationAlreadyExistsException extends AlreadyExistsException {
	private static final long serialVersionUID = -4980741181315896511L;

	public ShiftConfigurationAlreadyExistsException(Long doctorId) {
		super("The shift configuration for doctor " + doctorId + " already exists");
	}
}

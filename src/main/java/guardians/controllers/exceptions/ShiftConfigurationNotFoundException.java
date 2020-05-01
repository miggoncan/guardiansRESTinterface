package es.us.alumn.miggoncan2.controllers.exceptions;

import es.us.alumn.miggoncan2.model.entities.ShiftConfiguration;

/**
 * This class represents the exception that will be thrown when a requested
 * {@link ShiftConfiguration} is not found.
 * 
 * @see NotFoundException
 * @author miggoncan
 *
 */
public class ShiftConfigurationNotFoundException extends NotFoundException {
	private static final long serialVersionUID = 5699058085592740925L;

	/**
	 * @param doctorId The provided id that could not be matched to an existing
	 *                 {@link ShiftConfiguration}
	 */
	public ShiftConfigurationNotFoundException(Long doctorId) {
		super("Could not find the shift configuration for doctor " + doctorId);
	}
}

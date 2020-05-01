package es.us.alumn.miggoncan2.controllers.exceptions;

import es.us.alumn.miggoncan2.model.entities.Doctor;

/**
 * This class represents the exception that will be thrown when a requested
 * {@link Doctor} is not found.
 * 
 * @see NotFoundException
 * 
 * @author miggoncan
 */
public class DoctorNotFoundException extends NotFoundException {
	private static final long serialVersionUID = 6757222623745324542L;

	/**
	 * @param doctorId The provided id that could not be matched to an existing
	 *                 {@link Doctor}
	 */
	public DoctorNotFoundException(Long doctorId) {
		super("Could not find the doctor " + doctorId);
	}
}

package guardians.controllers.exceptions;

import guardians.model.entities.CycleShift;

/**
 * This class represents the exception that will be thrown whenever a
 * {@link CycleShift} cannot be found in the database
 * 
 * @author miggoncan
 */
public class CycleShiftNotFoundException extends NotFoundException {
	private static final long serialVersionUID = 5794992080062360800L;

	public CycleShiftNotFoundException(Integer day, Integer month, Integer year) {
		super("The cycle shift with reference date: " + day + "/" + month + "/" + year + " was not found");
	}
}

package es.us.alumn.miggoncan2.model.repositories.exceptions;

public class ShiftConfigurationNotFoundException  extends RuntimeException {
	
	public ShiftConfigurationNotFoundException(Long doctorId) {
		super("Could not find the shift configuration for doctor " + doctorId);
	}
}

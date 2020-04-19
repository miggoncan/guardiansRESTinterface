package es.us.alumn.miggoncan2.model.repositories.exceptions;

public class ShiftConfigurationNotFoundException  extends RuntimeException {
	private static final long serialVersionUID = 5699058085592740925L;

	public ShiftConfigurationNotFoundException(Long doctorId) {
		super("Could not find the shift configuration for doctor " + doctorId);
	}
}

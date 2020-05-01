package guardians.controllers.exceptions;

import guardians.model.entities.Calendar;
import guardians.model.entities.Schedule;

/**
 * This class represents the exception that will be thrown whenever the
 * {@link Calendar} related to a {@link Schedule} is not found in the database
 * 
 * @see NotFoundException
 * 
 * @author miggoncan
 */
public class ScheduleNotFoundException extends NotFoundException {
	private static final long serialVersionUID = 7293574842299023541L;

	public ScheduleNotFoundException(Integer month, Integer year) {
		super("The schedule for " + month + "-" + year + " could no be found");
	}
}

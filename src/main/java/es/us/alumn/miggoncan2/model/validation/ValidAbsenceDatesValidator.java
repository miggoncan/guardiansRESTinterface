package es.us.alumn.miggoncan2.model.validation;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import es.us.alumn.miggoncan2.model.entities.Absence;

/**
 * This class is used to validate the {@link Absence}s annotated with
 * {@link ValidAbsenceDates}
 * 
 * It checks that the start date is before the end date. A null Absence is also
 * considered to be valid
 * 
 * @author miggoncan
 */
public class ValidAbsenceDatesValidator implements ConstraintValidator<ValidAbsenceDates, Absence> {
	@Override
	public boolean isValid(Absence value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		Date start = value.getStart();
		Date end = value.getEnd();
		return start != null && end != null && start.before(end);
	}
}

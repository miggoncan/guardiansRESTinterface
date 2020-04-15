package es.us.alumn.miggoncan2.model.entityvalidation;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import es.us.alumn.miggoncan2.model.entities.Absence;

public class ValidAbsenceDatesValidator implements ConstraintValidator<ValidAbsenceDates, Absence> {
	@Override
	public boolean isValid(Absence value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		Date start = value.getStart();
		Date end = value.getEnd();
		return start != null && end != null&& start.before(end); 
	}
}

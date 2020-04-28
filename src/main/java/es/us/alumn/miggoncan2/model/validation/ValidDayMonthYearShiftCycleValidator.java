package es.us.alumn.miggoncan2.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import es.us.alumn.miggoncan2.model.entities.ShiftCycle;

/**
 * This class will apply the validation done by
 * {@link ValidDayMonthYearValidator} to a {@link ShiftCycle}
 * 
 * @author miggoncan
 * @see ValidDayMonthYearValidator
 */
public class ValidDayMonthYearShiftCycleValidator extends ValidDayMonthYearValidator
		implements ConstraintValidator<ValidDayMonthYear, ShiftCycle> {
	@Override
	public boolean isValid(ShiftCycle value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		
		return this.isValid(value.getDay(), value.getMonth(), value.getYear());
	}

}

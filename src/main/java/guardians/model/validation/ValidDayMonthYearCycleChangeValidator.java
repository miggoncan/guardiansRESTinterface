package es.us.alumn.miggoncan2.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import es.us.alumn.miggoncan2.model.entities.CycleChange;

/**
 * This class will apply the validation done by
 * {@link ValidDayMonthYearValidator} to a {@link CycleChange}
 * 
 * @author miggoncan
 * @see ValidDayMonthYearValidator
 */
public class ValidDayMonthYearCycleChangeValidator extends ValidDayMonthYearValidator
		implements ConstraintValidator<ValidDayMonthYear, CycleChange> {
	@Override
	public boolean isValid(CycleChange value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		
		return this.isValid(value.getDay(), value.getMonth(), value.getYear());
	}

}

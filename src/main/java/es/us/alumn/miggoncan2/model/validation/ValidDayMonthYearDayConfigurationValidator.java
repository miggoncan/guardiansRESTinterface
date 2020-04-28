package es.us.alumn.miggoncan2.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import es.us.alumn.miggoncan2.model.entities.DayConfiguration;

/**
 * This class will apply the validation done by
 * {@link ValidDayMonthYearValidator} to a {@link DayConfiguration}
 * 
 * @author miggoncan
 * @see ValidDayMonthYearValidator
 */
public class ValidDayMonthYearDayConfigurationValidator extends ValidDayMonthYearValidator
		implements ConstraintValidator<ValidDayMonthYear, DayConfiguration> {
	@Override
	public boolean isValid(DayConfiguration value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}

		return this.isValid(value.getDay(), value.getMonth(), value.getYear());
	}

}

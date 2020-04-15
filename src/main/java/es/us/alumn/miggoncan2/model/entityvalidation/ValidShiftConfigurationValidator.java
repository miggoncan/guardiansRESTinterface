package es.us.alumn.miggoncan2.model.entityvalidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import es.us.alumn.miggoncan2.model.entities.ShiftConfiguration;

public class ValidShiftConfigurationValidator 
	implements ConstraintValidator<ValidShiftConfiguration, ShiftConfiguration> {
	@Override
	public boolean isValid(ShiftConfiguration value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		Integer min = value.getMinShifts();
		Integer max = value.getMaxShifts();
		return min != null && max != null && min <= max;
	}
}

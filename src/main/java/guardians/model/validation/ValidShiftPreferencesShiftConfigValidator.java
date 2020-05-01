package guardians.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import guardians.model.entities.AllowedShift;
import guardians.model.entities.ShiftConfiguration;

/**
 * This validator applies the algorithm in
 * {@link ValidShiftPreferencesValidator} to the shift preferences of a
 * {@link ShiftConfiguration}
 * 
 * @author miggoncan
 */
public class ValidShiftPreferencesShiftConfigValidator
		implements ConstraintValidator<ValidShiftPreferences, ShiftConfiguration> {
	@Override
	public boolean isValid(ShiftConfiguration value, ConstraintValidatorContext context) {
		ValidShiftPreferencesValidator<AllowedShift> validator = new ValidShiftPreferencesValidator<>();
		return validator.isValid(value.getUnwantedShifts(), value.getUnavailableShifts(), value.getWantedShifts(),
				value.getMandatoryShifts());
	}
}

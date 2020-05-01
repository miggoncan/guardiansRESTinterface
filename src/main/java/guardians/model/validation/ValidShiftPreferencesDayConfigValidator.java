package guardians.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import guardians.model.entities.DayConfiguration;
import guardians.model.entities.Doctor;

/**
 * This validator applies the algorithm in
 * {@link ValidShiftPreferencesValidator} to the shift preferences of a
 * {@link DayConfiguration}
 * 
 * @author miggoncan
 */
public class ValidShiftPreferencesDayConfigValidator
		implements ConstraintValidator<ValidShiftPreferences, DayConfiguration> {
	@Override
	public boolean isValid(DayConfiguration value, ConstraintValidatorContext context) {
		ValidShiftPreferencesValidator<Doctor> validator = new ValidShiftPreferencesValidator<>();
		return validator.isValid(value.getUnwantedShifts(), value.getUnavailableShifts(), value.getWantedShifts(),
				value.getMandatoryShifts());
	}
}

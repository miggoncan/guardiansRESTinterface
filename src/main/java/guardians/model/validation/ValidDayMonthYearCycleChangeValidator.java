package guardians.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import guardians.model.entities.CycleChange;
import lombok.extern.slf4j.Slf4j;

/**
 * This class will apply the validation done by
 * {@link ValidDayMonthYearValidator} to a {@link CycleChange}
 * 
 * @author miggoncan
 * @see ValidDayMonthYearValidator
 */
@Slf4j
public class ValidDayMonthYearCycleChangeValidator extends ValidDayMonthYearValidator
		implements ConstraintValidator<ValidDayMonthYear, CycleChange> {
	@Override
	public boolean isValid(CycleChange value, ConstraintValidatorContext context) {
		log.debug("Request to validate the CycleChange: " + value);
		if (value == null) {
			log.debug("As the CycleChange is null, it is considered valid");
			return true;
		}
		
		Integer day = value.getDay();
		Integer month = value.getMonth();
		Integer year = value.getYear();
		if (day == null || month == null || year == null) {
			log.debug("Either the given day, month or year are false. The date is invalid");
			return false;
		}
		
		return this.isValid(day, month, year);
	}

}

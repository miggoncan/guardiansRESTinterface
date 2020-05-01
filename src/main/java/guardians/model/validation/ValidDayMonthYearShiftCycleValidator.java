package guardians.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import guardians.model.entities.ShiftCycle;
import lombok.extern.slf4j.Slf4j;

/**
 * This class will apply the validation done by
 * {@link ValidDayMonthYearValidator} to a {@link ShiftCycle}
 * 
 * @author miggoncan
 * @see ValidDayMonthYearValidator
 */
@Slf4j
public class ValidDayMonthYearShiftCycleValidator extends ValidDayMonthYearValidator
		implements ConstraintValidator<ValidDayMonthYear, ShiftCycle> {
	@Override
	public boolean isValid(ShiftCycle value, ConstraintValidatorContext context) {
		log.debug("Request to validate ShiftCycle: " + value);
		if (value == null) {
			log.debug("As the ShiftCycle is null, it is considered valid");
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

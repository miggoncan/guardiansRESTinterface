package guardians.model.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import guardians.model.entities.CycleShift;
import guardians.model.validation.annotations.ValidDayMonthYear;
import lombok.extern.slf4j.Slf4j;

/**
 * This class will apply the validation done by
 * {@link DayMonthYearValidator} to a {@link CycleShift}
 * 
 * @author miggoncan
 * @see DayMonthYearValidator
 */
@Slf4j
public class DayMonthYearShiftCycleValidator extends DayMonthYearValidator
		implements ConstraintValidator<ValidDayMonthYear, CycleShift> {
	@Override
	public boolean isValid(CycleShift value, ConstraintValidatorContext context) {
		log.debug("Request to validate CycleShift: " + value);
		if (value == null) {
			log.debug("As the CycleShift is null, it is considered valid");
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

package guardians.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import guardians.model.entities.ScheduleDay;
import lombok.extern.slf4j.Slf4j;

/**
 * This class will apply the validation done by
 * {@link ValidDayMonthYearValidator} to a {@link ScheduleDay}
 * 
 * @author miggoncan
 * @see ValidDayMonthYearValidator
 */
@Slf4j
public class ValidDayMonthYearScheduleDayValidator extends ValidDayMonthYearValidator
		implements ConstraintValidator<ValidDayMonthYear, ScheduleDay> {
	@Override
	public boolean isValid(ScheduleDay value, ConstraintValidatorContext context) {
		log.debug("Request to validate ScheduleDay: " + value);
		if (value == null) {
			log.debug("As the ScheduleDay is null, it is considered valid");
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

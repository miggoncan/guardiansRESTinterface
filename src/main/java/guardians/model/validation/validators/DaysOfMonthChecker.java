package guardians.model.validation.validators;

import java.time.YearMonth;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;

import guardians.model.entities.DayGetterInterface;
import lombok.extern.slf4j.Slf4j;

/**
 * This class will check that all days of a given {@link YearMonth} exist in a
 * given list of {@link DayGetterInterface}.
 * 
 * Moreover, it will make sure there are no duplicate days.
 * 
 * This is, for example, if we had a list of {@link DayGetterInterface} that
 * represent the days of April-2020, this class will check that all days from 1
 * to 30 exist in the list.
 * 
 * @author miggoncan
 */
@Slf4j
public class DaysOfMonthChecker<Day extends DayGetterInterface> {
	private YearMonth yearMonth;

	private Validator validator;

	public DaysOfMonthChecker(YearMonth yearMonth) {
		this.yearMonth = yearMonth;

		this.validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	public boolean areAllDaysPresent(List<Day> days) {
		log.debug("Request to validate days: " + days);
		if (days == null) {
			log.debug("The days list is null. The days are invalid");
			return false;
		}

		boolean isValid = true;

		int lengthOfMonth = this.yearMonth.lengthOfMonth();
		int numDays = days.size();

		if (lengthOfMonth != numDays) {
			log.debug("The month " + this.yearMonth + " has " + lengthOfMonth + " days. However, only " + numDays
					+ " days where provided. The days are invalid");
			isValid = false;
		} else if (days.parallelStream().anyMatch((day) -> { // If any of the days is invalid
			return day == null || !validator.validate(day).isEmpty();
		})) {
			log.debug("At least one of the provided days is invalid. The days are invalid");
			isValid = false;
		} else {
			// Sorting the days by the day number allows to easily check all days within the
			// month are present
			days.sort((day1, day2) -> {
				return day1.getDay() - day2.getDay();
			});
			log.debug("The days list after being sorted is: " + days);

			Day currentDay;
			for (int i = 0; i < lengthOfMonth; i++) {
				currentDay = days.get(i);
				// As soon as one of the required days is not present, the Schedule is invalid
				if (i + 1 != currentDay.getDay()) {
					log.debug("The " + (i + 1) + " day of " + this.yearMonth + " is missing. The days are invalid");
					isValid = false;
					break;
				}
			}
		}
		
		log.debug("The days given are valid: " + isValid);
		return isValid;
	}
}

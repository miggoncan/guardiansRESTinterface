package guardians.model.validation;

import java.time.DateTimeException;
import java.time.YearMonth;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import guardians.model.entities.Schedule;
import guardians.model.entities.ScheduleDay;
import guardians.model.entities.Schedule.ScheduleStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * This validator will make sure that if a {@link Schedule} is created, it will
 * have all the necessary days within the given month. However, if it is still
 * not created, it cannot have any {@link ScheduleDay}
 * 
 * @author miggoncan
 */
@Slf4j
public class ValidScheduleValidator implements ConstraintValidator<ValidSchedule, Schedule> {
	@Override
	public boolean isValid(Schedule value, ConstraintValidatorContext context) {
		log.debug("Request to validate Schedule: " + value);
		if (value == null) {
			log.debug("The given Schedule is null, which is considered a valid value");
			return true;
		}

		Boolean scheduleIsValid = null;

		Integer year = value.getYear();
		Integer month = value.getMonth();
		ScheduleStatus status = value.getStatus();

		YearMonth yearMonth = null;

		if (year == null || month == null || status == null) {
			log.debug("Either the month, year ot status are null, so the schedule is invalid");
			return false;
		} else {
			try {
				yearMonth = YearMonth.of(year, month);
			} catch (DateTimeException e) {
				log.debug("The year and month are not valid " + e.getMessage());
				return false;
			}
		}

		List<ScheduleDay> days = value.getDays();
		// If the schedule has not yet been created, it is allowed to have a null or
		// empty list of ScheduleDays
		if (status == ScheduleStatus.NOT_CREATED) {
			if (days == null || days.size() == 0) {
				log.debug("The schedule has not yet been created. It is valid");
				scheduleIsValid = true;
			} else {
				log.debug("The schedule is marked as not created, but it has some schedule days. It is invalid");
				scheduleIsValid = false;
			}
		// If the schedule has been created
		} else {
			int lengthOfMonth = yearMonth.lengthOfMonth();
			int numDays = days.size();

			if (lengthOfMonth != numDays) {
				log.debug("The month " + yearMonth + " has " + lengthOfMonth + " days. However, only " + numDays
						+ " days where provided. The schedule is invalid");
				scheduleIsValid = false;
			} else {
				// If the schedule has been created and has the number of needed days, sort them
				// by day number. This allows to easily check all days within the month are
				// present
				days.sort((day1, day2) -> {
					return day2.getDay() - day1.getDay();
				});
				log.debug("The ScheduleDay list after being sorted is: " + days);

				ScheduleDay currentDay;
				for (int i = 0; i < lengthOfMonth; i++) {
					currentDay = days.get(i);
					// As soon as one of the required days is not present, the Schedule is invalid
					if (i + 1 != currentDay.getDay()) {
						log.debug("The " + i + " day of " + yearMonth + " is missing. The schedule is invalid");
						scheduleIsValid = false;
						break;
					}
				}

				// If the schedule still hasn't been found invalid, it is valid
				scheduleIsValid = scheduleIsValid == null;
			}
		}

		log.debug("The schedule is valid: " + scheduleIsValid);
		return scheduleIsValid;
	}

}

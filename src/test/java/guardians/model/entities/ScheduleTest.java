package guardians.model.entities;

import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import guardians.model.entities.Schedule.ScheduleStatus;

@Testable
public class ScheduleTest {

	private static final String INVALID_SCHEDULE_MESSAGE = "If the schedule is created, it has to contain all days of the month. Otherwise, it cannot have any day";

	private EntityTester<Schedule> entityTester;

	public ScheduleTest() {
		this.entityTester = new EntityTester<>(Schedule.class);
	}

	public static Schedule createValidSchedule(YearMonth yearMonth, ScheduleStatus status) {
		Schedule schedule = new Schedule(status);
		schedule.setMonth(yearMonth.getMonthValue());
		schedule.setYear(yearMonth.getYear());

		if (status != ScheduleStatus.NOT_CREATED) {
			ScheduleDay scheduleDay;
			List<ScheduleDay> days = new LinkedList<>();
			for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
				scheduleDay = ScheduleDayTest.createValidScheduleDay(day);
				scheduleDay.setSchedule(schedule);
				days.add(scheduleDay);
			}
			schedule.setDays(days);
		}

		return schedule;
	}

	@Test
	void scheduleNotCreated() {
		YearMonth yearMonth = YearMonth.of(2020, 6);
		Schedule schedule = createValidSchedule(yearMonth, ScheduleStatus.NOT_CREATED);
		this.entityTester.assertValidEntity(schedule);
	}

	@Test
	void scheduleConfirmed() {
		YearMonth yearMonth = YearMonth.of(2020, 6);
		Schedule schedule = createValidSchedule(yearMonth, ScheduleStatus.CONFIRMED);
		this.entityTester.assertValidEntity(schedule);
	}
	
	@Test
	void scheduleNotCreatedAndWithDays() {
		YearMonth yearMonth = YearMonth.of(2020, 6);
		Schedule schedule = createValidSchedule(yearMonth, ScheduleStatus.CONFIRMED);
		schedule.setStatus(ScheduleStatus.NOT_CREATED);
		this.entityTester.assertEntityViolatedConstraint(schedule, INVALID_SCHEDULE_MESSAGE);
	}

	@Test
	void scheduleWithOneNullDay() {
		YearMonth yearMonth = YearMonth.of(2020, 6);
		Schedule schedule = createValidSchedule(yearMonth, ScheduleStatus.CONFIRMED);
		schedule.getDays().set(4, null);
		this.entityTester.assertEntityViolatedConstraint(schedule, INVALID_SCHEDULE_MESSAGE);
	}
	
	@Test
	void scheduleWithOneDuplicatedDay() {
		YearMonth yearMonth = YearMonth.of(2020, 6);
		Schedule schedule = createValidSchedule(yearMonth, ScheduleStatus.CONFIRMED);
		schedule.getDays().set(4, schedule.getDays().get(20));
		this.entityTester.assertEntityViolatedConstraint(schedule, INVALID_SCHEDULE_MESSAGE);
	}
	
	@Test
	void scheduleWithLessDaysThanNeeded() {
		YearMonth yearMonth = YearMonth.of(2020, 6);
		Schedule schedule = createValidSchedule(yearMonth, ScheduleStatus.CONFIRMED);
		schedule.getDays().remove(3);
		this.entityTester.assertEntityViolatedConstraint(schedule, INVALID_SCHEDULE_MESSAGE);
	}
}

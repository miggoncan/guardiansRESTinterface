package guardians.model.entities;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class ScheduleDayTest {

	private EntityTester<ScheduleDay> entityTester;
	
	public ScheduleDayTest() {
		this.entityTester = new EntityTester<>(ScheduleDay.class);
	}
	
	@Test
	void testDates() {
		ScheduleDay scheduleDay = new ScheduleDay();
		Set<Doctor> doctors = new HashSet<>();
		doctors.add(DoctorTest.createValidDoctor());
		scheduleDay.setIsWorkingDay(true);
		scheduleDay.setCycle(doctors);
		scheduleDay.setShifts(doctors);
		DateTester<ScheduleDay> dateTester = new DateTester<>(ScheduleDay.class);
		dateTester.testEntity(scheduleDay, (day, month, year) -> {
			scheduleDay.setDay(day);
			scheduleDay.setMonth(month);
			scheduleDay.setYear(year);
		});
	}
	
	////////////////////////////////////
	//
	// Test for valid values
	//
	////////////////////////////////////
	
	
	@Test
	void isWorkingDayCanBeTrue() {
		this.entityTester.assertValidValue("isWorkingDay", true);
	}
	
	@Test
	void isWorkingDayCanBeFalse() {
		this.entityTester.assertValidValue("isWorkingDay", false);
	}
	
	@Test
	void consultationsCanBeNull() {
		this.entityTester.assertValidValue("consultations", null);
	}
	
	@Test
	void consultationsCanBeEmpty() {
		this.entityTester.assertValidValue("consultations", Collections.emptySet());
	}
	
	@Test
	void validCycle() {
		Set<Doctor> doctors = new HashSet<>(DoctorTest.createValidDoctors());
		this.entityTester.assertValidValue("cycle", doctors);
	}
	
	@Test
	void validShifts() {
		Set<Doctor> doctors = new HashSet<>(DoctorTest.createValidDoctors());
		this.entityTester.assertValidValue("shifts", doctors);
	}
	
	@Test
	void validConsultations() {
		Set<Doctor> doctors = new HashSet<>(DoctorTest.createValidDoctors());
		this.entityTester.assertValidValue("consultations", doctors);
	}
	
	////////////////////////////////////
	//
	// Test for invalid values
	//
	////////////////////////////////////
	
	@Test
	void isWorkingDayCannotBeNull() {
		this.entityTester.assertAttributeCannotBeNull("isWorkingDay");
	}
	
	@Test
	void cycleCannotBeEmpty() {
		this.entityTester.assertAttributeCannotBeEmpty("cycle");
	}
	
	@Test
	void shiftsCannotBeEmpty() {
		this.entityTester.assertAttributeCannotBeEmpty("shifts");
	}
}

package es.us.alumn.miggoncan2.model.entities;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class DayConfigurationTests {
	private EntityTester<DayConfiguration> entityTester;
	
	public DayConfigurationTests() {
		this.entityTester = new EntityTester<>(DayConfiguration.class);
	}
	
	@Test
	void testDates() {
		DayConfiguration dayConfiguration = new DayConfiguration();
		dayConfiguration.setIsWorkingDay(true);
		dayConfiguration.setNumShifts(3);
		dayConfiguration.setNumConsultations(2);
		DateTester<DayConfiguration> dateTester = new DateTester<>(DayConfiguration.class);
		dateTester.testEntity(dayConfiguration, (day, month, year) -> {
			dayConfiguration.setDay(day);
			dayConfiguration.setMonth(month);
			dayConfiguration.setYear(year);
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
	void numShiftsCanBeZero() {
		this.entityTester.assertValidValue("numShifts", 0);
	}
	
	@Test
	void numShiftsCanBePositive() {
		this.entityTester.assertValidValue("numShifts", 3);
	}
	
	@Test 
	void numConsultationsCanBeZero() {
		this.entityTester.assertValidValue("numConsultations", 0);
	}
	
	@Test 
	void numConsultationsCanBePositive() {
		this.entityTester.assertValidValue("numConsultations", 2);
	}
	
	@Test
	void unwantedShiftsCanBeNull() {
		this.entityTester.assertValidValue("unwantedShifts", null);
	}
	
	@Test
	void unwantedShiftsCanBeEmpty() {
		this.entityTester.assertValidValue("unwantedShifts", new HashSet<>());
	}
	
	@Test
	void unavailableShiftsCanBeNull() {
		this.entityTester.assertValidValue("unavailableShifts", null);
	}
	
	@Test
	void unavailableShiftsCanBeEmpty() {
		this.entityTester.assertValidValue("unavailableShifts", new HashSet<>());
	}
	
	@Test
	void wantedShiftsCanBeNull() {
		this.entityTester.assertValidValue("wantedShifts", null);
	}
	
	@Test
	void wantedShiftsCanBeEmpty() {
		this.entityTester.assertValidValue("wantedShifts", new HashSet<>());
	}
	
	@Test
	void mandatoryShiftsCanBeNull() {
		this.entityTester.assertValidValue("mandatoryShifts", null);
	}
	
	@Test
	void mandatoryShiftsCanBeEmpty() {
		this.entityTester.assertValidValue("mandatoryShifts", new HashSet<>());
	}
	
	@Test
	void cycleChangesCanBeNull() {
		this.entityTester.assertValidValue("cycleChanges", null);
	}
	
	@Test
	void cycleChangesCanBeEmpty() {
		this.entityTester.assertValidValue("cycleChanges", new HashSet<>());
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
	void numShiftsCannotBeNull() {
		this.entityTester.assertAttributeCannotBeNull("numShifts");
	}
	
	@Test
	void numShiftsCannotBeNegative() {
		this.entityTester.assertInvalidValue("numShifts", -1, "must be greater than or equal to 0");
	}
	
	@Test
	void numConsultationsCannotBeNull() {
		this.entityTester.assertAttributeCannotBeNull("numConsultations");
	}
	
	@Test
	void numConsultationsCannotBeNegative() {
		this.entityTester.assertInvalidValue("numConsultations", -1, "must be greater than or equal to 0");
	}
	
	// TODO there cannot be clashes between preferences
}

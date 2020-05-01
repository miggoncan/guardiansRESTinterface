package guardians.model.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import guardians.model.repositories.DoctorRepository;
import guardians.model.repositories.ShiftCycleRepository;

@DataJpaTest
public class ShiftCycleTest {
	@Autowired
	ShiftCycleRepository shiftCycleRepository;
	
	@Autowired
	DoctorRepository doctorRepository;
	
	private EntityTester<ShiftCycle> entityTester;

	public ShiftCycleTest() {
		this.entityTester =  new EntityTester<>(ShiftCycle.class);
	}
	
	@Test
	void testDates() {
		ShiftCycle shiftCycle = new ShiftCycle();
		shiftCycle.setDoctors(DoctorTest.createValidDoctors());
		DateTester<ShiftCycle> dateTester = new DateTester<>(ShiftCycle.class);
		dateTester.testEntity(shiftCycle, (day, month, year) -> {
			shiftCycle.setDay(day);
			shiftCycle.setMonth(month);
			shiftCycle.setYear(year);
		});
	}
	
	//////////////////////////////////////////
	//
	// Tests for valid values
	//
	//////////////////////////////////////////
	
	@Test
	void validDoctors() {
		this.entityTester.assertValidValue("doctors", DoctorTest.createValidDoctors());
	}
	
	@Test
	void createAndSaveValidShiftCycle() {
		List<Doctor> doctors = doctorRepository.saveAll(DoctorTest.createValidDoctors());
		ShiftCycle shiftCycle = new ShiftCycle(1, 2, 2020, doctors);
		
		this.entityTester.assertValidEntity(shiftCycle);
		
		shiftCycle = shiftCycleRepository.save(shiftCycle);
		assertEquals(1, shiftCycle.getDay());
		assertEquals(2, shiftCycle.getMonth());
		assertEquals(2020, shiftCycle.getYear());
	}
	
	//////////////////////////////////////////
	//
	// Tests for invalid values
	//
	//////////////////////////////////////////
	
	@Test
	void doctorsCannotBeNull() {
		this.entityTester.assertInvalidValue("doctors", null, "must not be empty");
	}
	
	@Test
	void doctorsCannotBeEmpty() {
		this.entityTester.assertInvalidValue("doctors", new ArrayList<Doctor>(), "must not be empty");
	}
}

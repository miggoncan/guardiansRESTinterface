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
public class CycleShiftTest {
	@Autowired
	ShiftCycleRepository shiftCycleRepository;
	
	@Autowired
	DoctorRepository doctorRepository;
	
	private EntityTester<CycleShift> entityTester;

	public CycleShiftTest() {
		this.entityTester =  new EntityTester<>(CycleShift.class);
	}
	
	@Test
	void testDates() {
		CycleShift cycleShift = new CycleShift();
		cycleShift.setDoctors(DoctorTest.createValidDoctors());
		DateTester<CycleShift> dateTester = new DateTester<>(CycleShift.class);
		dateTester.testEntity(cycleShift, (day, month, year) -> {
			cycleShift.setDay(day);
			cycleShift.setMonth(month);
			cycleShift.setYear(year);
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
		CycleShift cycleShift = new CycleShift(1, 2, 2020, doctors);
		
		this.entityTester.assertValidEntity(cycleShift);
		
		cycleShift = shiftCycleRepository.save(cycleShift);
		assertEquals(1, cycleShift.getDay());
		assertEquals(2, cycleShift.getMonth());
		assertEquals(2020, cycleShift.getYear());
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

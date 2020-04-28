package es.us.alumn.miggoncan2.model.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.alumn.miggoncan2.model.repositories.DoctorRepository;
import es.us.alumn.miggoncan2.model.repositories.ShiftCycleRepository;

@DataJpaTest
public class ShiftCycleTest extends EntityTest {
	@Autowired
	ShiftCycleRepository shiftCycleRepository;
	
	@Autowired
	DoctorRepository doctorRepository;

	public ShiftCycleTest() {
		super(ShiftCycle.class);
	}
	
	//////////////////////////////////////////
	//
	// Tests for valid values
	//
	//////////////////////////////////////////
	
	@Test
	void validDoctors() {
		this.assertValidValue("doctors", DoctorTest.createValidDoctors());
	}
	
	@Test
	void leapYearDateIsValid() {
		ShiftCycle shiftCycle = new ShiftCycle(29, 2, 2020, DoctorTest.createValidDoctors());
		
		this.constraintViolations = new HashSet<>(this.validator.validate(shiftCycle));
		assertEquals(0, this.constraintViolations.size());
	}
	
	@Test
	void createAndSaveValidShiftCycle() {
		List<Doctor> doctors = doctorRepository.saveAll(DoctorTest.createValidDoctors());
		ShiftCycle shiftCycle = new ShiftCycle(1, 2, 2020, doctors);
		
		constraintViolations = new HashSet<>(validator.validate(shiftCycle));
		assertEquals(0, constraintViolations.size());
		
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
		this.assertInvalidValue("doctors", null, "must not be empty");
	}
	
	@Test
	void doctorsCannotBeEmpty() {
		this.assertInvalidValue("doctors", new ArrayList<Doctor>(), "must not be empty");
	}
	
	@Test
	void dateHasToBeValid() {
		ShiftCycle shiftCycle = new ShiftCycle(31, 2, 2020, DoctorTest.createValidDoctors());
		
		this.constraintViolations = new HashSet<>(validator.validate(shiftCycle));
		assertEquals(1, this.constraintViolations.size());
		assertTrue(this.constraintViolationsContains("The day, month and year have to be valid"));
	}
}

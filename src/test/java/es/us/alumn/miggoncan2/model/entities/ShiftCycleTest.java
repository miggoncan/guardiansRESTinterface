package es.us.alumn.miggoncan2.model.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
	void validShiftNumber() {
		this.assertValidValue("shiftNumber", 1);
	}
	
	@Test
	void isNextShiftInCycleCanBeTrue() {
		this.assertValidValue("isNextShiftInCycle", true);
	}
	
	@Test
	void isNextShiftInCycleCanBeFalse() {
		this.assertValidValue("isNextShiftInCycle", false);
	}
	
	@Test
	void validDoctors() {
		this.assertValidValue("doctors", DoctorTest.createValidDoctors());
	}
	
	@Test
	void createAndSaveValidShiftCycle() {
		List<Doctor> doctors = doctorRepository.saveAll(DoctorTest.createValidDoctors());
		ShiftCycle shiftCycle = new ShiftCycle(1, doctors , true);
		
		constraintViolations = new HashSet<>(validator.validate(shiftCycle));
		assertEquals(0, constraintViolations.size());
		
		shiftCycle = shiftCycleRepository.save(shiftCycle);
		assertEquals(1, shiftCycle.getShiftNumber());
	}
	
	//////////////////////////////////////////
	//
	// Tests for invalid values
	//
	//////////////////////////////////////////
	
	@Test
	void shiftNumberCannotBeZero() {
		this.assertInvalidValue("shiftNumber", new Integer(0), "must be greater than 0");
	}
	
	@Test
	void shiftNumberCannotBeNegative() {
		this.assertInvalidValue("shiftNumber", new Integer(-1), "must be greater than 0");
	}
	
	@Test
	void isNextShiftInCycleCannotBeNull() {
		this.assertAttributeCannotBeNull("isNextShiftInCycle");
	}
	
	@Test
	void doctorsCannotBeNull() {
		this.assertInvalidValue("doctors", null, "must not be empty");
	}
	
	@Test
	void doctorsCannotBeEmpty() {
		this.assertInvalidValue("doctors", new ArrayList<Doctor>(), "must not be empty");
	}
}

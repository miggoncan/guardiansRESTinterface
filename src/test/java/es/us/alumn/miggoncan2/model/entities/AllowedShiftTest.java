package es.us.alumn.miggoncan2.model.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.alumn.miggoncan2.model.repositories.AllowedShiftRepository;

@DataJpaTest
public class AllowedShiftTest extends EntityTest {
	@Autowired
	private AllowedShiftRepository allowedShiftRepository;
	
	// Will contain the violated constraints in each test
	protected Set<ConstraintViolation<AllowedShift>> constraintViolations;
	
	/**
	 * This method creates a List of valid AllowedShift instances,
	 * all different from each other. 
	 * 
	 * Calls to this method will always return the same List (the instances 
	 * will be new, but the attributes of the AllowedShifts will be the same)
	 * 
	 * @return The valid allowed shifts, but not persisted
	 */
	static public List<AllowedShift> createValidAllowedShifts() {
		List<AllowedShift> allowedShifts = new ArrayList<>();
		allowedShifts.add(new AllowedShift("Monday"));
		allowedShifts.add(new AllowedShift("Wednesday"));
		allowedShifts.add(new AllowedShift("Thursday"));
		return allowedShifts;
	}
	
	private boolean constraintViolationsContains(String message) {
		boolean containsMessage = false;
		for (ConstraintViolation<?> constraintViolation : constraintViolations) {
			if (constraintViolation.getMessage().contains(message)) {
				containsMessage = true;
				break;
			}
		}
		return containsMessage;
	}
	
	///////////////////////////////////////
	//
	// Tests for valid values
	//
	///////////////////////////////////////
	
	@Test
	void validShift() {
		constraintViolations = validator.validateValue(AllowedShift.class, "shift", "Monday");
		assertEquals(0, constraintViolations.size());
	}
	
	@Test 
	void createAndSaveValidAllowedShift() {
		AllowedShift allowedShift = new AllowedShift("Tuesday");
		
		constraintViolations = validator.validate(allowedShift);
		assertEquals(0, constraintViolations.size());
		
		allowedShift = allowedShiftRepository.save(allowedShift);
		assertNotEquals(0, allowedShift.getId());
		
	}
	
	///////////////////////////////////////
	//
	// Tests for invalid values
	//
	///////////////////////////////////////
	
	@Test
	void shiftCannotBeNull() {
		constraintViolations = validator.validateValue(AllowedShift.class, "shift", null);
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("must not be blank"));
	}
	
	@Test
	void shiftCannotBeEmpty() {
		constraintViolations = validator.validateValue(AllowedShift.class, "shift", "");
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("must not be blank"));
	}
	
	@Test
	void shiftCannotBeAWhiteSpaceString() {
		constraintViolations = validator.validateValue(AllowedShift.class, "shift", "  ");
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("must not be blank"));
	}
}

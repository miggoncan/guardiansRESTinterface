package es.us.alumn.miggoncan2.model.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.alumn.miggoncan2.model.repositories.DoctorRepository;

@DataJpaTest
public class DoctorTest extends EntityTest {
	@Autowired
	private DoctorRepository doctorRepository;
	
	// Will contain the violated constraints in each test
	protected Set<ConstraintViolation<Doctor>> constraintViolations;
	
	/**
	 * This method creates one valid Doctor instance. 
	 * 
	 * Calls to this method will always return a Doctor with the same 
	 * attribute values
	 * 
	 * @return a new Doctor object that has not been persisted
	 */
	public static Doctor createValidDoctor() {
		return new Doctor("Bilbo", "Baggins", "bilbo@mordor.com");
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
	void validFirstName() {
		constraintViolations = validator.validateValue(Doctor.class, "firstName", 
				"Aragorn son of Aragorn");
		assertEquals(0, constraintViolations.size());
	}
	
	@Test
	void validLastNames() {
		constraintViolations = validator.validateValue(Doctor.class, "lastNames", 
				"the heir of Isildur Elendil's son of Gondor");
		assertEquals(0, constraintViolations.size());
	}
	
	@Test
	void validEmail() {
		constraintViolations = validator.validateValue(Doctor.class, "email", 
				"elessar@reunitedkingdom.com");
		assertEquals(0, constraintViolations.size());
	}
	
	@Test
	void absenceCanBeNull() {
		constraintViolations = validator.validateValue(Doctor.class, "absence", null);
		assertEquals(0, constraintViolations.size());
	}
	
	@Test
	void createAndSaveValidDoctor() {
		Doctor doctor = new Doctor("Bilbo", "Baggins", "bilbo@mordos.com");
		
		constraintViolations = validator.validate(doctor);
		assertEquals(0, constraintViolations.size());
		
		doctor = doctorRepository.save(doctor);
		assertNotEquals(0, doctor.getId());
	}
	
	///////////////////////////////////////
	//
	// Tests for valid values
	//
	///////////////////////////////////////
	
	//
	// First name invalid value
	//
	
	@Test
	void firstNameCannotBeNull() {
		constraintViolations = validator.validateValue(Doctor.class, "firstName", null);
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("must not be blank"));
	}
	
	@Test
	void firstNameCannotBeEmpty() {
		constraintViolations = validator.validateValue(Doctor.class, "firstName", "");
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("must not be blank"));
	}
	
	@Test
	void firstNameCannotBeAWhiteSpaceString() {
		constraintViolations = validator.validateValue(Doctor.class, "firstName", "   ");
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("must not be blank"));
	}
	
	//
	// Last names invalid value
	//
	
	@Test
	void lastNamesCannotBeNull() {
		constraintViolations = validator.validateValue(Doctor.class, "lastNames", null);
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("must not be blank"));
	}
	
	@Test
	void lastNamesCannotBeEmpty() {
		constraintViolations = validator.validateValue(Doctor.class, "lastNames", "");
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("must not be blank"));
	}
	
	@Test
	void lastNamesCannotBeAWhiteSpaceString() {
		constraintViolations = validator.validateValue(Doctor.class, "lastNames", "   ");
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("must not be blank"));
	}
	
	//
	// email invalid value
	//
	
	@Test
	void emailCannotBeNull() {
		constraintViolations = validator.validateValue(Doctor.class, "email", null);
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("must not be blank"));
	}
	
	@Test
	void emailCannotBeEmpty() {
		constraintViolations = validator.validateValue(Doctor.class, "email", "");
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("must not be blank"));
	}
	
	@Test
	void emailCannotBeAWhiteSpaceString() {
		constraintViolations = validator.validateValue(Doctor.class, "email", "   ");
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("must not be blank"));
	}
	
	@Test
	void emailHasToHaveADomain() {
		constraintViolations = validator.validateValue(Doctor.class, "email", "aragorn");
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("must be a well-formed email address"));
	}
	
	@Test
	void emailHasToHaveAnAtSymbol() {
		constraintViolations = validator.validateValue(Doctor.class, "email", "aragorn.com");
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("must be a well-formed email address"));
	}
	

	@Test
	void emailCannotBeOnlyADomain() {
		constraintViolations = validator.validateValue(Doctor.class, "email", "@mordor.com");
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("must be a well-formed email address"));
	}
}

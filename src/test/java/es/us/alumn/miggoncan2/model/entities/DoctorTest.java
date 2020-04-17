package es.us.alumn.miggoncan2.model.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.alumn.miggoncan2.model.repositories.DoctorRepository;

@DataJpaTest
public class DoctorTest extends EntityTest {
	@Autowired
	private DoctorRepository doctorRepository;
	
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
	
	/**
	 * This method creates a list of valid Doctor instances. 
	 * 
	 * Calls to this method will always return the same Doctors
	 * 
	 * @return the new Doctor objects that have not been persisted
	 */
	public static List<Doctor> createValidDoctors() {
		List<Doctor> doctors = new ArrayList<>(6);
		doctors.add(new Doctor("Frodo", "Baggins", "frodo@mordor.com"));
		doctors.add(new Doctor("Gollumn", "aka Smeagol", "gollumn@mordor.com"));
		doctors.add(new Doctor("Gandalf", "The Gray", "gandalf@lonelymountain.com"));
		doctors.add(new Doctor("Samwise", "Gamgee", "samg@mordor.com"));
		doctors.add(new Doctor("Galadriel", "the Lady", "lady@lothlorien.com"));
		doctors.add(new Doctor("Gimli", "son of Gloin", "gimli@glittetingcaves.com"));
		return doctors;
	}
	
	public DoctorTest() {
		super(Doctor.class);
	}

	///////////////////////////////////////
	//
	// Tests for valid values
	//
	///////////////////////////////////////
	
	@Test
	void validFirstName() {
		this.assertValidValue("firstName", "Aragorn son of Aragorn");
	}
	
	@Test
	void validLastNames() {
		this.assertValidValue("lastNames", "the heir of Isildur Elendil's son of Gondor");
	}
	
	@Test
	void validEmail() {
		this.assertValidValue("email", "elessar@reunitedkingdom.com");
	}
	
	@Test
	void absenceCanBeNull() {
		this.assertValidValue("absence", null);
	}
	
	@Test
	void createAndSaveValidDoctor() {
		Doctor doctor = new Doctor("Bilbo", "Baggins", "bilbo@mordos.com");
		
		constraintViolations = new HashSet<>(validator.validate(doctor));
		assertEquals(0, constraintViolations.size());
		
		doctor = doctorRepository.save(doctor);
		assertNotEquals(0, doctor.getId());
	}
	
	///////////////////////////////////////
	//
	// Tests for invalid values
	//
	///////////////////////////////////////

	@Test
	void firstNameCannotBeBlank() {
		this.assertAttributeCannotBeBlank("firstName");
	}
	
	@Test
	void lastNamesCannotBeBlank() {
		this.assertAttributeCannotBeBlank("lastNames");
	}
	
	@Test
	void emailCannotBeBlank() {
		this.assertAttributeCannotBeBlank("email");
	}
	
	@Test
	void emailHasToHaveADomain() {
		constraintViolations = new HashSet<>(validator.validateValue(Doctor.class, "email", "aragorn"));
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("must be a well-formed email address"));
	}
	
	@Test
	void emailHasToHaveAnAtSymbol() {
		constraintViolations = new HashSet<>(validator.validateValue(Doctor.class, "email", "aragorn.com"));
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("must be a well-formed email address"));
	}
	

	@Test
	void emailCannotBeOnlyADomain() {
		constraintViolations = new HashSet<>(validator.validateValue(Doctor.class, "email", "@mordor.com"));
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("must be a well-formed email address"));
	}
}

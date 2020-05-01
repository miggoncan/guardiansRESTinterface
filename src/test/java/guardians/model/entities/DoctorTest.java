package guardians.model.entities;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import guardians.model.entities.Doctor;
import guardians.model.repositories.DoctorRepository;

@DataJpaTest
public class DoctorTest {
	@Autowired
	private DoctorRepository doctorRepository;
	
	private EntityTester<Doctor> entityTester;
	
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
		this.entityTester = new EntityTester<>(Doctor.class);
	}

	///////////////////////////////////////
	//
	// Tests for valid values
	//
	///////////////////////////////////////
	
	@Test
	void validFirstName() {
		this.entityTester.assertValidValue("firstName", "Aragorn son of Aragorn");
	}
	
	@Test
	void validLastNames() {
		this.entityTester.assertValidValue("lastNames", "the heir of Isildur Elendil's son of Gondor");
	}
	
	@Test
	void validEmail() {
		this.entityTester.assertValidValue("email", "elessar@reunitedkingdom.com");
	}
	
	@Test
	void absenceCanBeNull() {
		this.entityTester.assertValidValue("absence", null);
	}
	
	@Test
	void createAndSaveValidDoctor() {
		Doctor doctor = new Doctor("Bilbo", "Baggins", "bilbo@mordos.com");
		
		this.entityTester.assertValidEntity(doctor);
		
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
		this.entityTester.assertAttributeCannotBeBlank("firstName");
	}
	
	@Test
	void lastNamesCannotBeBlank() {
		this.entityTester.assertAttributeCannotBeBlank("lastNames");
	}
	
	@Test
	void emailCannotBeBlank() {
		this.entityTester.assertAttributeCannotBeBlank("email");
	}
	
	@Test
	void emailHasToHaveADomain() {
		this.entityTester.assertInvalidValue("email", "aragorn", "must be a well-formed email address");
	}
	
	@Test
	void emailHasToHaveAnAtSymbol() {
		this.entityTester.assertInvalidValue("email", "aragorn.com", "must be a well-formed email address");
	}
	

	@Test
	void emailCannotBeOnlyADomain() {
		this.entityTester.assertInvalidValue("email", "@mordor.com", "must be a well-formed email address");
	}
}

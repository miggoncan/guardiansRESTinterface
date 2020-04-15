package es.us.alumn.miggoncan2.model.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.jpa.JpaSystemException;

import es.us.alumn.miggoncan2.model.repositories.AbsenceRepository;
import es.us.alumn.miggoncan2.model.repositories.DoctorRepository;

@DataJpaTest
class AbsenceTest extends EntityTest{
	@Autowired
	private AbsenceRepository absenceRepository;
	
	@Autowired
	private DoctorRepository doctorRepository;

	// Will contain the violated constraints in each test
	private Set<ConstraintViolation<Absence>> constraintViolations;
	
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
	void validStartDate() {
		constraintViolations = validator.validateValue(Absence.class, "start", 
				new Date(System.currentTimeMillis() + 2*24*3600*1000));
		assertEquals(0, constraintViolations.size());
	}
	
	@Test
	void validEndDate() {
		constraintViolations = validator.validateValue(Absence.class, "end", 
				new Date(System.currentTimeMillis() + 2*24*3600*1000));
		assertEquals(0, constraintViolations.size());
	}
	
	@Test
	void createAndSaveValidAbsence() {
		Doctor myDoctor = doctorRepository.save(DoctorTest.createValidDoctor());
		Absence absence = new Absence(new Date(System.currentTimeMillis()), 
				new Date(System.currentTimeMillis() + 5*24*3600*1000));
		absence.setDoctor(myDoctor);
		
		constraintViolations = validator.validate(absence);
		assertEquals(0, constraintViolations.size());
		
		absence = absenceRepository.save(absence);
		assertEquals(myDoctor.getId(), absence.getDoctorId());
	}
	
	
	///////////////////////////////////////
	//
	// Tests for invalid values
	//
	///////////////////////////////////////
	
	@Test
	void endCannotBeNull() {
		constraintViolations = validator.validateValue(Absence.class, "end", null);
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("must not be null"));
	}
	
	@Test
	void startCannotBeNull() {
		constraintViolations = validator.validateValue(Absence.class, "start", null);
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("must not be null"));
	}
	
	@Test 
	void createAbsenceWithStartAfterEnd() {
		Doctor myDoctor = doctorRepository.save(DoctorTest.createValidDoctor());
		Absence absence = new Absence(new Date(System.currentTimeMillis() + 2*24*3600*1000),
				new Date(System.currentTimeMillis()));
		absence.setDoctor(myDoctor);
		constraintViolations = validator.validate(absence);
		assertNotEquals(0, constraintViolations.size());
		assertTrue(this.constraintViolationsContains("The start date of the Absence must be before its end date"));
	}
	
	@Test
	void saveAbsenceWithoutDoctor() {
		Absence absence = new Absence();
		Exception ex = assertThrows(JpaSystemException.class, () -> {
			absenceRepository.save(absence);
		});
		assertTrue(ex.getMessage().contains("attempted to assign id from null"));
	}

}

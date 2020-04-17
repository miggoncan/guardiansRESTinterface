package es.us.alumn.miggoncan2.model.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.HashSet;

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
	
	public AbsenceTest() {
		super(Absence.class);
	}
	
	///////////////////////////////////////
	//
	// Tests for valid values
	//
	///////////////////////////////////////
	
	@Test
	void validStartDate() {
		this.assertValidValue("start", new Date(System.currentTimeMillis() + 2*24*3600*1000));
	}
	
	@Test
	void validEndDate() {
		this.assertValidValue("end", new Date(System.currentTimeMillis() + 2*24*3600*1000));
	}
	
	@Test
	void createAndSaveValidAbsence() {
		Doctor myDoctor = doctorRepository.save(DoctorTest.createValidDoctor());
		Absence absence = new Absence(new Date(System.currentTimeMillis()), 
				new Date(System.currentTimeMillis() + 5*24*3600*1000));
		absence.setDoctor(myDoctor);
		
		constraintViolations = new HashSet<>(validator.validate(absence));
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
		this.assertAttributeCannotBeNull("end");
	}
	
	@Test
	void startCannotBeNull() {
		this.assertAttributeCannotBeNull("start");
	}
	
	@Test 
	void createAbsenceWithStartAfterEnd() {
		Doctor myDoctor = doctorRepository.save(DoctorTest.createValidDoctor());
		Absence absence = new Absence(new Date(System.currentTimeMillis() + 2*24*3600*1000),
				new Date(System.currentTimeMillis()));
		absence.setDoctor(myDoctor);
		constraintViolations = new HashSet<>(validator.validate(absence));
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

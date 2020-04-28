package es.us.alumn.miggoncan2.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import es.us.alumn.miggoncan2.model.entities.Absence;
import es.us.alumn.miggoncan2.model.entities.AbsenceTest;
import es.us.alumn.miggoncan2.model.entities.Doctor;
import es.us.alumn.miggoncan2.model.entities.DoctorTest;
import es.us.alumn.miggoncan2.model.repositories.AbsenceRepository;
import es.us.alumn.miggoncan2.model.repositories.DoctorRepository;

@SpringBootTest
@Transactional
class DoctorControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;
	
	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired
	private AbsenceRepository absenceRepository;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	private static JSONObject getJSONDoctor() {
		JSONObject doctor = new JSONObject();
		try {
			doctor.put("firstName", "Legolas");
			doctor.put("lastNames", "son of Elvenking Thranduil of Mirkwood");
			doctor.put("email", "legolas@mirkwood.com");
		} catch (JSONException e) {
			fail("Unexpected JSONException: " + e.toString());
		}
		return doctor;
	}
	
	private static JSONObject getJSONDoctorWithAbsence() {
		JSONObject doctor = new JSONObject();
		try {
			doctor.put("firstName", "Legolas");
			doctor.put("lastNames", "son of Elvenking Thranduil of Mirkwood");
			doctor.put("email", "legolas@mirkwood.com");
			JSONObject absence = new JSONObject();
			absence.put("start", "2020-02-10");
			absence.put("end", "2020-05-20");
			doctor.put("absence", absence);
		} catch (JSONException e) {
			fail("Unexpected JSONException: " + e.toString());
		}
		return doctor;
	}

	@Test
	void getDoctors() throws Exception {
		mockMvc.perform(get("/doctors")).andExpect(status().isOk());
	}
	
	@Test
	void getDoctor() throws Exception {
		Doctor savedDoctor = doctorRepository.save(DoctorTest.createValidDoctor());
		mockMvc.perform(get("/doctors/" + savedDoctor.getId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName", is(savedDoctor.getFirstName())))
			.andExpect(jsonPath("$.lastNames", is(savedDoctor.getLastNames())))
			.andExpect(jsonPath("$.email", is(savedDoctor.getEmail())));
	}

	/////////////////////////////////////
	//
	// Doctor creation
	//
	////////////////////////////////////
	
	@Test
	void postDoctorWithoutAbsence() throws Exception {
		JSONObject doctor = getJSONDoctor();
		mockMvc.perform(post("/doctors").content(doctor.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(doctor.toString(), false));
	}
	
	@Test
	void postDoctorWithAbsence() throws Exception {
		JSONObject doctor = getJSONDoctorWithAbsence();
		mockMvc.perform(post("/doctors").content(doctor.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(doctor.toString(), false));
	}
	
	@Test
	void postInvalidDoctor() throws Exception {
		JSONObject doctor = new JSONObject();
		doctor.put("firstName", "Legolas");
		// lastnames is ommitted
		doctor.put("email", "legolas@mirkwood.com");
		mockMvc.perform(post("/doctors").content(doctor.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void postDoctorWithInvalidAbsence() throws Exception {
		JSONObject doctor = getJSONDoctor();
		JSONObject absence = new JSONObject();
		// start date is after end date
		absence.put("start", "2020-04-10");
		absence.put("end", "2020-02-20");
		doctor.put("absence", absence);
		mockMvc.perform(post("/doctors").content(doctor.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Invalid Absence: \"The start date of the Absence must be before its end date\" "));
	}
	
	@Test
	void postDoctorAlreadyExists() throws Exception {
		// Persist first doctor
		Doctor savedDoctor = doctorRepository.save(DoctorTest.createValidDoctor());
		String firstName = savedDoctor.getFirstName();
		String lastNames = savedDoctor.getLastNames();
		JSONObject doctor = new JSONObject();
		doctor.put("firstName", firstName);
		doctor.put("lastNames", lastNames);
		doctor.put("email", "example@example.com");
		mockMvc.perform(post("/doctors")
					.content(doctor.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("The Doctor " + firstName + " " + lastNames + " already exists"));
	}
	
	/////////////////////////////////////
	//
	// Doctor update
	//
	////////////////////////////////////
	
	@Test
	void putValidDoctor() throws Exception {
		Doctor savedDoctor = doctorRepository.save(DoctorTest.createValidDoctor());
		JSONObject doctor = getJSONDoctor();
		mockMvc.perform(put("/doctors/" + savedDoctor.getId())
					.content(doctor.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(doctor.toString(), false));
	}
	
	@Test
	void putValidDoctorWithAbsence() throws Exception {
		Doctor savedDoctor = doctorRepository.save(DoctorTest.createValidDoctor());
		JSONObject doctor = getJSONDoctorWithAbsence();
		mockMvc.perform(put("/doctors/" + savedDoctor.getId())
					.content(doctor.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(doctor.toString(), false));
	}
	
	@Test
	void putDoctorWithInvalidAbsence() throws Exception {
		Doctor savedDoctor = doctorRepository.save(DoctorTest.createValidDoctor());
		JSONObject doctor = getJSONDoctorWithAbsence();
		JSONObject absence = new JSONObject();
		// start date is after end date
		absence.put("start", "2020-04-10");
		absence.put("end", "2020-02-20");
		doctor.put("absence", absence);
		mockMvc.perform(put("/doctors/" + savedDoctor.getId())
					.content(doctor.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Invalid Absence: \"The start date of the Absence must be before its end date\" "));
		}
	
	@Test
	void putValidDoctorEliminatingAbsence() throws Exception {
		Doctor savedDoctor = doctorRepository.save(DoctorTest.createValidDoctor());
		Absence absence = AbsenceTest.createValidAbsence();
		absence.setDoctor(savedDoctor);
		absence = absenceRepository.save(absence);
		JSONObject doctor = getJSONDoctor();
		mockMvc.perform(put("/doctors/" + savedDoctor.getId())
					.content(doctor.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(doctor.toString(), false));
	}
	
	@Test
	void putDoctorAlreadyExists() throws Exception {
		// Persist first doctor
		Doctor savedDoctor = doctorRepository.save(DoctorTest.createValidDoctor());
		String firstName = savedDoctor.getFirstName();
		String lastNames = savedDoctor.getLastNames();
		// Persist second doctor
		Doctor validDoctor = DoctorTest.createValidDoctor();
		validDoctor.setFirstName("Gandalf");
		validDoctor.setLastNames("The White");
		validDoctor = doctorRepository.save(validDoctor);
		// The doctor sent will have the name of the first doctor
		JSONObject doctor = new JSONObject();
		doctor.put("firstName", firstName);
		doctor.put("lastNames", lastNames);
		doctor.put("email", "example@example.com");
		mockMvc.perform(put("/doctors/" + validDoctor.getId())
					.content(doctor.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("The Doctor " + firstName + " " + lastNames + " already exists"));
	}
	
	@Test
	void putDoctorDoesntExist() throws Exception {
		JSONObject doctor = getJSONDoctor();
		mockMvc.perform(put("/doctors/" + 1515)
					.content(doctor.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(content().string("Could not find the doctor 1515"));
	}

}

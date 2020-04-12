package es.us.alumn.miggoncan2.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import es.us.alumn.miggoncan2.model.entities.Doctor;
import es.us.alumn.miggoncan2.model.entities.ShiftConfiguration;
import es.us.alumn.miggoncan2.model.repositories.DoctorRepository;
import es.us.alumn.miggoncan2.model.repositories.ShiftConfigurationRepository;
import es.us.alumn.miggoncan2.model.repositories.exceptions.ShiftConfigurationNotFoundException;

@Controller
public class DoctorController {
	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private ShiftConfigurationRepository shiftConfigurationRepository;
	
	@GetMapping("/facultativos")
	@ResponseBody
	public List<Doctor> getDoctors() {
		return doctorRepository.findAll();
	}
	
	@GetMapping("/facultativos/config-cas")
	@ResponseBody
	public List<ShiftConfiguration> getShitfConfigurations() {
		return shiftConfigurationRepository.findAll();
	}
	
	@GetMapping("/facultativos/config-cas/{doctorId}")
	@ResponseBody
	public ShiftConfiguration getShitfConfiguration(@PathVariable Long doctorId) {
		return shiftConfigurationRepository.findById(doctorId)
				.orElseThrow(() -> new ShiftConfigurationNotFoundException(doctorId));
	}
}

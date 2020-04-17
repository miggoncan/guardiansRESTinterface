package es.us.alumn.miggoncan2;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.us.alumn.miggoncan2.model.entities.Absence;
import es.us.alumn.miggoncan2.model.entities.AllowedShift;
import es.us.alumn.miggoncan2.model.entities.Doctor;
import es.us.alumn.miggoncan2.model.entities.ShiftConfiguration;
import es.us.alumn.miggoncan2.model.entities.ShiftCycle;
import es.us.alumn.miggoncan2.model.repositories.AbsenceRepository;
import es.us.alumn.miggoncan2.model.repositories.AllowedShiftRepository;
import es.us.alumn.miggoncan2.model.repositories.DoctorRepository;
import es.us.alumn.miggoncan2.model.repositories.ShiftConfigurationRepository;
import es.us.alumn.miggoncan2.model.repositories.ShiftCycleRepository;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class LoadDatabase {
	@Bean
	CommandLineRunner initDatabase(DoctorRepository doctorRepository, 
									AbsenceRepository absenceRepository,
									ShiftConfigurationRepository shiftConfigurationRepository, 
									AllowedShiftRepository allowedShiftRepository,
									ShiftCycleRepository shiftCycleRepository) {
		return args -> {
			AllowedShift allowedShiftMonday = allowedShiftRepository.save(new AllowedShift("Monday"));
			log.info("Preloading " + allowedShiftMonday);
			AllowedShift allowedShiftTuesday = allowedShiftRepository.save(new AllowedShift("Tuesday"));
			log.info("Preloading " + allowedShiftTuesday);
			AllowedShift allowedShiftWednesday = allowedShiftRepository.save(new AllowedShift("Wednesday"));
			log.info("Preloading " + allowedShiftWednesday);
			AllowedShift allowedShiftThursday = allowedShiftRepository.save(new AllowedShift("Thursday"));
			log.info("Preloading " + allowedShiftThursday);
			AllowedShift allowedShiftFriday = allowedShiftRepository.save(new AllowedShift("Friday"));
			log.info("Preloading " + allowedShiftFriday);
			
			// This will be a regular doctor
			Doctor doctor1 = doctorRepository.save(new Doctor("Bilbo", "Baggins", "bilbo@mordor.com"));
			Set<AllowedShift> doctor1UnwantedShifts = new HashSet<>();
			ShiftConfiguration doctor1ShiftConf = new ShiftConfiguration(3, 2, true);
			doctor1ShiftConf.setDoctor(doctor1);
			doctor1UnwantedShifts.add(allowedShiftFriday);
			doctor1UnwantedShifts.add(allowedShiftWednesday);
			doctor1ShiftConf.setUnwantedShifts(doctor1UnwantedShifts);
			Set<AllowedShift> doctor1UnavailableShifts = new HashSet<>();
			doctor1UnavailableShifts.add(allowedShiftMonday);
			doctor1ShiftConf.setUnavailableShifts(doctor1UnavailableShifts);
			Set<AllowedShift> doctor1WantedShifts = new HashSet<>();
			doctor1WantedShifts.add(allowedShiftTuesday);
			doctor1ShiftConf.setWantedShifts(doctor1WantedShifts);
			doctor1ShiftConf.setUnwantedShifts(doctor1UnwantedShifts);
			doctor1ShiftConf = shiftConfigurationRepository.save(doctor1ShiftConf);
			log.info("Preloading " + doctor1 + " with shitf configuration " + doctor1ShiftConf);
			
			// This doctor will not do non-cycle shifts
			Doctor doctor2 = doctorRepository.save(new Doctor("Frodo", "Baggins", "frodo@mordor.com"));
			log.info("Preloading " + doctor2);
	  
			// This doctor will be absent for 7 days from now
			Doctor sickDoctor = doctorRepository.save(new Doctor("Gollumn", "aka Smeagol", "gollumn@mordor.com"));
			Absence sickDoctorAbsence = new Absence(new Date(System.currentTimeMillis()), 
					new Date(System.currentTimeMillis() + 7*24*3600*1000));
			sickDoctorAbsence.setDoctor(sickDoctor);
			sickDoctorAbsence = absenceRepository.save(sickDoctorAbsence);
			ShiftConfiguration sickDoctorShiftConf = new ShiftConfiguration(2, 2, true);
			sickDoctorShiftConf.setDoctor(sickDoctor);
			Set<AllowedShift> sickDoctorMandatoryShifts = new HashSet<>();
			sickDoctorMandatoryShifts.add(allowedShiftThursday);
			sickDoctorMandatoryShifts.add(allowedShiftTuesday);
			sickDoctorShiftConf.setMandatoryShifts(sickDoctorMandatoryShifts);
			sickDoctorShiftConf = shiftConfigurationRepository.save(sickDoctorShiftConf);
			log.info("Preloading " + sickDoctor + " with absence " + sickDoctorAbsence 
					+ " and with shift configuration " + sickDoctorShiftConf);
			
			// This will be another regular doctor
			Doctor doctor4 = doctorRepository.save(new Doctor("Gandalf", "The Gray", "gandalf@lonelymountain.com"));
			ShiftConfiguration doctor4ShiftConf = new ShiftConfiguration(3, 3, false);
			doctor4ShiftConf.setDoctor(doctor4);
			Set<AllowedShift> doctor4UnwantedShifts = new HashSet<>();
			doctor4UnwantedShifts.add(allowedShiftThursday);
			doctor4UnwantedShifts.add(allowedShiftFriday);
			doctor4ShiftConf.setUnwantedShifts(doctor4UnwantedShifts);
			Set<AllowedShift> doctor4UnavailableShifts = new HashSet<>();
			doctor4UnavailableShifts.add(allowedShiftTuesday);
			doctor4UnavailableShifts.add(allowedShiftMonday);
			doctor4ShiftConf.setUnavailableShifts(doctor4UnavailableShifts);
			doctor4ShiftConf = shiftConfigurationRepository.save(doctor4ShiftConf);
			log.info("Preloading " + doctor4 + " with shift configuration " + doctor4ShiftConf);
			
			// First day in the shift cycle
			List<Doctor> shiftCycle1Doctors = new ArrayList<>();
			shiftCycle1Doctors.add(doctor1);
			shiftCycle1Doctors.add(doctor2);
			ShiftCycle shiftCycle1 = new ShiftCycle(1, shiftCycle1Doctors , true);
			log.info("Preloading " + shiftCycleRepository.save(shiftCycle1));
			// Second day in the shift cycle
			List<Doctor> shiftCycle2Doctors = new ArrayList<>();
			shiftCycle2Doctors.add(sickDoctor);
			shiftCycle2Doctors.add(doctor4);
			ShiftCycle shiftCycle2 = new ShiftCycle(2, shiftCycle2Doctors , true);
			log.info("Preloading " + shiftCycleRepository.save(shiftCycle2));
	    };
	}
}

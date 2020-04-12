package es.us.alumn.miggoncan2;

import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.us.alumn.miggoncan2.model.entities.Absence;
import es.us.alumn.miggoncan2.model.entities.Doctor;
import es.us.alumn.miggoncan2.model.entities.ShiftConfiguration;
import es.us.alumn.miggoncan2.model.repositories.AbsenceRepository;
import es.us.alumn.miggoncan2.model.repositories.DoctorRepository;
import es.us.alumn.miggoncan2.model.repositories.ShiftConfigurationRepository;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class LoadDatabase {
	@Bean
	CommandLineRunner initDatabase(DoctorRepository doctorRepository, AbsenceRepository absenceRepository,
			ShiftConfigurationRepository shiftConfigurationRepository) {
		return args -> {
			Doctor doctor1 = doctorRepository.save(new Doctor("Bilbo", "Baggins", "bilbo@mordor.com"));
			ShiftConfiguration doctor1ShiftConf = new ShiftConfiguration(3, 2, true);
			doctor1ShiftConf.setDoctor(doctor1);
			shiftConfigurationRepository.save(doctor1ShiftConf);
			log.info("Preloading " + doctor1 + " with shitf configuration " + doctor1ShiftConf);
			
			// This doctor will not do non-cycle shifts
			Doctor doctor2 = doctorRepository.save(new Doctor("Frodo", "Baggins", "frodo@mordor.com"));
			log.info("Preloading " + doctor2);
	  
			// This doctor will be absent for 7 days from now
			Doctor sickDoctor = doctorRepository.save(new Doctor("Gollumn", "", "gollumn@mordor.com"));
			Absence sickDoctorAbsence = new Absence(new Date(System.currentTimeMillis()), 
					new Date(System.currentTimeMillis() + 7*24*3600*1000));
			sickDoctorAbsence.setDoctor(sickDoctor);
			sickDoctorAbsence = absenceRepository.save(sickDoctorAbsence);
			log.info("Preloading " + sickDoctor + " with absence " + sickDoctorAbsence);
	    };
	}
}

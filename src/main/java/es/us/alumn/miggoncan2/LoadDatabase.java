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
import es.us.alumn.miggoncan2.model.entities.Calendar;
import es.us.alumn.miggoncan2.model.entities.CycleChange;
import es.us.alumn.miggoncan2.model.entities.DayConfiguration;
import es.us.alumn.miggoncan2.model.entities.Doctor;
import es.us.alumn.miggoncan2.model.entities.ShiftConfiguration;
import es.us.alumn.miggoncan2.model.entities.ShiftCycle;
import es.us.alumn.miggoncan2.model.repositories.AbsenceRepository;
import es.us.alumn.miggoncan2.model.repositories.AllowedShiftRepository;
import es.us.alumn.miggoncan2.model.repositories.CalendarRepository;
import es.us.alumn.miggoncan2.model.repositories.CycleChangeRespository;
import es.us.alumn.miggoncan2.model.repositories.DayConfigurationRepository;
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
									ShiftCycleRepository shiftCycleRepository,
									CalendarRepository calendarRepository,
									DayConfigurationRepository dayConfigurationRepository,
									CycleChangeRespository cycleChangeRespository) {
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
			
			// First Calendar
			Calendar calendarApril = calendarRepository.save(new Calendar(4, 2020));
			// Day one of first calendar
			DayConfiguration aprilConf1 = new DayConfiguration(1, true, 2, 0);
			aprilConf1.setCalendar(calendarApril);
			List<Doctor> aprilConf1UnwantedShifts = new ArrayList<>(2);
			aprilConf1UnwantedShifts.add(doctor1);
			aprilConf1UnwantedShifts.add(doctor2);
			aprilConf1.setUnwantedShifts(aprilConf1UnwantedShifts);
			List<Doctor> aprilConf1MandatoryShifts = new ArrayList<>(1);
			aprilConf1MandatoryShifts.add(doctor4);
			aprilConf1.setMandatoryShifts(aprilConf1MandatoryShifts);
			aprilConf1 = dayConfigurationRepository.save(aprilConf1);
			// Cycle change on the first day of the first calendar
			CycleChange aprilConf1CycleChange = new CycleChange(doctor1, doctor4);
			aprilConf1CycleChange.setDayConfiguration(aprilConf1);
			aprilConf1CycleChange = cycleChangeRespository.save(aprilConf1CycleChange);
			List<CycleChange> april1ConfChanges = new ArrayList<>(1);
			april1ConfChanges.add(aprilConf1CycleChange);
			aprilConf1.setCycleChanges(april1ConfChanges );
			// Second day of first calendar
			DayConfiguration aprilConf2 = new DayConfiguration(2, true, 2, 2);
			aprilConf2.setCalendar(calendarApril);
			List<Doctor> aprilConf2WantedShifts = new ArrayList<>(1);
			aprilConf2WantedShifts.add(doctor2);
			aprilConf2.setWantedShifts(aprilConf2WantedShifts);
			aprilConf2 = dayConfigurationRepository.save(aprilConf2);
			List<DayConfiguration> calendarAprilDays = new ArrayList<>(2);
			calendarAprilDays.add(aprilConf1);
			calendarAprilDays.add(aprilConf2);
			calendarApril.setDayConfigurations(calendarAprilDays);
			log.info("Preloading " + calendarApril);
			
			// Second Calendar
			Calendar calendarMay = calendarRepository.save(new Calendar(5, 2020));
			// Day one of first calendar
			DayConfiguration mayConf1 = new DayConfiguration(1, true, 2, 0);
			mayConf1.setCalendar(calendarMay);
			List<Doctor> mayConf1UnwantedShifts = new ArrayList<>(2);
			mayConf1UnwantedShifts.add(doctor2);
			mayConf1UnwantedShifts.add(doctor4);
			mayConf1.setUnwantedShifts(mayConf1UnwantedShifts);
			List<Doctor> mayConf1MandatoryShifts = new ArrayList<>(1);
			mayConf1MandatoryShifts.add(doctor1);
			mayConf1.setMandatoryShifts(mayConf1MandatoryShifts);
			mayConf1 = dayConfigurationRepository.save(mayConf1);
			// Cycle change on the first day of the first calendar
			CycleChange mayConf1CycleChange = new CycleChange(doctor1, doctor4);
			mayConf1CycleChange.setDayConfiguration(mayConf1);
			mayConf1CycleChange = cycleChangeRespository.save(mayConf1CycleChange);
			List<CycleChange> may1ConfChanges = new ArrayList<>(1);
			may1ConfChanges.add(mayConf1CycleChange);
			mayConf1.setCycleChanges(may1ConfChanges );
			// Second day of first calendar
			DayConfiguration mayConf2 = new DayConfiguration(2, true, 2, 2);
			mayConf2.setCalendar(calendarMay);
			List<Doctor> mayConf2UnwantedShifts = new ArrayList<>(1);
			mayConf2UnwantedShifts.add(doctor2);
			mayConf2.setUnwantedShifts(mayConf2UnwantedShifts);;
			mayConf2 = dayConfigurationRepository.save(mayConf2);
			List<DayConfiguration> calendarMayDays = new ArrayList<>(2);
			calendarMayDays.add(mayConf1);
			calendarMayDays.add(mayConf2);
			calendarMay.setDayConfigurations(calendarMayDays);
			log.info("Preloading " + calendarMay);
			
			// TODO add a Schedule to the database
	    };
	}
}

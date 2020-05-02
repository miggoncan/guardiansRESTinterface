package guardians;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import guardians.model.entities.Absence;
import guardians.model.entities.AllowedShift;
import guardians.model.entities.Calendar;
import guardians.model.entities.CycleChange;
import guardians.model.entities.DayConfiguration;
import guardians.model.entities.Doctor;
import guardians.model.entities.Schedule;
import guardians.model.entities.ScheduleDay;
import guardians.model.entities.ShiftConfiguration;
import guardians.model.entities.ShiftCycle;
import guardians.model.entities.Schedule.ScheduleStatus;
import guardians.model.repositories.AbsenceRepository;
import guardians.model.repositories.AllowedShiftRepository;
import guardians.model.repositories.CalendarRepository;
import guardians.model.repositories.CycleChangeRespository;
import guardians.model.repositories.DayConfigurationRepository;
import guardians.model.repositories.DoctorRepository;
import guardians.model.repositories.ScheduleRepository;
import guardians.model.repositories.ShiftConfigurationRepository;
import guardians.model.repositories.ShiftCycleRepository;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class LoadDatabase {
	@Autowired
	DoctorRepository doctorRepository;
	@Autowired
	AbsenceRepository absenceRepository;
	@Autowired
	ShiftConfigurationRepository shiftConfigurationRepository; 
	@Autowired
	AllowedShiftRepository allowedShiftRepository;
	@Autowired
	ShiftCycleRepository shiftCycleRepository;
	@Autowired
	CalendarRepository calendarRepository;
	@Autowired
	DayConfigurationRepository dayConfigurationRepository;
	@Autowired
	CycleChangeRespository cycleChangeRespository;
	@Autowired
	ScheduleRepository scheduleRepository;
	
	// Currently, the database is already preloaded. The Bean annotation is commented to
	// not load it every time the service is launched
//	@Bean
	CommandLineRunner initDatabase() {
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
			Absence sickDoctorAbsence = new Absence(LocalDate.of(2020, 5, 25), LocalDate.of(2020, 7, 30));
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
			ShiftCycle shiftCycle1 = new ShiftCycle(1, 2, 2020, shiftCycle1Doctors);
			log.info("Preloading " + shiftCycleRepository.save(shiftCycle1));
			// Second day in the shift cycle
			List<Doctor> shiftCycle2Doctors = new ArrayList<>();
			shiftCycle2Doctors.add(sickDoctor);
			shiftCycle2Doctors.add(doctor4);
			ShiftCycle shiftCycle2 = new ShiftCycle(2, 2, 2020, shiftCycle2Doctors);
			log.info("Preloading " + shiftCycleRepository.save(shiftCycle2));
			
			// First Calendar
			YearMonth april = YearMonth.of(2020, 4);
			Calendar calendarApril = calendarRepository.save(new Calendar(april.getMonthValue(), april.getYear()));
			// Day one of first calendar
			DayConfiguration aprilConf1 = new DayConfiguration(1, true, 2, 0);
			aprilConf1.setCalendar(calendarApril);
			Set<Doctor> aprilConf1UnwantedShifts = new HashSet<>(2);
			aprilConf1UnwantedShifts.add(doctor1);
			aprilConf1UnwantedShifts.add(doctor2);
			aprilConf1.setUnwantedShifts(aprilConf1UnwantedShifts);
			Set<Doctor> aprilConf1MandatoryShifts = new HashSet<>(1);
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
			Set<Doctor> aprilConf2WantedShifts = new HashSet<>(1);
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
			Set<Doctor> mayConf1UnwantedShifts = new HashSet<>(2);
			mayConf1UnwantedShifts.add(doctor2);
			mayConf1UnwantedShifts.add(doctor4);
			mayConf1.setUnwantedShifts(mayConf1UnwantedShifts);
			Set<Doctor> mayConf1MandatoryShifts = new HashSet<>(1);
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
			Set<Doctor> mayConf2UnwantedShifts = new HashSet<>(1);
			mayConf2UnwantedShifts.add(doctor2);
			mayConf2.setUnwantedShifts(mayConf2UnwantedShifts);;
			mayConf2 = dayConfigurationRepository.save(mayConf2);
			List<DayConfiguration> calendarMayDays = new ArrayList<>(2);
			calendarMayDays.add(mayConf1);
			calendarMayDays.add(mayConf2);
			calendarMay.setDayConfigurations(calendarMayDays);
			log.info("Preloading " + calendarMay);
			
			Schedule schedule = new Schedule(ScheduleStatus.CONFIRMED);
			schedule.setCalendar(calendarApril);
			List<ScheduleDay> scheduleDays = new LinkedList<>();
			ScheduleDay scheduleDay;
			for (int day = 1; day <= april.lengthOfMonth(); day++) {
				scheduleDay = new ScheduleDay(day, true);
				scheduleDay.setSchedule(schedule);
				scheduleDay.setCycle(aprilConf1MandatoryShifts);
				scheduleDay.setShifts(aprilConf1MandatoryShifts);
				scheduleDays.add(scheduleDay);
			}
			schedule.setDays(scheduleDays);
			schedule = scheduleRepository.save(schedule);
			log.info("Preloading " + schedule);
	    };
	}
}

package guardians;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
import guardians.model.entities.Schedule.ScheduleStatus;
import guardians.model.repositories.AllowedShiftRepository;
import guardians.model.repositories.CalendarRepository;
import guardians.model.repositories.DoctorRepository;
import guardians.model.repositories.ScheduleRepository;
import guardians.model.repositories.ShiftConfigurationRepository;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class LoadDatabase {
	@Autowired
	DoctorRepository doctorRepository;
	@Autowired
	ShiftConfigurationRepository shiftConfigurationRepository;
	@Autowired
	AllowedShiftRepository allowedShiftRepository;
	@Autowired
	CalendarRepository calendarRepository;
	@Autowired
	ScheduleRepository scheduleRepository;

	// Currently, the database is already preloaded. The Bean annotation is
	// commented to not load it every time the service is launched
	@Bean
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
			Doctor doctor1 = doctorRepository.save(new Doctor("Bilbo", "Baggins", "bilbo@mordor.com", LocalDate.of(2020, 6, 26)));
			Set<AllowedShift> doctor1UnwantedShifts = new HashSet<>();
			ShiftConfiguration doctor1ShiftConf = new ShiftConfiguration(3, 2, 2, true, false);
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
			Doctor doctor2 = doctorRepository.save(new Doctor("Frodo", "Baggins", "frodo@mordor.com", LocalDate.of(2020, 6, 25)));
			log.info("Preloading " + doctor2);

			// This doctor will be absent for 7 days from now
			Doctor sickDoctor = new Doctor("Gollumn", "aka Smeagol", "gollumn@mordor.com", LocalDate.of(2020, 6, 25));
			Absence sickDoctorAbsence = new Absence(LocalDate.of(2020, 5, 25), LocalDate.of(2020, 7, 30));
			sickDoctorAbsence.setDoctor(sickDoctor);
			sickDoctor.setAbsence(sickDoctorAbsence);
			sickDoctor = doctorRepository.save(sickDoctor);
			ShiftConfiguration sickDoctorShiftConf = new ShiftConfiguration(2, 2, 2, true, false);
			sickDoctorShiftConf.setDoctor(sickDoctor);
			Set<AllowedShift> sickDoctorMandatoryShifts = new HashSet<>();
			sickDoctorMandatoryShifts.add(allowedShiftThursday);
			sickDoctorMandatoryShifts.add(allowedShiftTuesday);
			sickDoctorShiftConf.setMandatoryShifts(sickDoctorMandatoryShifts);
			sickDoctorShiftConf = shiftConfigurationRepository.save(sickDoctorShiftConf);
			log.info("Preloading " + sickDoctor + " with shift configuration " + sickDoctorShiftConf);

			// This will be another regular doctor
			Doctor doctor4 = doctorRepository.save(new Doctor("Gandalf", "The Gray", "gandalf@lonelymountain.com", LocalDate.of(2020, 6, 26)));
			ShiftConfiguration doctor4ShiftConf = new ShiftConfiguration(3, 3, 0, true, false);
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

			// First Calendar
			YearMonth april = YearMonth.of(2020, 4);
			Calendar calendarApril = new Calendar(april.getMonthValue(), april.getYear());
			SortedSet<DayConfiguration> daysApril = new TreeSet<>();
			for (int i = 1; i <= april.lengthOfMonth(); i++) {
				DayConfiguration day = new DayConfiguration(i, true, 3, 0);
				day.setCalendar(calendarApril);
				if (i == 3 || i == 15) {
					day.setMandatoryShifts(new HashSet<>(Arrays.asList(doctor1, doctor4)));
				}
				if (i == 3 || i == 7) {
					day.setUnwantedShifts(new HashSet<>(Arrays.asList(doctor2)));
				}
				if (i == 4 || i == 10) {
					CycleChange cycleChange = new CycleChange(doctor1, doctor4);
					cycleChange.setDayConfiguration(day);
					day.setCycleChanges(Arrays.asList(cycleChange));
				}
				if (i == 5 || i == 7) {
					day.setWantedShifts(new HashSet<>(Arrays.asList(doctor1)));
				}
				daysApril.add(day);
			}
			calendarApril.setDayConfigurations(daysApril);
			calendarApril = calendarRepository.save(calendarApril);
			log.info("Preloading " + calendarApril);

			// Second calendar
			YearMonth may = YearMonth.of(2020, 5);
			Calendar calendarMay = new Calendar(may.getMonthValue(), may.getYear());
			SortedSet<DayConfiguration> daysMay = new TreeSet<>();
			for (int i = 1; i <= may.lengthOfMonth(); i++) {
				DayConfiguration day = new DayConfiguration(i, true, 3, 0);
				day.setCalendar(calendarMay);
				if (i == 5 || i == 10) {
					day.setMandatoryShifts(new HashSet<>(Arrays.asList(doctor2, sickDoctor)));
				}
				if (i == 1 || i == 27) {
					day.setUnwantedShifts(new HashSet<>(Arrays.asList(doctor1)));
				}
				if (i == 6 || i == 8) {
					CycleChange cycleChange = new CycleChange(doctor2, doctor4);
					cycleChange.setDayConfiguration(day);
					day.setCycleChanges(Arrays.asList(cycleChange));
				}
				if (i == 9 || i == 17) {
					day.setWantedShifts(new HashSet<>(Arrays.asList(doctor1)));
				}
				daysMay.add(day);
			}
			calendarMay.setDayConfigurations(daysMay);
			calendarMay = calendarRepository.save(calendarMay);
			log.info("Preloading " + calendarMay);

			Schedule schedule = new Schedule(ScheduleStatus.CONFIRMED);
			schedule.setCalendar(calendarApril);
			SortedSet<ScheduleDay> scheduleDays = new TreeSet<>();
			ScheduleDay scheduleDay;
			for (int day = 1; day <= april.lengthOfMonth(); day++) {
				scheduleDay = new ScheduleDay(day, true);
				scheduleDay.setSchedule(schedule);
				scheduleDay.setCycle(new HashSet<>(Arrays.asList(doctor1, doctor4)));
				scheduleDay.setShifts(new HashSet<>(Arrays.asList(doctor1, doctor2)));
				scheduleDays.add(scheduleDay);
			}
			schedule.setDays(scheduleDays);
			schedule = scheduleRepository.save(schedule);
			log.info("Preloading " + schedule);
		};
	}
}

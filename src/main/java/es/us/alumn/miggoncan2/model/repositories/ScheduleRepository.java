package es.us.alumn.miggoncan2.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.us.alumn.miggoncan2.model.entities.Schedule;
import es.us.alumn.miggoncan2.model.entities.primarykeys.CalendarPK;

public interface ScheduleRepository extends JpaRepository<Schedule, CalendarPK> {

}

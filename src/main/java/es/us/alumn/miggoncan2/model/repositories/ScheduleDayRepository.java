package es.us.alumn.miggoncan2.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.us.alumn.miggoncan2.model.entities.ScheduleDay;
import es.us.alumn.miggoncan2.model.entities.primarykeys.ScheduleDayPK;

public interface ScheduleDayRepository extends JpaRepository<ScheduleDay, ScheduleDayPK> {

}

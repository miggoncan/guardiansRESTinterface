package es.us.alumn.miggoncan2.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.us.alumn.miggoncan2.model.entities.Calendar;
import es.us.alumn.miggoncan2.model.entities.primarykeys.CalendarPK;

public interface CalendarRepository extends JpaRepository<Calendar, CalendarPK> {

}

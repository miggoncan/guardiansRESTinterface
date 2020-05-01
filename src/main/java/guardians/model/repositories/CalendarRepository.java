package es.us.alumn.miggoncan2.model.repositories;

import javax.persistence.Entity;

import org.springframework.data.jpa.repository.JpaRepository;

import es.us.alumn.miggoncan2.model.entities.Calendar;
import es.us.alumn.miggoncan2.model.entities.primarykeys.CalendarPK;

/**
 * This interface will be used by Jpa to auto-generate a class having all the
 * CRUD operations on the {@link Calendar} {@link Entity}. This operations will
 * be performed differently depending on the configured data-source. But this is
 * completely transparent to the application
 * 
 * @author miggoncan
 */
public interface CalendarRepository extends JpaRepository<Calendar, CalendarPK> {

}

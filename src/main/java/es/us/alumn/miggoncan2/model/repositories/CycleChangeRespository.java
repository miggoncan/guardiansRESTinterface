package es.us.alumn.miggoncan2.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.us.alumn.miggoncan2.model.entities.CycleChange;
import es.us.alumn.miggoncan2.model.entities.primarykeys.DayConfigurationPK;

public interface CycleChangeRespository extends JpaRepository<CycleChange, DayConfigurationPK>{

}

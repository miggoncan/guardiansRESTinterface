package es.us.alumn.miggoncan2.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.us.alumn.miggoncan2.model.entities.ShiftConfiguration;

public interface ShiftConfigurationRepository extends JpaRepository<ShiftConfiguration, Long> {
	
}

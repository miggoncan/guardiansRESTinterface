package es.us.alumn.miggoncan2.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.us.alumn.miggoncan2.model.entities.Doctor;

/**
 * This interfaces will autogenerate all the CRUD operations on the Doctor entity
 * 
 * @author miggoncan
 *
 */
public interface DoctorRepository extends JpaRepository<Doctor, Long>{

}

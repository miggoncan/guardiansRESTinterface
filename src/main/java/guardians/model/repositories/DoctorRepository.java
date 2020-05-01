package guardians.model.repositories;

import java.util.Optional;

import javax.persistence.Entity;

import org.springframework.data.jpa.repository.JpaRepository;

import guardians.model.entities.Doctor;

/**
 * This interface will be used by Jpa to auto-generate a class having all the
 * CRUD operations on the {@link Doctor} {@link Entity}. This operations will be
 * performed differently depending on the configured data-source. But this is
 * completely transparent to the application
 * 
 * @author miggoncan
 *
 */
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

	/**
	 * Retrieve a {@link Doctor} from the database if it exists, provided its
	 * firstName and lastNames
	 * 
	 * @param firstName The first name of the {@link Doctor}
	 * @param lastNames The lastNames of the {@link Doctor}
	 * @return The {@link Doctor}, if found
	 */
	public Optional<Doctor> findByFirstNameAndLastNames(String firstName, String lastNames);
}

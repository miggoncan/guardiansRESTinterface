package es.us.alumn.miggoncan2.model.entities;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * All classes that test the constraints of an Entity will extend this one
 * @author miggoncan
 *
 */
public class EntityTest {
		// Will be used to validate the constraints
		protected Validator validator;
		
		public EntityTest() {
			validator = Validation.buildDefaultValidatorFactory().getValidator();
		}
}

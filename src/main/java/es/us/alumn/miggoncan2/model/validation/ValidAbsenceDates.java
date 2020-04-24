/**
 * 
 */
package es.us.alumn.miggoncan2.model.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import es.us.alumn.miggoncan2.model.entities.Absence;

/**
 * @see ValidAbsenceDatesValidator
 * 
 * @author miggoncan
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
@Constraint(validatedBy = { ValidAbsenceDatesValidator.class })
public @interface ValidAbsenceDates {
	String message() default "{es.us.alumn.miggoncan2.model.entityvalidation.ValidAbsenceDates.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

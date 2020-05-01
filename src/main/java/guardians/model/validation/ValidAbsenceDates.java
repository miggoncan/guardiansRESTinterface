/**
 * 
 */
package guardians.model.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

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
	String message() default "{guardians.model.entityvalidation.ValidAbsenceDates.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

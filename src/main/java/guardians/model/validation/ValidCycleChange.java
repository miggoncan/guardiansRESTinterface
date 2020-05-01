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
 * @see ValidCycleChangeValidator
 * 
 * @author miggoncan
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
@Constraint(validatedBy = { ValidCycleChangeValidator.class })
public @interface ValidCycleChange {
	String message() default "{guardians.model.entityvalidation.ValidCycleChange.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

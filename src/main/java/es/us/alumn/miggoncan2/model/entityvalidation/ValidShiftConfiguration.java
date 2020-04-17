package es.us.alumn.miggoncan2.model.entityvalidation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
@Constraint(validatedBy = { ValidShiftConfigurationValidator.class })
/**
 * This Annotation is used to make sure that the number of minimum 
 * shifts a Doctor does is less than or equal to its maximum number 
 * of shifts. It also checks that the intersection of the shift 
 * preferences is empty. This means, for example, that the same 
 * AllowedShift cannot be selected both as an UnwantedShift and a 
 * MandatoryShift.
 * 
 * @author miggoncan
 */
public @interface ValidShiftConfiguration {
		String message() default "{es.us.alumn.miggoncan2.model.entityvalidation"
			+ ".ValidShiftConfiguration.message}";
		
		Class<?>[] groups() default {};
		
		Class <? extends Payload>[] payload() default {};
}

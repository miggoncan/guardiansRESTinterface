package es.us.alumn.miggoncan2.model.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * All classes that test the constraints of an Entity will extend this one
 * @author miggoncan
 *
 */
public class EntityTest {
		// Will be used to validate the constraints
		protected Validator validator;
		
		protected Set<ConstraintViolation<?>> constraintViolations;
		
		protected Class<?> testedEntityClass;
		
		/**
		 * @param testedEntityClass The Entity class that will be tested
		 */
		public EntityTest(Class<?> testedEntityClass) {
			log.debug("Creating a new EntityTest for class " + testedEntityClass.getCanonicalName());
			this.testedEntityClass = testedEntityClass;
			this.validator = Validation.buildDefaultValidatorFactory().getValidator();
		}
		
		/**
		 * This method checks if this.constraintViolations has a ConstraintViolation message 
		 * containing the parameter message
		 * 
		 * @param message The message to be looked for 
		 * @return True if the message was found. False otherwise
		 */
		protected boolean constraintViolationsContains(String message) {
			log.debug("Checking if the constrainViolations: " + this.constraintViolations
					+ " contain the message \"" + message + "\"");
			boolean containsMessage = false;
			for (ConstraintViolation<?> constraintViolation : this.constraintViolations) {
				if (constraintViolation.getMessage().contains(message)) {
					containsMessage = true;
					break;
				}
			}
			return containsMessage;
		}
		
		/**
		 * Check if value is valid for the attribute of the tested entity
		 * @param attribute The name of the attribute to be tested
		 * @param value The value to be tested
		 */
		protected void assertValidValue(String attribute, Object value) {
			log.debug("Testing the attribute " + attribute + " of the class " 
					+ this.testedEntityClass.getCanonicalName() + " can have value " + value);
			this.constraintViolations = new HashSet<>(
					validator.validateValue(this.testedEntityClass, attribute, value));
			log.debug("The returned contrainsViolations are: " + this.constraintViolations);
			assertEquals(0, constraintViolations.size());
		}
		
		/**
		 * Assert value is not valid for attribute. One of the resulting 
		 * constraint violations will contain expectedConstraintMessage
		 * @param attribute The name of the attribute to be tested
		 * @param value The value to be tested
		 * @param expectedConstraintMessage The message expected in the ConstraingViolations
		 */
		protected void assertInvalidValue(String attribute, Object value, String expectedConstraintMessage) {
			log.debug("Testing the attribute " + attribute + " of the class " 
					+ this.testedEntityClass.getCanonicalName() + " cannot have value " + value 
					+ ". Expected constraint violation is \"" + expectedConstraintMessage + "\"");
			constraintViolations = new HashSet<>(
					validator.validateValue(this.testedEntityClass, attribute, value));
			log.debug("The returned contrainsViolations are: " + this.constraintViolations);
			assertNotEquals(0, constraintViolations.size());
			assertTrue(this.constraintViolationsContains(expectedConstraintMessage));
		}
		
		/**
		 * Make sure the attribute cannot be blank
		 * @param attribute The name of the attribute to be tested
		 */
		protected void assertAttributeCannotBeBlank(String attribute) {
			log.debug("Testing the attribute " + attribute + " of the class " 
					+ this.testedEntityClass.getCanonicalName() + " cannot be blank");
			this.assertInvalidValue(attribute, null, "must not be blank");
			this.assertInvalidValue(attribute, "", "must not be blank");
			this.assertInvalidValue(attribute, "   ", "must not be blank");
		}
		
		/**
		 * Make sure the attribute cannot be null
		 * @param attribute The name of the attribute to be tested
		 */
		protected void assertAttributeCannotBeNull(String attribute) {
			log.debug("Testing the attribute " + attribute + " of the class " 
					+ this.testedEntityClass.getCanonicalName() + " cannot be null");
			this.assertInvalidValue(attribute, null, "must not be null");
		}
}

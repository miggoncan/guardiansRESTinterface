package es.us.alumn.miggoncan2.model.entityvalidation;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import es.us.alumn.miggoncan2.model.entities.AllowedShift;
import es.us.alumn.miggoncan2.model.entities.ShiftConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidShiftConfigurationValidator 
	implements ConstraintValidator<ValidShiftConfiguration, ShiftConfiguration> {
	@Override
	public boolean isValid(ShiftConfiguration value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		
		Integer min = value.getMinShifts();
		Integer max = value.getMaxShifts();
		boolean minMaxShiftsAreValid = min != null && max != null && min <= max;
		log.debug("The given shift configuration has valid min and max shifts: " + minMaxShiftsAreValid);
		
		boolean shiftsIntersect = false;
		if (minMaxShiftsAreValid) {
			Set<AllowedShift> unwantedShifts = value.getUnwantedShifts();
			Set<AllowedShift> unavailableShifts = value.getUnavailableShifts();
			Set<AllowedShift> wantedShifts = value.getWantedShifts();
			Set<AllowedShift> mandatoryShifts = value.getMandatoryShifts();
			// Create empty set if null
			unwantedShifts = unwantedShifts == null ? new HashSet<>() : unwantedShifts;
			log.debug("The unwantedShifts are: " + unwantedShifts);
			unavailableShifts = unavailableShifts == null ? new HashSet<>() : unavailableShifts;
			log.debug("The unavailableShifts are: " + unavailableShifts);
			wantedShifts = wantedShifts == null ? new HashSet<>() : wantedShifts;
			log.debug("The wantedShifts are: " + wantedShifts);
			mandatoryShifts = mandatoryShifts == null ? new HashSet<>() : mandatoryShifts;
			log.debug("The mandatoryShifts are: " + mandatoryShifts);
			// Using the stream api to check intersections between the sets
			shiftsIntersect = unwantedShifts.stream().anyMatch(unavailableShifts::contains)
					|| unwantedShifts.stream().anyMatch(wantedShifts::contains)
					|| unwantedShifts.stream().anyMatch(mandatoryShifts::contains)
					|| unavailableShifts.stream().anyMatch(wantedShifts::contains)
					|| unavailableShifts.stream().anyMatch(mandatoryShifts::contains)
					|| wantedShifts.stream().anyMatch(mandatoryShifts::contains);
			log.debug("The given shift configuration has valid shift preferences: " + !shiftsIntersect);
		}
		
		return minMaxShiftsAreValid && !shiftsIntersect;
	}
}

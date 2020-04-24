package es.us.alumn.miggoncan2.model.entities.serializers;

import java.io.IOException;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import es.us.alumn.miggoncan2.model.entities.AllowedShift;
import es.us.alumn.miggoncan2.model.entities.ShiftConfiguration;

/**
 * This class is responsible for serializing instances of
 * {@link ShiftConfiguration}
 * 
 * @author miggoncan
 */
public class ShiftConfigurationSerializer extends StdSerializer<ShiftConfiguration> {
	private static final long serialVersionUID = 6089719598744827992L;

	public ShiftConfigurationSerializer() {
		super((Class<ShiftConfiguration>) null);
	}

	@Override
	public void serialize(ShiftConfiguration value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeNumberField("id_facultativo", value.getDoctorId());
		gen.writeNumberField("min", value.getMinShifts());
		gen.writeNumberField("max", value.getMaxShifts());
		gen.writeBooleanField("pasa_consulta", value.getDoesConsultations());
		this.serializeShiftConfig(gen, "inconveniencia", value.getUnwantedShifts());
		this.serializeShiftConfig(gen, "imposibilidad", value.getUnavailableShifts());
		this.serializeShiftConfig(gen, "preferencia", value.getWantedShifts());
		this.serializeShiftConfig(gen, "necesidad", value.getMandatoryShifts());
		gen.writeEndObject();
	}

	/**
	 * This method serializes the given shiftPreferences using the given fieldName
	 * and the generator
	 *
	 * To know what shiftPreferences are, see {@link ShiftConfiguration}
	 * 
	 * Note, if shiftPreferences is null, no field will be added to the given
	 * generator, but if shiftPreferences is an empty Set, an empty array will be
	 * added with the given fieldName
	 * 
	 * @param gen              The generator used to serialize the shiftPreferences
	 * @param fieldName        The name used to serialize the array
	 * @param shiftPreferences The Set of AllowedShifts to be serialized
	 * @throws IOException
	 */
	private void serializeShiftConfig(JsonGenerator gen, String fieldName, Set<AllowedShift> shiftPreferences)
			throws IOException {
		if (shiftPreferences != null) {
			gen.writeArrayFieldStart(fieldName);
			for (AllowedShift shiftPreference : shiftPreferences) {
				gen.writeString(shiftPreference.getShift());
			}
			gen.writeEndArray();
		}
	}

}

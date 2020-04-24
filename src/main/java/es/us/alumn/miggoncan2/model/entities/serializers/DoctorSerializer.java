package es.us.alumn.miggoncan2.model.entities.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import es.us.alumn.miggoncan2.model.entities.Absence;
import es.us.alumn.miggoncan2.model.entities.Doctor;

/**
 * This class is responsible for serializing an instance of {@link Doctor} into
 * a Json object
 * 
 * @author miggoncan
 */
public class DoctorSerializer extends StdSerializer<Doctor> {
	private static final long serialVersionUID = 1698856415409918348L;

	public DoctorSerializer() {
		super((Class<Doctor>) null);
	}

	// TODO When a doctor gets serialized, the top level object is called "content". That should be changed so that no field name is used.
	// TODO When a list of doctors gets serialized, the list is added to the field doctorList. That should be changed so that no field name is used

	@Override
	public void serialize(Doctor doctor, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeNumberField("id_facultativo", doctor.getId());
		gen.writeStringField("nombre", doctor.getFirstName());
		gen.writeStringField("apellidos", doctor.getLastNames());
		gen.writeStringField("correo", doctor.getEmail());
		Absence absence = doctor.getAbsence();
		if (absence != null) {
			gen.writeObjectFieldStart("periodo_ausencia");
			gen.writeStringField("fecha_inicio", absence.getStart().toString());
			gen.writeStringField("fecha_finalizacion", absence.getEnd().toString());
			gen.writeEndObject();
		}
		gen.writeEndObject();
	}

}

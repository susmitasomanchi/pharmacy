package models.doctor;
import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;


public enum AppointmentStatus {
	@EnumValue("AVAILABLE")
	AVAILABLE,
	@EnumValue("REQUESTED")
	REQUESTED,
	@EnumValue("APPROVED")
	APPROVED,
	@EnumValue("CANCELLED")
	CANCELLED,
	@EnumValue("SERVED")
	SERVED;


	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final AppointmentStatus val : AppointmentStatus.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}

}

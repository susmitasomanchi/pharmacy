package models;
import java.util.LinkedHashMap;
import java.util.Map;

public enum AppointmentStatus {
	REQUESTED,APPROVED,CANCELLED,SERVED;


	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final AppointmentStatus val : AppointmentStatus.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}

}

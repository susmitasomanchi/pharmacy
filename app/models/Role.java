package models;

import java.util.LinkedHashMap;
import java.util.Map;

public enum Role {
	DOCTOR,PATIENT,PHARMACIST,MR,DOCTOR_ASSISTANT;

	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final Role val : Role.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}
}

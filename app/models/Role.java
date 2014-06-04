package models;

import java.util.LinkedHashMap;
import java.util.Map;

public enum Role {
	ADMIN,DOCTOR,PATIENT,PHARMACIST,MR,DIAGREP;

	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final Role val : Role.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}
}

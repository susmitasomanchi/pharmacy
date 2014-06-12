package models;

import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum Role {
	@EnumValue("ADMIN")
	ADMIN,
	@EnumValue("DOCTOR")
	DOCTOR,
	@EnumValue("DOCTOR_SECRETARY")
	DOCTOR_SECRETARY,
	@EnumValue("PATIENT")
	PATIENT,
	@EnumValue("ADMIN_PHARMACIST")
	ADMIN_PHARMACIST,
	@EnumValue("PHARMACIST")
	PHARMACIST,
	@EnumValue("MR")
	MR,
	@EnumValue("ADMIN_MR")
	ADMIN_MR,
	@EnumValue("DIAGREP")
	DIAGREP;

	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final Role val : Role.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}
}

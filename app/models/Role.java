package models;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;

import com.avaje.ebean.annotation.EnumValue;

public enum Role {

	@EnumValue("MEDNETWORK_ADMIN")
	MEDNETWORK_ADMIN,

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
	@EnumValue("ADMIN_MR")
	ADMIN_MR,
	@EnumValue("MR")
	MR,
	@EnumValue("ADMIN_DIAGREP")
	ADMIN_DIAGREP,
	@EnumValue("DIAGREP")
	DIAGREP,
	@EnumValue("CLINIC_ADMIN")
	CLINIC_ADMIN,
	@EnumValue("BLOG_ADMIN")
	BLOG_ADMIN;


	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final Role val : Role.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}

	public String capitalize(){
		return WordUtils.capitalizeFully(this.toString().replaceAll("_", " "));
	}

}

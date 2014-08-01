package models.pharmacist;

import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum PharmacyPrescriptionStatus {
	
	@EnumValue("RECEIVED")
	RECEIVED,
	
	@EnumValue("CONFIRMED")
	CONFIRMED,

	@EnumValue("SERVED")
	SERVED;

	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final PharmacyPrescriptionStatus val : PharmacyPrescriptionStatus.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}
}

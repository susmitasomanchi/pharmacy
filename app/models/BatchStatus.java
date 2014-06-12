package models;

import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum BatchStatus {

	@EnumValue("SUFFICIENT")
	SUFFICIENT,

	@EnumValue("NEARING_EXHAUSTION")
	NEARING_EXHAUSTION,

	@EnumValue("EXHAUSTED")
	EXHAUSTED,

	@EnumValue("APPROACHING_EXPIRY")
	APPROACHING_EXPIRY,

	@EnumValue("EXPIRED")
	EXPIRED;

	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final BatchStatus val : BatchStatus.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}



}

package models;
import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum Status {

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
		for (final Status val : Status.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}

}

package models;

import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum Country {

	@EnumValue("AFGHANISTAN")
	AFGHANISTAN,

	@EnumValue("INDIA")
	INDIA,

	@EnumValue("PAKISTAN")
	PAKISTAN,

	@EnumValue("UNITED STATES")
	UNITED_STATES;

	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final Country val : Country.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}
}

package models;

import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum DayOfTheWeek {
	@EnumValue("SUN")
	SUN,
	@EnumValue("MON")
	MON,
	@EnumValue("TUSE")
	TUSE,
	@EnumValue("WED")
	WED,
	@EnumValue("THURS")
	THURS,
	@EnumValue("FRI")
	FRI,
	@EnumValue("SAT")
	SAT;

	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final DayOfTheWeek val : DayOfTheWeek.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}
}

package models;

import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum DayOfTheWeek {
	@EnumValue("SUNDAY")
	SUNDAY,
	@EnumValue("MONDAY")
	MONDAY,
	@EnumValue("TUESDAY")
	TUESDAY,
	@EnumValue("WEDNESDAY")
	WEDNESDAY,
	@EnumValue("THURSDAY")
	THURSDAY,
	@EnumValue("FRIDAY")
	FRIDAY,
	@EnumValue("SATURDAY")
	SATURDAY;

	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final DayOfTheWeek val : DayOfTheWeek.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}
}

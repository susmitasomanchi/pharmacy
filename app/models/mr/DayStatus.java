package models.mr;

import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum DayStatus {
	@EnumValue("WORKING_DAY")
	WORKING_DAY,
	@EnumValue("HOLIDAY")
	HOLIDAY;

	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final DayStatus val : DayStatus.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}

}

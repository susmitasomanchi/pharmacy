package models;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;

import com.avaje.ebean.annotation.EnumValue;

public enum Sex {
	@EnumValue("MALE")
	MALE,
	@EnumValue("FEMALE")
	FEMALE,
	@EnumValue("OTHER")
	OTHER;

	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final Sex val : Sex.values()) {
			vals.put(val.toString(), WordUtils.capitalizeFully(val.toString().replaceAll("_", " ")));
		}
		return vals;
	}
}

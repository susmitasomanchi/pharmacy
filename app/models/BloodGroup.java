package models;

import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum BloodGroup {

	@EnumValue("A+")
	A_POSITIVE,
	@EnumValue("A-")
	A_NEGATIVE,
	@EnumValue("B+")
	B_POSITIVE,
	@EnumValue("B-")
	B_NEGATIVE,
	@EnumValue("AB+")
	AB_POSITIVE,
	@EnumValue("AB-")
	AB_NEGATIVE,
	@EnumValue("O+")
	O_POSITIVE,
	@EnumValue("O-")
	O_NEGATIVE;

	public static String sign(final String sign){
		if(sign.equalsIgnoreCase("POSITIVE")) {
			return "+";
		} else {
			return "-";
		}
	}

	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final BloodGroup val : BloodGroup.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}


}

package models.mr;
import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum EmploymentStatus {

	@EnumValue("CONFIRMED")
	CONFIRMED,

	@EnumValue("SUSPENDED")
	SUSPENDED,

	@EnumValue("PROBATION")
	PROBATION,

	@EnumValue("RESIGNED")
	RESIGNED,

	@EnumValue("RELIEVED")
	RELIEVED;


	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final EmploymentStatus val : EmploymentStatus.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}
}

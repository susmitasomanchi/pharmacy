package models.mr;

import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum DCRStatus {

	@EnumValue("DRAFT")
	DRAFT,
	@EnumValue("SUBMITTED")
	SUBMITTED,
	@EnumValue("APPROVED")
	APPROVED,
	@EnumValue("REJECTED")
	REJECTED,
	@EnumValue("REOPENED")
	REOPENED;

	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final DCRStatus val : DCRStatus.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}

}

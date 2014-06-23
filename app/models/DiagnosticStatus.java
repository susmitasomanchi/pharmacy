package models;

import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum DiagnosticStatus {
	
	@EnumValue("CONFIRMED")
	CONFIRMED,
	
	@EnumValue("SAMPLE_COLLECTED")
	SAMPLE_COLLECTED,
	
	@EnumValue("REPORT_READY")
	REPORT_READY;

	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final DiagnosticStatus val : DiagnosticStatus.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}

}

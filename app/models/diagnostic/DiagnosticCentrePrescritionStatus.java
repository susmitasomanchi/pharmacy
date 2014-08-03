package models.diagnostic;

import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum DiagnosticCentrePrescritionStatus {
	
	@EnumValue("RECEIVED")
	RECEIVED,

	@EnumValue("CONFIRMED")
	CONFIRMED,
	
	@EnumValue("CANCELLED")
	CANCELLED,
	
	@EnumValue("SERVED")
	SERVED;



	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final DiagnosticCentrePrescritionStatus val : DiagnosticCentrePrescritionStatus.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}


}

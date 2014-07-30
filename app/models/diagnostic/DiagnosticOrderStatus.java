package models.diagnostic;

import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum DiagnosticOrderStatus {

	@EnumValue("ORDER_RECEIVED")
	ORDER_RECEIVED,

	@EnumValue("ORDER_CONFIRMED")
	ORDER_CONFIRMED,
	
	@EnumValue("ORDER_CANCELLED")
	ORDER_CANCELLED;

	

	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final DiagnosticOrderStatus val : DiagnosticOrderStatus.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}

}

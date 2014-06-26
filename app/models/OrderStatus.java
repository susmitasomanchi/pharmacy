package models;

import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum OrderStatus {
	@EnumValue("DRAFT")
	DRAFT,

	@EnumValue("READY")
	READY,

	@EnumValue("DELIVERED")
	DELIVERED;

	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final OrderStatus val : OrderStatus.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}
}

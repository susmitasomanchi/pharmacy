package models;

import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum ProductInventoryStatus {

	@EnumValue("AVAILABLE")
	AVAILABLE,
	@EnumValue("OUT_OF_STOCK")
	OUT_OF_STOCK;

	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final ProductInventoryStatus val : ProductInventoryStatus.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}

}

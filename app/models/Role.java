package models;

import java.util.LinkedHashMap;
import java.util.Map;

public enum Role {
	ADMIN,KMPO,COORDINATOR,USER;
	
	public static Map<String, String> options() {
		LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (Role val : Role.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}
}

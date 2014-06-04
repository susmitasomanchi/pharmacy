package models;
import java.util.LinkedHashMap;
import java.util.Map;

public enum Status {
	REQUESTED,APPROVED,CANCELLED,SERVED;


	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final Status val : Status.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}

}

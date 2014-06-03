package models;
import java.util.LinkedHashMap;
import java.util.Map;

public enum Status {
	REQUEST,APPROVE,CANCEL;
	
	
	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final Status val : Status.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}

}

package models.diagnostic;

import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum DiagnosticReportStatus {

	@EnumValue("SAMPLE_NOT_COLLECTED")
	SAMPLE_NOT_COLLECTED,

	@EnumValue("SAMPLE_COLLECTED")
	SAMPLE_COLLECTED,

	@EnumValue("REPORT_READY")
	REPORT_READY;

	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final DiagnosticOrderStatus val : DiagnosticOrderStatus.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}

}

package models;

import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum BloodGroup {

	@EnumValue("OPLUS")
	OPLUS,

	@EnumValue("OMINUS")
	OMINUS,

	@EnumValue("APLUS")
	APLUS,

	@EnumValue("AMINUS")
	AMINUS,

	@EnumValue("BPLUS")
	BPLUS,

	@EnumValue("BMINUS")
	BMINUS,

	@EnumValue("ABPLUS")
	ABPLUS,

	@EnumValue("ABMINUS")
	ABMINUS;



	public static Map<BloodGroup, String> options() {
		final LinkedHashMap<BloodGroup, String> vals = new LinkedHashMap<BloodGroup, String>();
		for (final BloodGroup group : BloodGroup.values()) {
			vals.put(group, group.toString().replaceAll("PLUS", "+").replaceAll("MINUS", "-"));
		}
		return vals;
	}

	public String capitalize(){
		return this.toString().replaceAll("PLUS", "+").replaceAll("MINUS", "-");
	}
}

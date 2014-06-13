package models;
import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum Language {

	@EnumValue("ASSAMESE")
	ASSAMESE,
	
	@EnumValue("BENGALI")
	BENGALI,
	
	@EnumValue("BODO")
	BODO,
	
	@EnumValue("DOGRI")
	DOGRI,
	
	@EnumValue("ENGLISH")
	ENGLISH,

	@EnumValue("GUJARATI")
	GUJARATI,
	
	@EnumValue("HINDI")
	HINDI,
	
	@EnumValue("KANNADA")
	KANNADA,
	
	@EnumValue("KASHMIRI")
	KASHMIRI,
	
	@EnumValue("KONKANI")
	KONKANI,
	
	@EnumValue("MAITHILI")
	MAITHILI,
	
	@EnumValue("MALAYALAM")
	MALAYALAM,
	
	@EnumValue("MANIPURI")
	MANIPURI,

	@EnumValue("MARATHI")
	MARATHI,
	
	@EnumValue("NEPALI")
	NEPALI,
	
	@EnumValue("ORIYA")
	ORIYA,
	
	@EnumValue("PUNJABI")
	PUNJABI,
	
	@EnumValue("SANTALI")
	SANTALI,
	
	@EnumValue("SINDHI")
	SINDHI,
	
	@EnumValue("TAMIL")
	TAMIL,
	
	@EnumValue("TELGU")
	TELGU,
	
	@EnumValue("URDU")
	URDU;
	
	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final Status val : Status.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}

}

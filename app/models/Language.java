package models;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;

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

	@EnumValue("TELUGU")
	TELUGU,

	@EnumValue("URDU")
	URDU;

	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final Language val : Language.values()) {
			vals.put(val.toString(), WordUtils.capitalizeFully(val.toString().replaceAll("_", " ")));
		}
		return vals;
	}


	public String capitalize(){
		return WordUtils.capitalizeFully(this.toString().replaceAll("_", " "));
	}

}

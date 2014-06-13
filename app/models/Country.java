package models;

import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum Country {

	@EnumValue("AFGHANISTAN")
	AFGHANISTAN,

	@EnumValue("AKROTIRI")
	AKROTIRI,

	@EnumValue("ALBANIA")
	ALBANIA,

	@EnumValue("ALGERIA")
	ALGERIA,

	@EnumValue("AMERICAN_SAMOA")
	AMERICAN_SAMOA,

	@EnumValue("ANDORRA")
	ANDORRA,

	@EnumValue("ANGOLA")
	ANGOLA,

	@EnumValue("ANGUILLA")
	ANGUILLA,

	@EnumValue("ANTARCTICA")
	ANTARCTICA ,

	@EnumValue("ANTIGUA_AND_BARBUDA")
	ANTIGUA_AND_BARBUDA ,

	@EnumValue("ARGENTINA")
	ARGENTINA,

	@EnumValue("ARMENIA")
	ARMENIA,

	@EnumValue("ARUBA")
	ARUBA,

	@EnumValue("ASHMORE_AND_CARTIER_ISLANDS")
	ASHMORE_AND_CARTIER_ISLANDS ,

	@EnumValue("AUSTRALIA")
	AUSTRALIA,

	@EnumValue("AUSTRIA")
	AUSTRIA,

	@EnumValue("AZERBAIJAN")
	AZERBAIJAN,

	@EnumValue("BAHRAIN")
	BAHRAIN,

	@EnumValue("BANGLADESH")
	BANGLADESH,

	@EnumValue("BARBADOS")
	BARBADOS,

	@EnumValue("BELARUS")
	BELARUS,

	@EnumValue("BELGIUM")
	BELGIUM,

	@EnumValue("BELIZE")
	BELIZE,

	@EnumValue("BENIN")
	BENIN,

	@EnumValue("BERMUDA")
	BERMUDA,

	@EnumValue("BHUTAN")
	BHUTAN,

	@EnumValue("BOLIVIA")
	BOLIVIA,

	@EnumValue("BOSNIA_AND_HERZEGOVINA")
	BOSNIA_AND_HERZEGOVINA,

	@EnumValue("BOTSWANA")
	BOTSWANA,

	@EnumValue("BOUVET_ISLAND")
	BOUVET_ISLAND,

	@EnumValue("BRAZIL")
	BRAZIL,

	@EnumValue("BRUNEI")
	BRUNEI,

	@EnumValue("BULGARIA")
	BULGARIA,

	@EnumValue("BURKINA_FASO")
	BURKINA_FASO,

	@EnumValue("BURMA")
	BURMA,

	@EnumValue("BURUNDI")
	BURUNDI,

	@EnumValue("CAMBODIA")
	CAMBODIA,

	@EnumValue("CAMEROON")
	CAMEROON,

	@EnumValue("CANADA")
	CANADA,

	@EnumValue("CAPE_VERDE")
	CAPE_VERDE,

	@EnumValue("CHAD")
	CHAD,

	@EnumValue("CHILE")
	CHILE,

	@EnumValue("CHINA")
	CHINA,

	@EnumValue("COLOMBIA")
	COLOMBIA,

	@EnumValue("COMOROS")
	COMOROS,

	@EnumValue("CUBA")
	CUBA,

	@EnumValue("CYPRUS")
	CYPRUS,

	@EnumValue("DENMARK")
	DENMARK,

	@EnumValue("DHEKELIA")
	DHEKELIA,

	@EnumValue("DJIBOUTI")
	DJIBOUTI,

	@EnumValue("DOMINICA")
	DOMINICA,

	@EnumValue("ECUADOR")
	ECUADOR,

	@EnumValue("EGYPT")
	EGYPT,

	@EnumValue("INDIA")
	INDIA,

	@EnumValue("PAKISTAN")
	PAKISTAN,

	@EnumValue("UNITED_STATES")
	UNITED_STATES;

	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final Country val : Country.values()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}
}

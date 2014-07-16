package models;

import java.util.Map;
import java.util.TreeMap;

import com.avaje.ebean.annotation.EnumValue;

public enum State {

	@EnumValue("ANDHRA_PRADESH")
	ANDHRA_PRADESH,

	@EnumValue("ARUNACHAL_PRADESH")
	ARUNACHAL_PRADESH,

	@EnumValue("ASSAM")
	ASSAM,

	@EnumValue("BIHAR")
	BIHAR,

	@EnumValue("CHHATTISGARH")
	CHHATTISGARH,

	@EnumValue("GOA")
	GOA,

	@EnumValue("GUJARAT")
	GUJARAT,

	@EnumValue("HARYANA")
	HARYANA,

	@EnumValue("HIMACHAL_PRADESH")
	HIMACHAL_PRADESH,

	@EnumValue("JAMMU_AND_KASHMIR")
	JAMMU_AND_KASHMIR,

	@EnumValue("JHARKHAND")
	JHARKHAND,

	@EnumValue("KARNATAKA")
	KARNATAKA,

	@EnumValue("KERALA")
	KERALA,

	@EnumValue("MADHYA_PRADESH")
	MADHYA_PRADESH,

	@EnumValue("MAHARASHTRA")
	MAHARASHTRA,

	@EnumValue("MANIPUR")
	MANIPUR,

	@EnumValue("MEGHALAYA")
	MEGHALAYA,

	@EnumValue("MIZORAM")
	MIZORAM,

	@EnumValue("NAGALAND")
	NAGALAND,

	@EnumValue("ODISHA")
	ODISHA,

	@EnumValue("PUNJAB")
	PUNJAB,

	@EnumValue("RAJASTHAN")
	RAJASTHAN,

	@EnumValue("SIKKIM")
	SIKKIM,

	@EnumValue("TAMIL_NADU")
	TAMIL_NADU,

	@EnumValue("TELANGANA")
	TELANGANA,

	@EnumValue("TRIPURA")
	TRIPURA,

	@EnumValue("UTTAR_PRADESH")
	UTTAR_PRADESH,


	@EnumValue("UTTARAKHAND")
	UTTARAKHAND,

	@EnumValue("WEST_BENGAL")
	WEST_BENGAL,

	@EnumValue("ANDAMAN_AND_NICOBAR_ISLANDS")
	ANDAMAN_AND_NICOBAR_ISLANDS,

	@EnumValue("CHNADIGARH")
	CHNADIGARH,

	@EnumValue("DADRA_AND_NAGAR_HAVELI")
	DADRA_AND_NAGAR_HAVELI,

	@EnumValue("DAMAN_AND_DIU")
	DAMAN_AND_DIU,

	@EnumValue("LAKSHADWEEP")
	LAKSHADWEEP,

	@EnumValue("NATIONAL_CAPITAL_TERRITORY_OF_DELHI")
	NATIONAL_CAPITAL_TERRITORY_OF_DELHI,

	@EnumValue("PUDUCHERRY")
	PUDUCHERRY;

	public static Map<String, String> options() {
		final TreeMap<String, String> vals = new TreeMap<String, String>();
		for (final State val : State.values()) {
			vals.put(val.toString(), val.toString().replaceAll("_", " "));
		}
		return vals;
	}
}

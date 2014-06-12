package models;

import java.util.LinkedHashMap;
import java.util.Map;

import com.avaje.ebean.annotation.EnumValue;

public enum State {

	@EnumValue("ANDHRA PRADESH")
	ANDHRA_PRADESH,

	@EnumValue("ARUNACHAL PRADESH")
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

	@EnumValue("HIMACHAL PRADESH")
	HIMACHAL_PRADESH,

	@EnumValue("JAMMU & KASHMIR")
	JAMMU_AND_KASHMIR,

	@EnumValue("JHARKHAND")
	JHARKHAND,

	@EnumValue("KARNATAKA")
	KARNATAKA,

	@EnumValue("KERALA")
	KERALA,


	@EnumValue("MADHYA PRADESH")
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

	@EnumValue("TAMIL NADU")
	TAMIL_NADU,

	@EnumValue("TELANGANA")
	TELANGANA,

	@EnumValue("TRIPURA")
	TRIPURA,

	@EnumValue("UTTAR PRADESH")
	UTTAR_PRADESH,


	@EnumValue("UTTARAKHAND")
	UTTARAKHAND,

	@EnumValue("WEST BENGAL")
	WEST_BENGAL,

	@EnumValue("ANDAMAN AND NICOBAR ISLANDS")
	ANDAMAN_AND_NICOBAR_ISLANDS,

	@EnumValue("CHNADIGARH")
	CHNADIGARH,

	@EnumValue("DADRA AND NAGAR HAVELI")
	DADRA_AND_NAGAR_HAVELI,

	@EnumValue("DAMAN AND DIU")
	DAMAN_AND_DIU,

	@EnumValue("LAKSHADWEEP")
	LAKSHADWEEP,

	@EnumValue("NATIONAL CAPITAL TERRITORY OF DELHI")
	NATIONAL_CAPITAL_TERRITORY_OF_DELHI,

	@EnumValue("PUDUCHERRY")
	PUDUCHERRY;

	public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final State val : State.values()) {
			vals.put(val.toString(), val.toString().replace("_", " "));
		}
		return vals;
	}
}

package models;

import com.avaje.ebean.annotation.EnumValue;

public enum Sex {
	@EnumValue("MALE")
	MALE,
	@EnumValue("FEMALE")
	FEMALE,
	@EnumValue("OTHER")
	OTHER
}

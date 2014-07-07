package models.blog;

import com.avaje.ebean.annotation.EnumValue;

public enum BlogCommentatorType {

	@EnumValue("APP_USER")
	APP_USER,
	@EnumValue("GOOGLE")
	GOOGLE,
	@EnumValue("FACEBOOK")
	FACEBOOK;

}

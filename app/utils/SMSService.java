
package utils;
import models.AppUser;
import play.Logger;
import play.libs.WS;

public class SMSService {

	public static void sendSMS(final String mobileNumber, final String message){

		//@TODO Send the SMS

		WS.url("http://localhost:9000/test222").get();

		Logger.info("SMS sent to "+mobileNumber+": "+message);
	}

	public static void sendConfirmationSMS(final AppUser appUser){
		appUser.mobileNumberConfirmationKey="0";
		/*
		appUser.mobileNumberConfirmationKey = RandomStringUtils.randomAlphanumeric(5).toLowerCase();
		 */
		//@TODO Send the SMS

		appUser.update();
		Logger.info("Confirmation code sent to: "+appUser.mobileNumber+" code:"+appUser.mobileNumberConfirmationKey);
	}


}

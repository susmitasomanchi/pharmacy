
package utils;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import models.AppUser;
import play.Logger;

public class SMSService {

	public static void sendSMS(final String mobileNumber, final String message){

		//@TODO Send the SMS

		Logger.info("SMS sent to "+mobileNumber+": "+message);
	}

	public static void sendConfirmationSMS(final AppUser appUser){
		final Random random = new SecureRandom();
		final String randomString = new BigInteger(6, random).toString();
		appUser.mobileNumberConfirmationKey = randomString;

		//@TODO Send the SMS

		appUser.update();
		Logger.info("Confirmation code sent to: "+appUser.mobileNumber+" code:"+appUser.mobileNumberConfirmationKey);
	}


}

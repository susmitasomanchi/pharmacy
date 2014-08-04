package utils;

import play.Logger;

public class SMSService {

	public void sendSMS(final String mobileNumber, final String message){
		Logger.info("SMS sent to "+mobileNumber+": "+message);
	}


}

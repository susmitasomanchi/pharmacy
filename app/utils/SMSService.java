
package utils;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import models.AppUser;

import org.apache.commons.lang3.RandomStringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import play.Logger;

public class SMSService {

	public static void sendSMS(final String mobileNumber, final String message){
		final String callUrl = "http://sms.proactivesms.in/sendsms.jsp?user=sanskrit&password=sanskrit&mobiles="+mobileNumber.trim()+"&sms="+message.trim().replaceAll(" ","%20")+"&senderid=MEDNET";
		try{
			final URL url = new URL(callUrl);
			final URLConnection connection = url.openConnection();
			DocumentBuilderFactory objDocumentBuilderFactory = null;
			DocumentBuilder objDocumentBuilder = null;
			Document doc = null;
			objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
			objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();
			doc = objDocumentBuilder.parse(connection.getInputStream());
			final NodeList descNodes = doc.getElementsByTagName("smslist");
			for(int i=0; i<descNodes.getLength();i++){
				System.out.println(descNodes.item(i).getNodeName()+": "+descNodes.item(i).getTextContent());
			}
			Logger.info("SMS sent to "+mobileNumber+": "+message);
		}
		catch (final Exception e){
			Logger.error("Error while sending ");
		}
	}

	public static void sendConfirmationSMS(final AppUser appUser){
		appUser.mobileNumberConfirmationKey = RandomStringUtils.randomAlphanumeric(5).toLowerCase();
		appUser.update();
		sendSMS(Long.toString(appUser.mobileNumber),"Your mobile confirmation code is "+appUser.mobileNumberConfirmationKey);
		Logger.info("Confirmation code sent to: "+appUser.mobileNumber+" code:"+appUser.mobileNumberConfirmationKey);
	}


}

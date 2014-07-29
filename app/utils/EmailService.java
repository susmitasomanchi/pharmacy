package utils;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

public class EmailService {
	/**
	 * @author Mitesh
	 * Action to send mail user to conform its email-id
	 * NO URL
	 */
	public static boolean sendSimpleEMail(final String receiverEmailId, final String subject, final String message) {
		boolean result=true;
		try{
			final Email email = new SimpleEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator("assistant@greensoftware.in", "test.assistant"));
			email.setSSLOnConnect(true);
			email.setFrom("assistant@greensoftware.in");
			email.setSubject(subject);
			email.setMsg(message);
			email.addTo(receiverEmailId);
			email.send();
			System.out.println("Mail Sent Successfully!");
		}
		catch (final Exception e){
			System.out.println("ERROR While Sending Email");
			e.printStackTrace();
			result=false;
		}

		return result;
	}
	public static boolean sendConformationEmail(final String receiverEmailId,final Long appUserId){
		final Random random = new SecureRandom();
		final byte []bytes=new byte[32];
		random.nextBytes(bytes);
		boolean result=true;
		try{
			final Email email = new SimpleEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator("assistant@greensoftware.in", "test.assistant"));
			email.setSSLOnConnect(true);
			email.setFrom("assistant@greensoftware.in");
			email.setSubject("Conformation Email");
			email.setMsg(appUserId+"/"+bytes);
			email.addTo(receiverEmailId);
			email.send();
			System.out.println("Mail Sent Successfully!");
		}
		catch (final Exception e){
			System.out.println("ERROR While Sending Email");
			e.printStackTrace();
			result=false;
		}

		return result;
	}

}


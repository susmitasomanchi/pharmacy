package utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import models.AppUser;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import play.Logger;
import controllers.LoginController;

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
		final String randomString = new BigInteger(130, random).toString(32);
		boolean result=true;
		try{
			final StringBuilder builder=new StringBuilder();
			builder.append("<html>");
			builder.append("<a href=localhost:9000/user/confirmation/");
			builder.append(appUserId);
			builder.append("/"+randomString +">");
			builder.append("<b>click here</b>");
			builder.append("</a>");
			builder.append("</html>");
			final HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator("mitesh.greensoftware@gmail.com", "mitesh@greensoftware.in"));
			email.setSSLOnConnect(true);
			email.setFrom("assistant@greensoftware.in");
			email.setSubject("Conformation Email");
			email.setHtmlMsg(builder.toString());
			email.addTo(receiverEmailId);
			email.send();
			System.out.println("Mail Sent Successfully!");
			final AppUser appUser=LoginController.getLoggedInUser();
			appUser.emailConfirmationKey=randomString;
			appUser.update();
			Logger.info(builder.toString());
		}
		catch (final Exception e){
			System.out.println("ERROR While Sending Email");
			e.printStackTrace();
			result=false;
		}

		return result;
	}

}


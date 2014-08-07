package utils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import javax.activation.DataSource;

import models.AppUser;
import models.FileEntity;

import org.apache.commons.mail.*;

import play.Logger;

public class EmailService {
	/**
	 * @author Mitesh
	 * Action to send simple email
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

	/**
	 * @author Mitesh
	 * Action to send mail user to conform its email-id
	 * NO URL
	 */
	public static boolean sendConfirmationEmail(final AppUser appUser){
		final Random random = new SecureRandom();
		final String randomString = new BigInteger(130,random).toString(32);
		boolean result = true;
		try{
			final StringBuilder builder=new StringBuilder();
			builder.append("<html><body>");
			builder.append("<a href=\"http://mednetwork.in/user/confirmation/");
			builder.append(appUser.id);
			builder.append("/"+randomString +"\">");
			builder.append("<b>click here</b>");
			builder.append("</a>");
			builder.append("</body></html>");
			final HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator("mitesh.greensoftware@gmail.com", "mitesh@greensoftware.in"));
			email.setSSLOnConnect(true);
			email.setFrom("assistant@greensoftware.in");
			email.setSubject("Conformation Email");
			email.setHtmlMsg(builder.toString());
			email.addTo(appUser.email);
			email.send();
			System.out.println("Mail Sent Successfully!");
			appUser.emailConfirmationKey = randomString;
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

	public static void sendHTMLEmailWithAttachments(final String receiverEmailId, final String subject, final String htmlMessage, final List<FileEntity> fileEntityList){



	}

}


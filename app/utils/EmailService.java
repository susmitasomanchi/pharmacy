package utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import models.AppUser;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

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

	public void sendHTMLEmailWithAttachments(final String receiverEmailId, final String subject, final String htmlMessage, final List<byte[]> imageList){

		// Create the attachment
		final EmailAttachment attachment = new EmailAttachment();
		attachment.setPath("mypictures/john.jpg");
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setDescription("");
		attachment.setName("filename");

		// Create the email message
		final MultiPartEmail email = new MultiPartEmail();
		email.setHostName("mail.myserver.com");
		try {
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator("mitesh.greensoftware@gmail.com", "mitesh@greensoftware.in"));
			email.setSSLOnConnect(true);
			email.setFrom("me@apache.org", "Me");
			email.setSubject("The picture");
			email.setMsg("Here is the picture you wanted");
			// add the attachment
			email.attach(attachment);
			email.send();
		} catch (final EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		// send the email

	}

}


package utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

import javax.activation.DataSource;

import models.AppUser;
import models.FileEntity;
import models.clinic.ClinicUser;
import models.doctor.Appointment;
import models.doctor.Clinic;
import models.doctor.Doctor;

import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
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
			email.setAuthenticator(new DefaultAuthenticator(Constants.EMAIL_ID, Constants.EMAIL_PASSWORD));
			email.setSSLOnConnect(true);
			email.setFrom(Constants.EMAIL_ID,"MedNetwork");
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
			builder.append("<p>Dear "+appUser.name+",<br><br>Thank you for signing up at MedNetwork. Please ");
			builder.append("<a href=\"http://mednetwork.in/secure-user/confirmation/");
			builder.append(appUser.id);
			builder.append("/"+randomString +"\">");
			builder.append("<b>click here</b>");
			builder.append("</a> to confirm your email address.<br><br>Best regards,<br>MedNetwork.in</p>");
			builder.append("</body></html>");
			final HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator(Constants.EMAIL_ID, Constants.EMAIL_PASSWORD));
			email.setSSLOnConnect(true);
			email.setFrom(Constants.EMAIL_ID,"MedNetwork");
			email.setSubject("Confirm your account at MedNetwork");
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

	@SuppressWarnings({ "deprecation", "deprecation" })
	public static void sendHTMLEmailWithAttachments(final String receiverEmailId, final String subject, final String htmlMessage, final List<FileEntity> files){


		// Create the email message
		final MultiPartEmail email = new MultiPartEmail();
		email.setHostName("mail.myserver.com");
		try {
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator(Constants.EMAIL_ID, Constants.EMAIL_PASSWORD));
			email.setSSLOnConnect(true);
			email.setFrom(Constants.EMAIL_ID,"MedNetwork");
			email.setSubject("The picture");
			email.setMsg("Here is the picture you wanted");
			email.addTo(receiverEmailId);
			// add the attachment
			for (final FileEntity fileEntity : files) {
				if(fileEntity.mimeType.compareToIgnoreCase(null) == 0 || fileEntity.mimeType.compareToIgnoreCase("") == 0 ){
					fileEntity.mimeType = "application/pdf";
				}
				final DataSource source = new ByteArrayDataSource(new ByteArrayInputStream(fileEntity.byteContent), fileEntity.mimeType);
				email.attach(source,fileEntity.fileName,"No description");
			}
			email.send();
		} catch (final EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		// send the email

	}



	/**
	 * Action to send mail to appUser's email id to change password(forgot password)
	 * NO URL
	 */
	public static void sendForgotPasswordEmail(final AppUser appUser){
		final Random random = new SecureRandom();
		final String randomString = new BigInteger(130,random).toString(32);
		boolean result = true;
		try{
			final StringBuilder builder=new StringBuilder();
			builder.append("<html><body>");
			builder.append("<p>Dear "+appUser.name+",<br><br>To reset your password, please ");
			builder.append("<a href=\"http://mednetwork.in/secure-forgot-reset-password/");
			builder.append(appUser.id);
			builder.append("/"+randomString +"\">");
			builder.append("<b>click here.</b>");
			builder.append("</a><br><br>In case you haven't raised any request for resetting your password, kindly ignore this mail. You can continue using your existing password.<br><br>Best regards,<br>MedNetwork.in</p>");
			builder.append("</body></html>");
			final HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator(Constants.EMAIL_ID, Constants.EMAIL_PASSWORD));
			email.setSSLOnConnect(true);
			email.setFrom(Constants.EMAIL_ID,"MedNetwork");
			email.setSubject("Reset password at MedNetwork");
			email.setHtmlMsg(builder.toString());
			email.addTo(appUser.email);
			email.send();
			System.out.println("Mail Sent Successfully!");
			appUser.forgotPasswordConfirmationKey = randomString;
			appUser.update();
			Logger.info(builder.toString());
		}
		catch (final Exception e){
			System.out.println("ERROR While Sending Email");
			e.printStackTrace();
			result=false;
		}
	}

	public static boolean sendVerificationConformMessage(final AppUser appUser) {
		boolean result = true;
		try{
			final StringBuilder builder=new StringBuilder();
			builder.append("<html><body>");
			builder.append("<p>Dear "+appUser.name+",<br><br>Thank you for verifying your email id.");
			builder.append("<br><br>Best regards,<br>MedNetwork.in</p>");
			builder.append("</body></html>");
			final HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator(Constants.EMAIL_ID, Constants.EMAIL_PASSWORD));
			email.setSSLOnConnect(true);
			email.setFrom(Constants.EMAIL_ID,"MedNetwork");
			email.setSubject("MedNetwork Account Verified");
			email.setHtmlMsg(builder.toString());
			email.addTo(appUser.email);
			email.send();
			Logger.info("Mail Sent Successfully!");
			Logger.info(builder.toString());
		}
		catch (final Exception e){
			System.out.println("ERROR While Sending Email");
			e.printStackTrace();
			result=false;
		}
		return result;
	}

	public static boolean sendAppointmentConformMail(final AppUser requestBy ,final AppUser requestTo,final Appointment appointment) {

		boolean result = true;
		try{
			final StringBuilder builder=new StringBuilder();
			builder.append("<html><body>");
			builder.append("<p>Dear "+requestBy.name+",<br><br>Your appointment has been booked, details are as follows");
			builder.append("<br><br><b>Appointment Date:</b>"+ new SimpleDateFormat("dd-MMM-yyyy").format(appointment.appointmentTime));
			builder.append("<br><br><b>Appointment Time:</b>"+ new SimpleDateFormat("hh:mm").format(appointment.appointmentTime));
			builder.append("<br><br><b>Clinic          :</b>"+ appointment.doctorClinicInfo.clinic.name);
			builder.append("<br><br><b>Doctor          :</b>"+ appointment.doctorClinicInfo.doctor.appUser.name);

			builder.append("<br><br>Best regards,<br>MedNetwork.in</p>");
			builder.append("</body></html>");
			final HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator(Constants.EMAIL_ID, Constants.EMAIL_PASSWORD));
			email.setSSLOnConnect(true);
			email.setFrom(Constants.EMAIL_ID,"MedNetwork");
			email.setSubject("An appointment has been booked at "+appointment.doctorClinicInfo.clinic.name+".");
			email.setHtmlMsg(builder.toString());
			email.addTo(requestBy.email);
			email.send();
			Logger.info("Mail Sent Successfully!");
			Logger.info(builder.toString());
		}
		catch (final Exception e){
			System.out.println("ERROR While Sending Email");
			e.printStackTrace();
			result=false;
		}
		try{
			final StringBuilder builder=new StringBuilder();
			builder.append("<html><body>");
			builder.append("<p>Dear Dr. "+requestTo.name+",<br><br>An appointment has been booked,details are as follows");
			builder.append("<br><br><b>Appointment Date			:</b>"+ new SimpleDateFormat("dd-MMM-yyyy").format(appointment.appointmentTime));
			builder.append("<br><br><b>Appointment Time			:</b>"+ new SimpleDateFormat("hh:mm").format(appointment.appointmentTime));
			builder.append("<br><br><b>Clinic          			:</b>"+ appointment.doctorClinicInfo.clinic.name);
			builder.append("<br><br><b>Patient                  :</b>"+ requestBy.name+"("+requestBy.getPatient().getSexAndAge()+")");

			builder.append("<br><br>Best regards,<br>MedNetwork.in</p>");
			builder.append("</body></html>");
			final HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator(Constants.EMAIL_ID, Constants.EMAIL_PASSWORD));
			email.setSSLOnConnect(true);
			email.setFrom(Constants.EMAIL_ID,"MedNetwork");
			email.setSubject("An appointment has been booked at "+appointment.doctorClinicInfo.clinic.name+".");
			email.setHtmlMsg(builder.toString());
			email.addTo(requestTo.email);
			email.send();
			Logger.info("Mail Sent Successfully!");
			Logger.info(builder.toString());
		}
		catch (final Exception e){
			System.out.println("ERROR While Sending Email");
			e.printStackTrace();
			result=false;
		}
		return result;
	}

	public static boolean sendPrescriptionSaveMessage(final AppUser requestBy ,final AppUser requestTo) {

		boolean result = true;
		try{
			final StringBuilder builder=new StringBuilder();
			builder.append("<html><body>");
			builder.append("<p>Dear "+requestBy.name+",<br><br>Your prescription by Dr. "+requestTo.name+" has been saved.");

			builder.append("<br><br>Best regards,<br>MedNetwork.in</p>");
			builder.append("</body></html>");
			final HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator(Constants.EMAIL_ID, Constants.EMAIL_PASSWORD));
			email.setSSLOnConnect(true);
			email.setFrom(Constants.EMAIL_ID,"MedNetwork");
			email.setSubject("Your Prescription has been saved at MedNetwork");
			email.setHtmlMsg(builder.toString());
			email.addTo(requestBy.email);
			email.send();
			Logger.info("Mail Sent Successfully!");
			Logger.info(builder.toString());
		}
		catch (final Exception e){
			System.out.println("ERROR While Sending Email");
			e.printStackTrace();
			result=false;
		}
		try{
			final StringBuilder builder=new StringBuilder();
			builder.append("<html><body>");
			builder.append("<p>Dear Dr."+requestTo.name+",<br><br>Your prescription for "+requestBy.name+"("+requestBy.getPatient().getSexAndAge()+")"+" has been saved.");

			builder.append("<br><br>Best regards,<br>MedNetwork.in</p>");
			builder.append("</body></html>");
			final HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator(Constants.EMAIL_ID, Constants.EMAIL_PASSWORD));
			email.setSSLOnConnect(true);
			email.setFrom(Constants.EMAIL_ID,"MedNetwork");
			email.setSubject("Prescription saved at MedNetwork");
			email.setHtmlMsg(builder.toString());
			email.addTo(requestTo.email);
			email.send();
			Logger.info("Mail Sent Successfully!");
			Logger.info(builder.toString());
		}
		catch (final Exception e){
			System.out.println("ERROR While Sending Email");
			e.printStackTrace();
			result=false;
		}
		return result;
	}

	public static boolean sendSimpleHtmlEMail(final String receiverEmailId, final String subject, final String message) {
		boolean result = true;
		try{
			final HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator(Constants.EMAIL_ID, Constants.EMAIL_PASSWORD));
			email.setSSLOnConnect(true);
			email.setFrom(Constants.EMAIL_ID,"MedNetwork");
			email.setSubject(subject);
			email.setHtmlMsg(message);
			email.addTo(receiverEmailId);
			email.send();
			Logger.info("Mail Sent Successfully!");
			Logger.info(message);
		}
		catch (final Exception e){
			System.out.println("ERROR While Sending Email");
			e.printStackTrace();
			result=false;
		}
		return result;

	}

	/**
	 * @author lakshmi
	 * Action to send invitation to the Doctor.
	 * NO URL
	 */
	public static boolean sendClinicInvitationConfirmationEmail(final AppUser appUser,final Clinic clinic, final String verificationCode){
		boolean result = true;
		try{
			final StringBuilder builder=new StringBuilder();
			builder.append("<html><body>");
			builder.append("<p> Dr "+appUser.name+",<br><br> You have an invitation from "+clinic.name+", <br>Please ");
			builder.append("<a href=\"http://mednetwork.in/secure-clinic/add-doctor/"+appUser.getDoctor().id+"/"+clinic.id+"/"+verificationCode+"\"");
			builder.append("<b>click here</b>");
			builder.append("</a> to accept the invitation and join "+clinic.name+".<br><br>Best regards,<br>MedNetwork.in</p>");
			builder.append("</body></html>");
			final HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator(Constants.EMAIL_ID, Constants.EMAIL_PASSWORD));
			email.setSSLOnConnect(true);
			email.setFrom(Constants.EMAIL_ID,"MedNetwork");
			email.setSubject("Clinic Invitation");
			email.setHtmlMsg(builder.toString());
			email.addTo(appUser.email);
			email.send();
			System.out.println("Mail Sent Successfully!");
			Logger.info(builder.toString());
		}
		catch (final Exception e){
			System.out.println("ERROR While Sending Email");
			e.printStackTrace();
			result=false;
		}
		return result;
	}
	/**
	 * @author lakshmi
	 * Action to send successfully added  to the Clinic message to the Doctor
	 */
	public static boolean sendClinicInvitationSuccessEmail(final Doctor doctor,final Clinic clinic){
		boolean result = true;
		try{
			final StringBuilder builder=new StringBuilder();
			builder.append("<html><body>");
			builder.append("<p> Dr. "+doctor.appUser.name+",<br><br> You have been added to the Clinic "+clinic.name);
			builder.append("<br><br>Best regards,<br>MedNetwork.in</p>");
			builder.append("</body></html>");
			final HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator(Constants.EMAIL_ID, Constants.EMAIL_PASSWORD));
			email.setSSLOnConnect(true);
			email.setFrom(Constants.EMAIL_ID,"MedNetwork");
			email.setSubject("Clinic Invitation");
			email.setHtmlMsg(builder.toString());
			email.addTo(doctor.appUser.email);
			email.send();
			System.out.println("Mail Sent Successfully!");
			Logger.info(builder.toString());
		}
		catch (final Exception e){
			System.out.println("ERROR While Sending Email");
			e.printStackTrace();
			result=false;
		}
		return result;
	}

	/**
	 * @author lakshmi
	 * Action to send email and password to the Clinic User.
	 * NO URL
	 */
	public static boolean sendClinicUserConfirmationMail(final ClinicUser clinicUser,final ClinicUser clinicAdmin){
		boolean result = true;
		try{
			final StringBuilder builder=new StringBuilder();
			builder.append("<html><body>");
			builder.append("<p> Dear "+clinicUser.appUser.name+",<br><br> You have been added as the Clinic User in "+clinicAdmin.clinic.name+"by "+clinicAdmin.appUser.name+" , <br>");
			builder.append(" Your Email Id <b>"+clinicUser.appUser.email+"</b><br> Password <b>"+clinicUser.appUser.password+"</b><br><br>Best regards,<br>MedNetwork.in</p>");
			builder.append("</body></html>");
			final HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator(Constants.EMAIL_ID, Constants.EMAIL_PASSWORD));
			email.setSSLOnConnect(true);
			email.setFrom(Constants.EMAIL_ID,"MedNetwork");
			email.setSubject("Clinic User Confirmation");
			email.setHtmlMsg(builder.toString());
			email.addTo(clinicUser.appUser.email);
			email.send();
			System.out.println("Mail Sent Successfully!");
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


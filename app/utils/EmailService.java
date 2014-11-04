package utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.AppUser;
import models.clinic.ClinicUser;
import models.doctor.Appointment;
import models.doctor.Clinic;
import models.doctor.Doctor;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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
			final String url = Constants.EMAIL_URL;

			final HttpClient client = new DefaultHttpClient();
			final HttpPost post = new HttpPost(url);

			final List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("api_user", Constants.EMAIL_ID));
			urlParameters.add(new BasicNameValuePair("api_key", Constants.EMAIL_PASSWORD));
			urlParameters.add(new BasicNameValuePair("to", receiverEmailId));
			//urlParameters.add(new BasicNameValuePair("toname", ""));
			urlParameters.add(new BasicNameValuePair("subject", "Subject"));
			//urlParameters.add(new BasicNameValuePair("text", builder.toString())); // This can be used for any plain text content of the mail
			urlParameters.add(new BasicNameValuePair("html", "Content Of Email"));
			urlParameters.add(new BasicNameValuePair("fromname", "MedNetwork"));
			urlParameters.add(new BasicNameValuePair("from", "noreply@mednetwork.in"));
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			client.execute(post);

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
		final String url = Constants.EMAIL_URL;

		final HttpClient client = new DefaultHttpClient();
		final HttpPost post = new HttpPost(url);

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
			final List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("api_user", Constants.EMAIL_ID));
			urlParameters.add(new BasicNameValuePair("api_key", Constants.EMAIL_PASSWORD));
			urlParameters.add(new BasicNameValuePair("to", appUser.email));
			//urlParameters.add(new BasicNameValuePair("toname", ""));
			urlParameters.add(new BasicNameValuePair("subject", "Confirm your account at MedNetwork"));
			//urlParameters.add(new BasicNameValuePair("text", builder.toString())); // This can be used for any plain text content of the mail
			urlParameters.add(new BasicNameValuePair("html", builder.toString()));
			urlParameters.add(new BasicNameValuePair("fromname", "MedNetwork"));
			urlParameters.add(new BasicNameValuePair("from", "noreply@mednetwork.in"));
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			client.execute(post);
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

	/*@SuppressWarnings({ "deprecation", "deprecation" })
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

	}*/



	/**
	 * Action to send mail to appUser's email id to change password(forgot password)
	 * NO URL
	 */
	public static void sendForgotPasswordEmail(final AppUser appUser){

		final String url = Constants.EMAIL_URL;

		final HttpClient client = new DefaultHttpClient();
		final HttpPost post = new HttpPost(url);

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

			final List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("api_user", Constants.EMAIL_ID));
			urlParameters.add(new BasicNameValuePair("api_key", Constants.EMAIL_PASSWORD));
			urlParameters.add(new BasicNameValuePair("to", appUser.email));
			//urlParameters.add(new BasicNameValuePair("toname", ""));
			urlParameters.add(new BasicNameValuePair("subject", "Reset Password  at MedNetwork"));
			//urlParameters.add(new BasicNameValuePair("text", builder.toString())); // This can be used for any plain text content of the mail
			urlParameters.add(new BasicNameValuePair("html", builder.toString()));
			urlParameters.add(new BasicNameValuePair("fromname", "MedNetwork"));
			urlParameters.add(new BasicNameValuePair("from", "noreply@mednetwork.in"));
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			client.execute(post);

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

		final String url = Constants.EMAIL_URL;

		final HttpClient client = new DefaultHttpClient();
		final HttpPost post = new HttpPost(url);

		boolean result = true;
		try{
			final StringBuilder builder=new StringBuilder();
			builder.append("<html><body>");
			builder.append("<p>Dear "+appUser.name+",<br><br>Thank you for verifying your email id.");
			builder.append("<br><br>Best regards,<br>MedNetwork.in</p>");
			builder.append("</body></html>");

			final List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("api_user", Constants.EMAIL_ID));
			urlParameters.add(new BasicNameValuePair("api_key", Constants.EMAIL_PASSWORD));
			urlParameters.add(new BasicNameValuePair("to", appUser.email));
			//urlParameters.add(new BasicNameValuePair("toname", ""));
			urlParameters.add(new BasicNameValuePair("subject", "MedNetwork Account Verified"));
			//urlParameters.add(new BasicNameValuePair("text", builder.toString())); // This can be used for any plain text content of the mail
			urlParameters.add(new BasicNameValuePair("html", builder.toString()));
			urlParameters.add(new BasicNameValuePair("fromname", "MedNetwork"));
			urlParameters.add(new BasicNameValuePair("from", "noreply@mednetwork.in"));
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			client.execute(post);

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
		final String url = Constants.EMAIL_URL;

		final HttpClient client = new DefaultHttpClient();
		final HttpPost post = new HttpPost(url);

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

			final List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("api_user", Constants.EMAIL_ID));
			urlParameters.add(new BasicNameValuePair("api_key", Constants.EMAIL_PASSWORD));
			urlParameters.add(new BasicNameValuePair("to", requestBy.email));
			//urlParameters.add(new BasicNameValuePair("toname", ""));
			urlParameters.add(new BasicNameValuePair("subject", "An appointment has been booked at "+appointment.doctorClinicInfo.clinic.name+"."));
			//urlParameters.add(new BasicNameValuePair("text", builder.toString())); // This can be used for any plain text content of the mail
			urlParameters.add(new BasicNameValuePair("html", builder.toString()));
			urlParameters.add(new BasicNameValuePair("fromname", "MedNetwork"));
			urlParameters.add(new BasicNameValuePair("from", "noreply@mednetwork.in"));
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			client.execute(post);

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



			final List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("api_user", Constants.EMAIL_ID));
			urlParameters.add(new BasicNameValuePair("api_key", Constants.EMAIL_PASSWORD));
			urlParameters.add(new BasicNameValuePair("to", requestTo.email));
			//urlParameters.add(new BasicNameValuePair("toname", ""));
			urlParameters.add(new BasicNameValuePair("subject", "An appointment has been booked at "+appointment.doctorClinicInfo.clinic.name+"."));
			//urlParameters.add(new BasicNameValuePair("text", builder.toString())); // This can be used for any plain text content of the mail
			urlParameters.add(new BasicNameValuePair("html", builder.toString()));
			urlParameters.add(new BasicNameValuePair("fromname", "MedNetwork"));
			urlParameters.add(new BasicNameValuePair("from", "noreply@mednetwork.in"));
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			client.execute(post);

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

		final String url = Constants.EMAIL_URL;

		final HttpClient client = new DefaultHttpClient();
		final HttpPost post = new HttpPost(url);


		boolean result = true;
		try{
			final StringBuilder builder=new StringBuilder();
			builder.append("<html><body>");
			builder.append("<p>Dear "+requestBy.name+",<br><br>Your prescription by Dr. "+requestTo.name+" has been saved.");

			builder.append("<br><br>Best regards,<br>MedNetwork.in</p>");
			builder.append("</body></html>");

			final List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("api_user", Constants.EMAIL_ID));
			urlParameters.add(new BasicNameValuePair("api_key", Constants.EMAIL_PASSWORD));
			urlParameters.add(new BasicNameValuePair("to", requestBy.email));
			//urlParameters.add(new BasicNameValuePair("toname", ""));
			urlParameters.add(new BasicNameValuePair("subject", "Your Prescription has been saved at MedNetwork."));
			//urlParameters.add(new BasicNameValuePair("text", builder.toString())); // This can be used for any plain text content of the mail
			urlParameters.add(new BasicNameValuePair("html", builder.toString()));
			urlParameters.add(new BasicNameValuePair("fromname", "MedNetwork"));
			urlParameters.add(new BasicNameValuePair("from", "noreply@mednetwork.in"));
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			client.execute(post);

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

			final List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("api_user", Constants.EMAIL_ID));
			urlParameters.add(new BasicNameValuePair("api_key", Constants.EMAIL_PASSWORD));
			urlParameters.add(new BasicNameValuePair("to", requestTo.email));
			//urlParameters.add(new BasicNameValuePair("toname", ""));
			urlParameters.add(new BasicNameValuePair("subject", "Mail Sent Successfully!"));
			//urlParameters.add(new BasicNameValuePair("text", builder.toString())); // This can be used for any plain text content of the mail
			urlParameters.add(new BasicNameValuePair("html", builder.toString()));
			urlParameters.add(new BasicNameValuePair("fromname", "MedNetwork"));
			urlParameters.add(new BasicNameValuePair("from", "noreply@mednetwork.in"));
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			client.execute(post);

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
		final String url = Constants.EMAIL_URL;

		final HttpClient client = new DefaultHttpClient();
		final HttpPost post = new HttpPost(url);
		boolean result = true;
		try{
			final List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("api_user", Constants.EMAIL_ID));
			urlParameters.add(new BasicNameValuePair("api_key", Constants.EMAIL_PASSWORD));
			urlParameters.add(new BasicNameValuePair("to", receiverEmailId));
			//urlParameters.add(new BasicNameValuePair("toname", ""));
			urlParameters.add(new BasicNameValuePair("subject", "Subject"));
			//urlParameters.add(new BasicNameValuePair("text", builder.toString())); // This can be used for any plain text content of the mail
			urlParameters.add(new BasicNameValuePair("html", "Content Of Email"));
			urlParameters.add(new BasicNameValuePair("fromname", "MedNetwork"));
			urlParameters.add(new BasicNameValuePair("from", "noreply@mednetwork.in"));
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			client.execute(post);

			System.out.println("Mail Sent Successfully!");
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

		final String url = Constants.EMAIL_URL;

		final HttpClient client = new DefaultHttpClient();
		final HttpPost post = new HttpPost(url);

		boolean result = true;
		try{
			final StringBuilder builder=new StringBuilder();
			builder.append("<html><body>");
			builder.append("<p> Dr "+appUser.name+",<br><br> You have an invitation from "+clinic.name+", <br>Please ");
			builder.append("<a href=\"http://mednetwork.in/secure-clinic/add-doctor/"+appUser.getDoctor().id+"/"+clinic.id+"/"+verificationCode+"\"");
			builder.append("<b>click here</b>");
			builder.append("</a> to accept the invitation and join "+clinic.name+".<br><br>Best regards,<br>MedNetwork.in</p>");
			builder.append("</body></html>");

			final List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("api_user", Constants.EMAIL_ID));
			urlParameters.add(new BasicNameValuePair("api_key", Constants.EMAIL_PASSWORD));
			urlParameters.add(new BasicNameValuePair("to", appUser.email));
			//urlParameters.add(new BasicNameValuePair("toname", ""));
			urlParameters.add(new BasicNameValuePair("subject", "Clinic Invitation"));
			//urlParameters.add(new BasicNameValuePair("text", builder.toString())); // This can be used for any plain text content of the mail
			urlParameters.add(new BasicNameValuePair("html", builder.toString()));
			urlParameters.add(new BasicNameValuePair("fromname", "MedNetwork"));
			urlParameters.add(new BasicNameValuePair("from", "noreply@mednetwork.in"));
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			client.execute(post);

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

		final String url = Constants.EMAIL_URL;

		final HttpClient client = new DefaultHttpClient();
		final HttpPost post = new HttpPost(url);

		boolean result = true;
		try{
			final StringBuilder builder=new StringBuilder();
			builder.append("<html><body>");
			builder.append("<p> Dr. "+doctor.appUser.name+",<br><br> You have been added to the Clinic "+clinic.name);
			builder.append("<br><br>Best regards,<br>MedNetwork.in</p>");
			builder.append("</body></html>");

			final List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("api_user", Constants.EMAIL_ID));
			urlParameters.add(new BasicNameValuePair("api_key", Constants.EMAIL_PASSWORD));
			urlParameters.add(new BasicNameValuePair("to", doctor.appUser.email));
			//urlParameters.add(new BasicNameValuePair("toname", ""));
			urlParameters.add(new BasicNameValuePair("subject", "Clinic Invitation"));
			//urlParameters.add(new BasicNameValuePair("text", builder.toString())); // This can be used for any plain text content of the mail
			urlParameters.add(new BasicNameValuePair("html", builder.toString()));
			urlParameters.add(new BasicNameValuePair("fromname", "MedNetwork"));
			urlParameters.add(new BasicNameValuePair("from", "noreply@mednetwork.in"));
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			client.execute(post);

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

		final String url = Constants.EMAIL_URL;

		final HttpClient client = new DefaultHttpClient();
		final HttpPost post = new HttpPost(url);

		boolean result = true;
		try{
			final StringBuilder builder=new StringBuilder();
			builder.append("<html><body>");
			builder.append("<p> Dear "+clinicUser.appUser.name+",<br><br> You have been added as the Clinic User in "+clinicAdmin.clinic.name+"by "+clinicAdmin.appUser.name+" , <br>");
			builder.append(" Your Email Id <b>"+clinicUser.appUser.email+"</b><br> Password <b>"+clinicUser.appUser.password+"</b><br><br>Best regards,<br>MedNetwork.in</p>");
			builder.append("</body></html>");

			final List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("api_user", Constants.EMAIL_ID));
			urlParameters.add(new BasicNameValuePair("api_key", Constants.EMAIL_PASSWORD));
			urlParameters.add(new BasicNameValuePair("to", clinicUser.appUser.email));
			//urlParameters.add(new BasicNameValuePair("toname", ""));
			urlParameters.add(new BasicNameValuePair("subject", "Clinic User Confirmation"));
			//urlParameters.add(new BasicNameValuePair("text", builder.toString())); // This can be used for any plain text content of the mail
			urlParameters.add(new BasicNameValuePair("html", builder.toString()));
			urlParameters.add(new BasicNameValuePair("fromname", "MedNetwork"));
			urlParameters.add(new BasicNameValuePair("from", "noreply@mednetwork.in"));
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			client.execute(post);

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


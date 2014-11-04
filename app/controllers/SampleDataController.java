package controllers;


import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import models.Address;
import models.Alert;
import models.AppUser;
import models.MasterDiagnosticTest;
import models.MasterProduct;
import models.PrimaryCity;
import models.Role;
import models.State;
import models.bloodBank.BloodBank;
import models.bloodBank.BloodBankUser;
import models.diagnostic.DiagnosticCentrePrescriptionInfo;
import models.doctor.DiagnosticTestLineItem;
import models.doctor.Doctor;
import models.doctor.MasterSpecialization;
import models.doctor.Prescription;
import models.mr.Designation;
import models.mr.MedicalRepresentative;
import models.mr.PharmaceuticalCompany;
import models.patient.Patient;
import models.patient.PatientDoctorInfo;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constants;
import utils.SMSService;

public class SampleDataController extends Controller {

	public static Result populate() {

		//final Doctor doctor = new Doctor();
		//doctor.name = "Test Admin";
		/*doctor.name = "Test Admin";
		//final Doctor doctor = new Doctor();
		//doctor.name = "Test Admin";
		/*doctor.name = "Test Admin";
		//doctor.role = Role.DOCTOR;
		//doctor.email = "admin@mednet.com";
		//doctor.password = "123456";
		//doctor.save();
		doctor.email = "admin@mednet.com";
		doctor.password = "123456";
		doctor.save();*/

		final AppUser user = new AppUser();
		user.name = "Test User";
		user.role = Role.PATIENT;
		user.email = "user@mednet1.com";
		user.password = "1";
		user.save();
		final Patient patient=new Patient();
		patient.appUser=user;
		patient.save();
		//return ok("user created");
		return redirect(routes.Application.index());

	}

	public static Result cleanUp() {

		final List<AppUser> users = AppUser.find.all();
		for (final AppUser user : users) {
			user.delete();
		}

		return ok();
	}

	public static Result temp() {
		/*final AppUser user=new AppUser();
		user.email="mitesh@ukate.com";
		user.password="123456";
		final Patient patient=new Patient();
		//user.patient=patient;
		patient.save();
		user.save();*/

		return ok("created");
	}



	/**
	 * @author Mitesh
	 * Action to create dummy PatientDoctorInfo
	 * GET /sampledata/create-patdoc
	 */
	public static Result createSamplePatientDoctor() {

		final Patient patient=LoginController.getLoggedInUser().getPatient();

		for(int i=0;i<10;i++){
			final PatientDoctorInfo patDocInfo=new PatientDoctorInfo();
			patDocInfo.patient=patient;


			final AppUser appUser=new AppUser();
			appUser.name="Test Doctor"+i;
			appUser.dob=new Date();
			appUser.email="test@doctor.com"+i;
			appUser.password="1";
			appUser.save();

			final Doctor doctor=new Doctor();
			doctor.appUser=appUser;
			doctor.degree="Deegree"+i;
			doctor.experience=i;

			doctor.save();

			patDocInfo.doctor=doctor;

			patient.patientDoctorInfoList.add(patDocInfo);
		}

		patient.save();

		return ok("Created");
	}

	public static Result test(){
		final PharmaceuticalCompany company = PharmaceuticalCompany.find.byId(1L);
		for(int i=0; i<10; i++){
			final AppUser appUser = new AppUser();
			appUser.name = "Anand"+i;
			appUser.email = "anand"+i+"@gmail.com";
			appUser.password = "1111";
			appUser.role = Role.MR;
			appUser.save();
			final MedicalRepresentative mr = new MedicalRepresentative();
			mr.appUser = appUser;
			mr.pharmaceuticalCompany = company;
			mr.save();
			company.mrList.add(mr);
			company.update();
		}
		return ok();
	}



	public static Result mrSample() {
		final PharmaceuticalCompany pc = new PharmaceuticalCompany();
		pc.name = "Acme Pharma Pvt Ltd";
		pc.save();
		for (int i=1;i<=16;i++)
		{
			if(i==1)
			{
				final MedicalRepresentative mr = new MedicalRepresentative();
				final AppUser appUser = new AppUser();
				appUser.name = "MR Admin";
				appUser.email="mradmin@gmail.com";
				appUser.password="vvCEZl0vYX8iwftcTBhF7rEIM+oaK1kww2ZVX0bihEM=";
				appUser.salt="jdekZFmcw2oSU1RmpX/ekNcTSm49Z6sMwNBMvYjmefM=";
				appUser.role = Role.ADMIN_MR;
				appUser.save();
				mr.appUser = appUser;
				mr.pharmaceuticalCompany = pc;
				mr.save();
				pc.mrList.add(mr);
				pc.adminMR = mr;
				pc.update();
			}
			final MedicalRepresentative mr = new MedicalRepresentative();
			final AppUser appUser = new AppUser();
			appUser.name = "MR-"+i;
			appUser.email = "mr"+i+"@gmail.com";
			appUser.password="vvCEZl0vYX8iwftcTBhF7rEIM+oaK1kww2ZVX0bihEM=";
			appUser.salt="jdekZFmcw2oSU1RmpX/ekNcTSm49Z6sMwNBMvYjmefM=";
			appUser.role = Role.MR;
			appUser.save();
			mr.appUser = appUser;
			mr.pharmaceuticalCompany = pc;
			mr.save();
			pc.mrList.add(mr);
			pc.update();
		}
		return ok();
	}




	/*public static Result mrSampleData(){
		final AppUser appUser = new AppUser();
		appUser.name = "anand";
		appUser.email = "anand@gmail.com";
		appUser.password = "123";
		appUser.role = Role.ADMIN_MR;
		appUser.save();
		final MedicalRepresentative mr = new MedicalRepresentative();
		mr.appUser = appUser;
		final PharmaceuticalCompany company = new PharmaceuticalCompany();
		company.name="green pharma";
		company.save();
		mr.pharmaceuticalCompany = company;
		mr.save();
		return ok();

	}*/
	public static Result mrSampleData(){
		final AppUser appUser = new AppUser();
		appUser.name = "anand";
		appUser.email = "anand@gmail.com";
		final String password = "123";
		appUser.role = Role.ADMIN_MR;
		if(AppUser.find.where().eq("email", appUser.email).findRowCount()>0){
			flash().put("alert", new Alert("alert-danger", "Sorry! User with email id "+appUser.email.trim()+" already exists!").toString());
			if(appUser.role == Role.ADMIN_MR){
				return ok("User already exist");
			}
		}
		try {

			final Random random = new SecureRandom();
			final byte[] saltArray = new byte[32];
			random.nextBytes(saltArray);
			final String randomSalt = Base64.encodeBase64String(saltArray);

			final String passwordWithSalt = password+randomSalt;
			final MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
			final byte[] passBytes = passwordWithSalt.getBytes();
			final String hashedPasswordWithSalt = Base64.encodeBase64String(sha256.digest(passBytes));

			appUser.salt = randomSalt;
			appUser.password = hashedPasswordWithSalt;

		} catch (final Exception e) {
			Logger.error("ERROR WHILE CREATING SHA2 HASH");
			e.printStackTrace();
		}
		appUser.save();

		final MedicalRepresentative mr = new MedicalRepresentative();
		mr.appUser = appUser;
		final PharmaceuticalCompany company = new PharmaceuticalCompany();
		final Designation designation = new Designation();
		designation.name = "administrator";
		//designation.save();

		company.name="green pharma";
		//company.save();

		company.designationList.add(designation);

		//company.update();
		mr.pharmaceuticalCompany = company;
		mr.designation = company.designationList.get(0);
		mr.save();
		company.adminMR = mr;
		company.update();
		return ok("mr Sample data saved");

	}

	public static Result patientTest(){
		final AppUser appUser1 = new AppUser();
		appUser1.name = "laxmi";
		appUser1.email = "patient@gmail.com";
		appUser1.password = "1111";
		appUser1.role = Role.PATIENT;
		appUser1.save();
		final Patient patient = new Patient();
		patient.appUser = appUser1;
		patient.save();
		return ok();
	}
	/**
	 * Action to create Prescription for the Diagnostic Centre
	 * @return
	 * @throws ParseException
	 */

	//	public static Result prescripetionTest() throws ParseException{
	//		Prescription prescription = new Prescription();
	//		PharmacyPrescriptionInfo pharmacyPrescriptionInfo = new PharmacyPrescriptionInfo();
	//		pharmacyPrescriptionInfo.pharmacy = LoginController.getLoggedInUser().getPharmacist().pharmacy;
	//		MedicineLineItem medicineLineItem1 = new MedicineLineItem();
	//		medicineLineItem1.fullNameOfMedicine = "medicine1";
	//		prescription.medicineLineItemList.add(medicineLineItem1);
	//		MedicineLineItem medicineLineItem2 = new MedicineLineItem();
	//		medicineLineItem2.fullNameOfMedicine = "medicine2";
	//		prescription.medicineLineItemList.add(medicineLineItem2);
	//		pharmacyPrescriptionInfo.prescription = prescription;
	//		pharmacyPrescriptionInfo.receivedDate = new SimpleDateFormat("dd-MMM-yyyy").parse("7-Jun-2013");
	//		pharmacyPrescriptionInfo.save();
	////		return redirect(routes.PharmacistController.addPharmacyOrderFromDoctor(pharmacy.id,prescription.id));
	//
	//return ok();
	//	}
	//
	//}

	/**
	 * Action to create Prescription for the Diagnostic Centre
	 * @return
	 */

	public static Result prescripetionTest(){
		final Prescription prescription = new Prescription();
		final DiagnosticCentrePrescriptionInfo diagnosticCentrePrescriptionInfo = new DiagnosticCentrePrescriptionInfo();
		diagnosticCentrePrescriptionInfo.diagnosticCentre = LoginController.getLoggedInUser().getDiagnosticRepresentative().diagnosticCentre;
		final DiagnosticTestLineItem medicineLineItem1 = new DiagnosticTestLineItem();
		medicineLineItem1.fullNameOfDiagnosticTest = "X-ray";
		prescription.diagnosticTestLineItemList.add(medicineLineItem1);
		final DiagnosticTestLineItem medicineLineItem2 = new DiagnosticTestLineItem();
		medicineLineItem2.fullNameOfDiagnosticTest = "Scanning";
		prescription.diagnosticTestLineItemList.add(medicineLineItem2);
		prescription.save();
		diagnosticCentrePrescriptionInfo.prescription = prescription;
		diagnosticCentrePrescriptionInfo.sharedDate = new Date();
		diagnosticCentrePrescriptionInfo.save();
		return ok();

	}

	public static Result createMasterMedicinesAndTests(){
		for(int i=10; i<=15; i++){
			final MasterProduct product = new MasterProduct();
			product.fullName = "Medicine-"+i;
			product.save();

			final MasterDiagnosticTest test = new MasterDiagnosticTest();
			test.name = "Test-"+i;
			test.save();
		}
		return ok();
	}


	public static Result createDocSpez(){
		MasterSpecialization spez;
		spez = new MasterSpecialization();
		spez.name = "Cardiology";
		spez.save();
		spez = new MasterSpecialization();
		spez.name = "Opthalmology";
		spez.save();
		spez = new MasterSpecialization();
		spez.name = "Orthopaedic";
		spez.save();
		spez = new MasterSpecialization();
		spez.name = "Neurologist";
		spez.save();
		spez = new MasterSpecialization();
		spez.name = "Paediatrics";
		spez.save();
		spez = new MasterSpecialization();
		spez.name = "Gynecologist";
		spez.save();
		spez = new MasterSpecialization();
		spez.name = "Obstetician";
		spez.save();
		return ok();
	}

	public static Result mednetAdmin(){
		final AppUser appUser = new AppUser();
		appUser.name = "laxmi";
		appUser.email = "vlaxmi.b3@gmail.com";
		appUser.role = Role.MEDNETWORK_ADMIN;
		appUser.save();
		return ok();
	}

	/*	public static Result testXXX() {
		promise(new Function0<Integer>() {
			@Override
			public Integer apply() {
				//return intensiveComputation();
				try {
					System.out.println("1");
					Thread.sleep(1000);
					System.out.println("2");
					Thread.sleep(1000);
					System.out.println("3");
					Thread.sleep(1000);
					System.out.println("4");
					Thread.sleep(1000);
					System.out.println("5");
					Thread.sleep(1000);
					System.out.println("6");
					Thread.sleep(1000);
					System.out.println("7");
					Thread.sleep(1000);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
				return 0;
			}
		});

		return ok("Right Now!");
	}
	 */


	public static Result testurl(){
		SMSService.sendSMS("9949254085", "From Paris With Love!");
		return ok("This Page");
	}
	public static Result addPrimaryCity(){
		final PrimaryCity primaryCity = new PrimaryCity();
		primaryCity.name = "Macherla";
		primaryCity.state = State.ANDHRA_PRADESH;
		primaryCity.save();
		final PrimaryCity primaryCity2 = new PrimaryCity();
		primaryCity2.name = "Guntur";
		primaryCity2.state = State.ANDHRA_PRADESH;
		primaryCity2.save();
		flash().put("alert", new Alert("alert-info","Primary City Added").toString());
		return ok();
	}
	public static Result addDefaultPrimaryCity(){
		final List<AppUser> appUsers = AppUser.find.where().eq("role", Role.DOCTOR).findList();
		Logger.info("size()=="+appUsers.get(0).role);
		final PrimaryCity primaryCity = new PrimaryCity();
		primaryCity.name = "Vijayawada";
		primaryCity.state = State.ANDHRA_PRADESH;
		primaryCity.save();
		for (final AppUser appUser : appUsers) {
			final Doctor doctor = Doctor.find.where().eq("appUser", appUser).findUnique();
			Logger.info("doctor..."+doctor.appUser.name);
			doctor.primaryCity = primaryCity;
			doctor.update();
		}
		return ok("added primary city");
	}

	public static Result addBloodBank(){

		final String dt = "22-10-2014";
		final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try{
			final Date date = sdf.parse(dt);
		}
		catch(final Exception e){

		}

		final BloodBankUser bloodBankUser = new BloodBankUser();
		final AppUser appUser = new AppUser();
		appUser.name = "BlooBankAppUser";
		appUser.email = "bloodbank@bloodbank.com";
		appUser.role = Role.BLOOD_BANK_ADMIN;
		appUser.save();
		bloodBankUser.appUser = appUser;
		bloodBankUser.save();
		final BloodBank bloodBank = new BloodBank();
		bloodBank.name="Red Cross Blood Bank";
		bloodBank.bloodBankAdmin = bloodBankUser;
		bloodBank.contactPersonName = "laxmi";
		bloodBank.address = Address.find.byId(1L);
		bloodBank.save();
		bloodBankUser.bloodBank = bloodBank;
		bloodBankUser.update();
		return ok("added blood bank");
	}

	public static Result joinUsClinic(){
		return ok(views.html.clinic.joinus.render());
	}


	public static Result sendGrid(){

		try{
			final String url = "https://api.sendgrid.com/api/mail.send.json";

			final HttpClient client = new DefaultHttpClient();
			final HttpPost post = new HttpPost(url);

			// add header
			//post.setHeader("User-Agent", USER_AGENT);

			final StringBuilder builder=new StringBuilder();
			builder.append("<html><body>");
			builder.append("<p>Dear Mr. Singh,<br><br>Thank you for signing up at MedNetwork. Please ");
			builder.append("<a href=\"http://mednetwork.in/secure-user/confirmation/");
			builder.append("X");
			builder.append("/"+"Y"+"\">");
			builder.append("<b>click here</b>");
			builder.append("</a> to confirm your email address.<br><br>Best regards,<br>MedNetwork.in</p>");
			builder.append("</body></html>");

			final List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("api_user", Constants.EMAIL_ID));
			urlParameters.add(new BasicNameValuePair("api_key", Constants.EMAIL_PASSWORD));
			urlParameters.add(new BasicNameValuePair("to", "butasingh@gmail.com"));
			//urlParameters.add(new BasicNameValuePair("toname", ""));
			urlParameters.add(new BasicNameValuePair("subject", "Mail From MedNetwork"));
			//urlParameters.add(new BasicNameValuePair("text", builder.toString())); // This can be used for any plain text content of the mail
			urlParameters.add(new BasicNameValuePair("html", builder.toString()));
			urlParameters.add(new BasicNameValuePair("fromname", "MedNetwork"));
			urlParameters.add(new BasicNameValuePair("from", "noreply@mednetwork.in"));
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			client.execute(post);
			return ok();
			/*
			final HttpResponse response = client.execute(post);
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + post.getEntity());
			System.out.println("Response Code : " +response.getStatusLine().getStatusCode());

			final BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			final StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			System.out.println(result.toString());
			return ok(result.toString());

			 */
		}
		catch(final Exception e){
			return ok("FAILED");
		}
	}

}


package controllers;


import java.util.Date;
import java.util.List;

import models.AppUser;
import models.MasterDiagnosticTest;
import models.MasterProduct;
import models.Role;
import models.Sex;
import models.diagnostic.DiagnosticCentrePrescriptionInfo;
import models.doctor.Appointment;
import models.doctor.DiagnosticTestLineItem;
import models.doctor.Doctor;
import models.doctor.Prescription;
import models.mr.MedicalRepresentative;
import models.mr.PharmaceuticalCompany;
import models.patient.Patient;
import models.patient.PatientDoctorInfo;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

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


	public static Result createBlogAdmin(){
		if(AppUser.find.where().eq("email", "blog@mednetwork.in").findList().size()>0){
			return redirect(routes.Application.index());
		}
		final AppUser appUser = new AppUser();
		appUser.name = "Blog Admin";
		appUser.role = Role.BLOG_ADMIN;
		appUser.email = "blog@mednetwork.in";
		appUser.password = "med2014blog";
		appUser.save();
		return redirect(routes.Application.index());
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

	/**
	 * 
	 * @author Dibesh
	 * 
	 *         Dummy data
	 * 
	 *         GET /mr/add-value controllers.MRController.values()
	 */

	public static Result values() {
		for (int i=1;i<=15;i++)
		{
			if(i==1)
			{
				final MedicalRepresentative mr = new MedicalRepresentative();
				final AppUser appUser = new AppUser();

				appUser.name = "admin";
				appUser.username = "admin";
				appUser.email="admin@gmail.com";
				appUser.password="admin";
				appUser.role = Role.ADMIN_MR;
				appUser.save();
				mr.appUser = appUser;
				mr.companyName = "hello";
				final PharmaceuticalCompany pc = new PharmaceuticalCompany();
				pc.name = mr.companyName;
				pc.mrList.add(mr);
				pc.adminMR = LoginController.getLoggedInUser().getMedicalRepresentative();
				pc.save();
				mr.pharmaceuticalCompany = pc;
				mr.save();

			}
			final MedicalRepresentative mr = new MedicalRepresentative();
			final AppUser appUser = new AppUser();
			appUser.name = "sam"+i;
			appUser.username = "sam"+i;
			appUser.email="sam@gmail.com";
			appUser.password="sam";
			appUser.role = Role.MR;
			appUser.save();
			mr.appUser = appUser;
			mr.companyName = "hello";
			final PharmaceuticalCompany pc = new PharmaceuticalCompany();
			pc.name = mr.companyName;
			pc.mrList.add(mr);
			pc.adminMR = LoginController.getLoggedInUser().getMedicalRepresentative();
			pc.save();
			mr.pharmaceuticalCompany = LoginController
					.getLoggedInUser().getMedicalRepresentative().pharmaceuticalCompany;
			//mr.manager = MedicalRepresentative.find.where().eq("companyName", mr.companyName).findUnique();
			mr.save();
		}

		return ok();
	}




	public static Result mrSampleData(){
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
		Patient patient = Patient.find.byId(1L);
		patient.age="20";
		patient.appUser.sex = Sex.FEMALE;
		patient.update();
		prescription.patient = patient;
		 Doctor doctor= Doctor.find.byId(1L);		 	 
		prescription.doctor= doctor;
		Appointment appointment = new Appointment();
		appointment.appointmentTime = new Date();
		prescription.appointment= appointment;
		prescription.prescriptionDate = new Date();
		prescription.save();
		/*final DiagnosticTestLineItem medicineLineItem1 = new DiagnosticTestLineItem();
		medicineLineItem1.fullNameOfDiagnosticTest = "X-ray";
		prescription.diagnosticTestLineItemList.add(medicineLineItem1);
		final DiagnosticTestLineItem medicineLineItem2 = new DiagnosticTestLineItem();
		medicineLineItem2.fullNameOfDiagnosticTest = "Scanning";
		prescription.diagnosticTestLineItemList.add(medicineLineItem2);
		prescription.save();
		diagnosticCentrePrescriptionInfo.prescription = prescription;
		diagnosticCentrePrescriptionInfo.sharedDate = new Date();
		diagnosticCentrePrescriptionInfo.save();*/
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

}


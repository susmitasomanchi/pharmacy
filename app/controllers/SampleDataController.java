package controllers;


import java.util.Date;
import java.util.List;

import models.AppUser;
import models.Role;
import models.diagnostic.DiagnosticCentre;
import models.diagnostic.MasterDiagnosticTest;
import models.diagnostic.DiagnosticTestLineItem;
import models.doctor.Doctor;
import models.doctor.Prescription;
import models.mr.MedicalRepresentative;
import models.mr.PharmaceuticalCompany;
import models.patient.Patient;
import models.patient.PatientDoctorInfo;
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
	 */
	
	public static Result prescripetionTest(){
		DiagnosticCentre diagnosticCentre = DiagnosticCentre.find.byId(1L);
		Prescription prescription = new Prescription();
		DiagnosticTestLineItem diagnosticTestLineItem = new DiagnosticTestLineItem();
		MasterDiagnosticTest test = new MasterDiagnosticTest();
		test.name="X-ray";
		test.description = "X-ray description";
		test.save();
		diagnosticTestLineItem.masterDiagnosticTest = test;
		prescription.diagnosticTestLineItemList.add(diagnosticTestLineItem);
		DiagnosticTestLineItem diagnosticTestLineItem2 = new DiagnosticTestLineItem();
		MasterDiagnosticTest test2 = new MasterDiagnosticTest();
		test2.name="scanning";
		test2.description = "scanning description";
		test2.save();
		diagnosticTestLineItem2.masterDiagnosticTest = test2;
		prescription.diagnosticTestLineItemList.add(diagnosticTestLineItem2);
		DiagnosticTestLineItem diagnosticTestLineItem3 = new DiagnosticTestLineItem();
		MasterDiagnosticTest test3 = new MasterDiagnosticTest();
		test3.name="CT scan";
		test3.description = "CT Scan description";
		test3.save();
		diagnosticTestLineItem3.masterDiagnosticTest = test3;
		prescription.diagnosticTestLineItemList.add(diagnosticTestLineItem3);
		prescription.save();
		diagnosticCentre.prescriptionList.add(prescription);
		diagnosticCentre.update();
		return redirect(routes.DiagnosticController.addOrderFromDoctor(diagnosticCentre.id,prescription.id));
		//return ok();
		

	}

}




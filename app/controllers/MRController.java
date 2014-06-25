package controllers;

import java.util.List;

import models.DCRLineItem;
import models.Doctor;
import models.HeadQuarter;
import models.MedicalRepresentative;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class MRController extends Controller{
	static MedicalRepresentative loggedInMR = LoginController.getLoggedInUser().getMedicalRepresentative();
	public static Form<MedicalRepresentative> medicalRepresentative=Form.form(MedicalRepresentative.class);
	public static Form<HeadQuarter> headquarter=Form.form(HeadQuarter.class);
	public static Form<DCRLineItem> DCRLineItemForm=Form.form(DCRLineItem.class);

	//add MR
	public static Result addMR(){
		return ok(views.html.mr.medicalRepresentative.render(medicalRepresentative));

	}

	public static Result mrList(){
		final List<MedicalRepresentative> mrList = MedicalRepresentative.find.where().eq("mrAdminId",LoginController.getLoggedInUser().id).findList();
		return ok(views.html.mr.mrList.render(mrList));
	}


	public static Result headQuarter(){
		return ok(views.html.mr.headQuarter.render(headquarter));
	}

	public static Result doctorList(){
		final List<Doctor> doctorList = Doctor.find.all();
		return ok(views.html.mr.doctorList.render(doctorList));
	}

	public static Result addDoctor(final Long id){
		if(loggedInMR.doctorList.contains(Doctor.find.byId(id))!=true){
			loggedInMR.doctorList.add(Doctor.find.byId(id));
		}
		return redirect(routes.MRController.mrDoctorList());

	}
	public static Result mrDoctorList(){
		return ok(views.html.mr.mrDoctor.render(loggedInMR.doctorList));

	}
	//delete doctor from mr list
	public static Result removeDoctor(final Long id){
		int indexOfDoctorList=-1;
		final Doctor doctor=Doctor.find.byId(id);
		for(final Doctor doc:loggedInMR.doctorList){
			indexOfDoctorList++;
			if(doctor.appUser.name.equals(doc.appUser.name)){
				Logger.info("doctor name : "+doc.appUser.name);

				//indexOfDoctorList=loggedInMR.doctorList.indexOf(doctor.appUser.name);

				Logger.info("index is : "+indexOfDoctorList);
				break;
			}
		}

		//return TODO;
		loggedInMR.doctorList.remove(indexOfDoctorList);

		return redirect(routes.MRController.mrDoctorList());

	}

	//for searching doctor
	public static Result search(){

		final DynamicForm requestData = Form.form().bindFromRequest();

		final String searchStr = requestData.get("searchStr");

		// if string is empty return zero
		if (searchStr != null && !searchStr.isEmpty()) {

			// it is a string, search by name
			if (searchStr.matches("[a-zA-Z]+")) {


				final List<Doctor> doctorList = Doctor.find.where().like("appUser.name", searchStr+"%").findList();

				return ok(views.html.mr.doctorList.render(doctorList));
			}else{
				return redirect(routes.MRController.doctorList());
			}

		}else{
			return redirect(routes.MRController.doctorList());
		}


	}

	//mr visits the doctor

	public static Result visitDoctor(){
		final List<Doctor> doctorList = Doctor.find.all();
		return ok(views.html.mr.DailyCallReport.render(DCRLineItemForm,doctorList));

	}
	public static Result visitDoctorProccess(){
		final List<Doctor> doctorList = Doctor.find.all();
		final Form<DCRLineItem> filledForm=DCRLineItemForm.bindFromRequest();

		if(filledForm.hasErrors()) {
			Logger.info("*** user bad request");
			return badRequest(views.html.mr.DailyCallReport.render(filledForm,doctorList));
		}
		else {
			final DCRLineItem dcrLineItem = filledForm.get();
			Logger.info("*** user object ");

			if(dcrLineItem.id == null) {
				dcrLineItem.save();
				final String message = flash("success");

			}
			else {
				dcrLineItem.update();
			}
		}


		return ok("DCRLineItem stored");
	}

	//schedule appointment for mr
	public static Result scheduleAppointment() {

		return redirect(routes.PatientController.scheduleAppointment());
	}

}

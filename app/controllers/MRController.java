package controllers;

import java.util.ArrayList;
import java.util.List;

import models.AppUser;
import models.DCRLineItem;
import models.Doctor;
import models.MedicalRepresentative;
import models.Role;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class MRController extends Controller{
	static MedicalRepresentative loggedInMR = LoginController.getLoggedInUser().getMedicalRepresentative();
	public static Form<DCRLineItem> DCRLineItemForm=Form.form(DCRLineItem.class);

	
	public static Result doctorList(){
		List<Doctor> doctorList = Doctor.find.all();
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
		Doctor doctor=Doctor.find.byId(id);
		for(Doctor doc:loggedInMR.doctorList){
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
		List<Doctor> doctorList = Doctor.find.all();
		return ok(views.html.mr.DailyCallReport.render(DCRLineItemForm,doctorList));
		
	}
	public static Result visitDoctorProccess(){
		List<Doctor> doctorList = Doctor.find.all();
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

}

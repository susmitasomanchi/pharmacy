package controllers;

import java.util.ArrayList;
import java.util.List;

import models.AppUser;
import models.Doctor;
import models.HeadQuarter;
import models.MedicalRepresentative;
import models.Role;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class MRController extends Controller{
	static MedicalRepresentative loggedInMR = LoginController.getLoggedInUser().getMedicalRepresentative();
	public static Form<MedicalRepresentative> medicalRepresentative=Form.form(MedicalRepresentative.class);
	public static Form<HeadQuarter> headquarter=Form.form(HeadQuarter.class);
	
	//add MR
	public static Result addMR(){
		return ok(views.html.mr.medicalRepresentative.render(medicalRepresentative));
		
	}
	
	public static Result headQuarter(){
		return ok(views.html.mr.headQuarter.render(headquarter));
	}
	
	public static Result doctorList(){
		List<Doctor> doctorList = Doctor.find.all();
		return ok(views.html.doctorList.render(doctorList));
	}
	
	public static Result addDoctor(final Long id){
		if(loggedInMR.doctorList.contains(Doctor.find.byId(id))!=true){
	    loggedInMR.doctorList.add(Doctor.find.byId(id));	
		}
		return ok(views.html.mrDoctor.render(loggedInMR.doctorList));
	
	}
	public static Result mrDoctorList(){
		return ok(views.html.mrDoctor.render(loggedInMR.doctorList));
		
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
	/*public static Result search() {

		final DynamicForm requestData = Form.form().bindFromRequest();

		final String searchStr = requestData.get("searchStr");

		// if string is empty return zero
		if (searchStr != null && !searchStr.isEmpty()) {

			// it is a string, search by name
			if (searchStr.matches("[a-zA-Z]+")) {

				final List<AppUser> doctors = AppUser.find.where()
						.like("name", searchStr+"%").findList();

				return ok(views.html.mrSelectDoctor.render(doctors));
			}else{
				return redirect(routes.AppointmentController.doctorList());
			}

		}else{
			return redirect(routes.AppointmentController.doctorList());
		}


	}*/

}

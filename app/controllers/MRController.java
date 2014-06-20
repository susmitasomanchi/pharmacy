package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;

import models.DCRLineItem;
import models.DailyCallReport;
import models.Doctor;
import models.HeadQuarter;
import models.MedicalRepresentative;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class MRController extends Controller{

	public static Form<MedicalRepresentative> medicalRepresentative=Form.form(MedicalRepresentative.class);
	public static Form<HeadQuarter> headquarter=Form.form(HeadQuarter.class);

	//add MR
	public static Result addMR(){
		return ok(views.html.mr.medicalRepresentative.render(medicalRepresentative));

	}

	public static Result headQuarter(){
		return ok(views.html.mr.headQuarter.render(headquarter));
	}

	public static Result mrList(){
		List<MedicalRepresentative> mrList = MedicalRepresentative.find.where().eq("mrAdminId",LoginController.getLoggedInUser().id).findList();
		return ok(views.html.mr.mrList.render(mrList));
	}

	public static Result doctorList(){
		List<Doctor> doctorList = Doctor.find.all();
		return ok(views.html.mr.doctorList.render(doctorList));
	}

	public static Result addDoctor(final Long id){
		MedicalRepresentative loggedInMr = LoginController.getLoggedInUser().getMedicalRepresentative();
		if(loggedInMr.doctorList.contains(Doctor.find.byId(id))!=true){
			loggedInMr.doctorList.add(Doctor.find.byId(id));
		}
		Logger.info(" logged IN Mr id : "+loggedInMr.appUser.name);
		loggedInMr.save();

		return redirect(routes.MRController.mrDoctorList());

	}
	public static Result mrDoctorList(){
		MedicalRepresentative loggedInMr = LoginController.getLoggedInUser().getMedicalRepresentative();
		return ok(views.html.mr.mrDoctor.render(loggedInMr.doctorList));
	}
	//delete doctor from mr list
	public static Result removeDoctor(final Long id){
		MedicalRepresentative loggedInMr = LoginController.getLoggedInUser().getMedicalRepresentative();
		int indexOfDoctorList=-1;
		Doctor doctor=Doctor.find.byId(id);
		for(Doctor doc:loggedInMr.doctorList){
			indexOfDoctorList++;
			if(doctor.appUser.name.equals(doc.appUser.name)){
				Logger.info("doctor name : "+doc.appUser.name);

				Logger.info("index is : "+indexOfDoctorList);
				break;
			}
		}

		//return TODO;
		loggedInMr.doctorList.remove(indexOfDoctorList);
		loggedInMr.update();
		//loggedInMr.doctorList.
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



	//DCR Form
	public static Result dcrForm(){
		MedicalRepresentative loggedInMr = LoginController.getLoggedInUser().getMedicalRepresentative();
		
		return ok(views.html.mr.dcrForm.render(loggedInMr.doctorList,loggedInMr.pharmaceuticalCompany.productList));

	}

	//DCR Form Submission
	public static Result processDCRForm(){
		DailyCallReport dcr = new DailyCallReport();
		DCRLineItem dcrLineItem=new DCRLineItem();
		final DynamicForm requestData = Form.form().bindFromRequest();
		final String strDate = requestData.get("date");
		Logger.info(strDate);
		//string to date
		SimpleDateFormat sdf=new SimpleDateFormat("dd-mm-yyyy");
		try {
			Date date=sdf.parse(strDate);
			dcr.forDate=date;
			//Logger.info(""+date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String doctorId[] = request().body().asFormUrlEncoded().get("doctorId");
		Logger.info("id is : "+doctorId[0]);
		
		final List<Doctor> doctorList = new ArrayList<Doctor>();
		for(int i=0;i<doctorId.length;i++){
			//Doctor.find.byId(Long.parseLong(doctorId[i]));
			doctorList.add(Doctor.find.byId(Long.parseLong(doctorId[i])));
		}
		dcrLineItem.doctorList=doctorList;
		dcrLineItem.save();
		
		//final List<DCRLineItem> dcrLineItemList = new ArrayList<DCRLineItem>();
		//for(int i=0;i<dcrLineItem.;i++){
			
		//}
		dcr.dcrLineItem= dcrLineItem;
		dcr.save();
		return ok("DCR item saved ");
	}

}

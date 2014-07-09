package controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import models.Alert;
import models.HeadQuarter;
import models.Product;
import models.doctor.Appointment;
import models.doctor.Doctor;
import models.mr.DCRLineItem;
import models.mr.DailyCallReport;
import models.mr.MedicalRepresentative;
import models.mr.Sample;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class MRController extends Controller{

	public static Form<MedicalRepresentative> medicalRepresentative=Form.form(MedicalRepresentative.class);
	public static Form<HeadQuarter> headquarter=Form.form(HeadQuarter.class);
	public static Form<DCRLineItem> dcrLineItemForm = Form.form(DCRLineItem.class);
	public static Form<DailyCallReport> dcrForm = Form.form(DailyCallReport.class);

	//add MR
	public static Result addMR(){
		return ok(views.html.mr.medicalRepresentative.render(medicalRepresentative));

	}

	public static Result headQuarter(){
		return ok(views.html.mr.headQuarter.render(headquarter));
	}

	public static Result mrList(){
		final List<MedicalRepresentative> mrList = MedicalRepresentative.find.where().eq("mrAdminId",LoginController.getLoggedInUser().id).findList();
		return ok(views.html.mr.mrList.render(mrList));
	}

	public static Result doctorList(){
		final List<Doctor> doctorList = Doctor.find.all();
		return ok(views.html.mr.doctorList.render(doctorList));
	}

	public static Result addDoctor(final Long id){
		final MedicalRepresentative loggedInMr = LoginController.getLoggedInUser().getMedicalRepresentative();
		if(loggedInMr.doctorList.contains(Doctor.find.byId(id))!=true){
			loggedInMr.doctorList.add(Doctor.find.byId(id));

		}
		loggedInMr.update();

		return redirect(routes.MRController.mrDoctorList());

	}
	public static Result mrDoctorList(){
		final MedicalRepresentative loggedInMr = LoginController.getLoggedInUser().getMedicalRepresentative();
		return ok(views.html.mr.mrDoctor.render(loggedInMr.doctorList));

	}
	//delete doctor from mr list
	public static Result removeDoctor(final Long id){
		final MedicalRepresentative loggedInMr = LoginController.getLoggedInUser().getMedicalRepresentative();
		int indexOfDoctorList=-1;
		final Doctor doctor=Doctor.find.byId(id);
		for(final Doctor doc:loggedInMr.doctorList){
			indexOfDoctorList++;
			if(doctor.appUser.name.equals(doc.appUser.name)){
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

			// it is a string, search by namDailyCallRe
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
	public static Result listDCR(){
		final MedicalRepresentative loggedInMr = LoginController.getLoggedInUser().getMedicalRepresentative();
		return ok(views.html.mr.dcrList.render(loggedInMr.dcrList));
	}

	/**
	 * @author anand
	 * url : /mr/new-dcr
	 * this method is used to capture date and store into database
	 * and date related server side validation
	 * 
	 * */
	public static Result processNewDCR(){
		boolean isExistingDCRDate = false;
		final MedicalRepresentative loggedInMr = LoginController.getLoggedInUser().getMedicalRepresentative();
		final Form<DailyCallReport> filledDCRForm = dcrForm.bindFromRequest();

		final DailyCallReport dcr = new DailyCallReport();
		final DynamicForm requestData = Form.form().bindFromRequest();
		final String strDate = requestData.get("forDate");
		if(strDate == ""){
			flash().put("alert", new Alert("alert-danger","Please Enter the Date. ").toString());
		}else{
			final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
			final DateTime myDate = formatter.parseDateTime(strDate);

			//Logger.info("entered date is : "+myDate);




			final DateTime today = new DateTime();
			Logger.info("today is : "+today);
			//Logger.info("mydate  : "+jodaMyDate);
			final int dayInterval = Days.daysBetween(myDate.withTimeAtStartOfDay() , today.withTimeAtStartOfDay() ).getDays();

			Logger.info("dayInterval : "+dayInterval);
			if(dayInterval>5){
				//Logger.info("before 5 days");
				flash().put("alert", new Alert("alert-danger","You have exceeded your DCR submission date").toString());
			}else{
				for(final DailyCallReport dCR : loggedInMr.dcrList){
					Logger.info("dcr date  is : "+dCR.forDate);
					if(dCR.forDate.equals(myDate.toDate())){
						isExistingDCRDate = true;
						flash().put("alert", new Alert("alert-danger","You have already created DCR for this date").toString());
						break;
					}
				}
				if(isExistingDCRDate == false){
					dcr.forDate = myDate.toDate();
					//Logger.info("within 5 days");
					loggedInMr.dcrList.add(dcr);
					loggedInMr.update();
				}
			}
		}
		return redirect(routes.MRController.listDCR());

	}
	/**
	 * @author anand
	 * url : /mr/show-dcr/:id
	 * 
	 * to show dcr-line-item to adding in daily call report
	 * **/
	public static Result addDCRLineItem(final Long id){
		final DailyCallReport dcr = DailyCallReport.find.byId(id);
		final MedicalRepresentative loggedInMr = LoginController.getLoggedInUser().getMedicalRepresentative();
		return ok(views.html.mr.dcrLineItem.render(dcr, dcrLineItemForm,loggedInMr.doctorList,loggedInMr.pharmaceuticalCompany.productList));

	}

	/**
	 * @author anand
	 * url : /mr/dcr/add-line-item
	 * 
	 * this shows the added dcrlinetime in daily call report of particular mr
	 */
	public static Result processDCRLineItem(){

		final MedicalRepresentative loggedInMr = LoginController.getLoggedInUser().getMedicalRepresentative();

		final String dcrId = request().body().asFormUrlEncoded().get("dcrId")[0];
		final String doctorId = request().body().asFormUrlEncoded().get("doctor")[0];

		final String sampleList[] = request().body().asFormUrlEncoded().get("sampleList");
		final String qtyList[] = request().body().asFormUrlEncoded().get("qtyList");
		final String promotionList[] = request().body().asFormUrlEncoded().get("promotionList");
		final String inTime = request().body().asFormUrlEncoded().get("inTime")[0];
		final String outTime = request().body().asFormUrlEncoded().get("outTime")[0];
		final String pob = request().body().asFormUrlEncoded().get("pob")[0];
		final String remarks = request().body().asFormUrlEncoded().get("remarks")[0];


		final DCRLineItem dcrLineItem = new DCRLineItem();
		final DailyCallReport dcr = DailyCallReport.find.byId(Long.parseLong(dcrId));


		dcrLineItem.doctor=Doctor.find.byId(Long.parseLong(doctorId));

		Logger.info("sample size: "+sampleList.length);

		for(int i=0;i<sampleList.length;i++){
			final Sample sample = new Sample();

			if((sampleList[i].compareToIgnoreCase("")==0)||(sampleList[i].compareToIgnoreCase("0")==0)){

			}else{
				sample.product = Product.find.byId(Long.parseLong(sampleList[i]));
				if(qtyList[i] == ""){
					sample.quantity = 0;
				}else{
					sample.quantity = Integer.parseInt(qtyList[i]);
				}
				dcrLineItem.sampleList.add(sample);
			}


			//Logger.info("product is : "+sample.product.medicineName);
			//Logger.info("quantity is : "+sample.quantity);

		}

		Logger.info("promotion is: "+promotionList);
		if(promotionList == null){

		}else{
			for(int i=0;i<promotionList.length;i++){
				dcrLineItem.promotionList.add(Product.find.byId(Long.parseLong(promotionList[i])));
			}
		}

		dcrLineItem.inTime = inTime;
		dcrLineItem.outTime = outTime;

		if(pob==""){
			dcrLineItem.pob=0;
		}else{
			dcrLineItem.pob = Integer.parseInt(pob);
		}

		dcrLineItem.remarks = remarks;

		dcr.dcrLineItemList.add(dcrLineItem);
		dcr.update();


		return ok(views.html.mr.filledDCRLineItem.render(dcr.dcrLineItemList));
	}

	/**
	 * @author anand
	 * url : /mr/dcr/delete-line-item/:dcrid/:lineitemid
	 * 
	 * to remove the added dcr-line-item
	 * 
	 * @param dcrId
	 * @param lineItemId
	 * @return
	 */
	public static Result removeDCRLineItem(final Long dcrId,final Long lineItemId){

		final DailyCallReport dcr = DailyCallReport.find.byId(dcrId);
		final DCRLineItem lineItem = DCRLineItem.find.byId(lineItemId);
		dcr.dcrLineItemList.remove(lineItem);
		lineItem.delete();
		dcr.update();
		return ok(views.html.mr.filledDCRLineItem.render(dcr.dcrLineItemList));
	}
	//schedule appointment for mr
	public static Result scheduleAppointment(final String docID) {
		List<Appointment> listAppointments=null;
		final Map<Date, List<Appointment>> appointmentMap = new LinkedHashMap<Date, List<Appointment>>();
		final Doctor doctor=Doctor.find.byId(Long.parseLong(docID));
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		//		calendar.set(Calendar.HOUR_OF_DAY,doctor.doctorClinicInfoList.get(0).fromHrsMr);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);
		int size=0;

		for(int i=0;i<20;i++){
			listAppointments = Appointment.getAvailableMrAppointmentList(doctor, calendar.getTime());
			if(listAppointments.size()!=0){
				appointmentMap.put(calendar.getTime(), listAppointments);
				size=listAppointments.size();
			}
			Logger.error(listAppointments.size()+"Test");

			calendar.add(Calendar.DATE, 1);
			//			calendar.set(Calendar.HOUR_OF_DAY,doctor.doctorClinicInfoList.get(0).fromHrsMr);
			calendar.set(Calendar.MINUTE,0);
			calendar.set(Calendar.SECOND,0);
			calendar.set(Calendar.MILLISECOND,0);
			System.out.print(calendar.getTime());
		}
		return ok(views.html.patient.scheduleAppointment.render(appointmentMap,size));
	}
}

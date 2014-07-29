package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import models.Alert;
import models.AppUser;
import models.mr.PharmaceuticalProduct;
import models.State;
import models.doctor.Appointment;
import models.doctor.Doctor;
import models.mr.DCRLineItem;
import models.mr.DCRStatus;
import models.mr.DailyCallReport;
import models.mr.HeadQuarter;
import models.mr.MedicalRepresentative;
import models.mr.PharmaceuticalCompany;
import models.mr.Sample;
import models.mr.TourPlan;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import actions.BasicAuth;
import beans.MedicalRepresentativeBean;

@BasicAuth
public class MRController extends Controller {

	public static Map<MedicalRepresentative, List<MedicalRepresentative>> mgrMap = new HashMap<MedicalRepresentative, List<MedicalRepresentative>>();

	public static Form<MedicalRepresentative> medicalRepresentative = Form
			.form(MedicalRepresentative.class);
	public static Form<HeadQuarter> headQuarter = Form.form(HeadQuarter.class);
	public static Form<DCRLineItem> dcrLineItemForm = Form
			.form(DCRLineItem.class);
	public static Form<HeadQuarter> headquarter = Form.form(HeadQuarter.class);
	public static Form<DailyCallReport> dcrForm = Form
			.form(DailyCallReport.class);
	public static Form<MedicalRepresentativeBean> mrForm = Form
			.form(MedicalRepresentativeBean.class);

	/**
	 * 
	 * @author Dibesh
	 * 
	 *         This method redirects to add medical representative page
	 * 
	 *         GET /mr/add-mr controllers.MRController.addMR()
	 */

	public static Result addMR() {
		final MedicalRepresentative mr = new MedicalRepresentative();
		final List<MedicalRepresentative> mrList = MedicalRepresentative.find
				.where().eq("appUser.role", "MR").findList();
		return ok(views.html.mr.medicalRepresentative.render(mrForm, mrList));
	}

	/**
	 * 
	 * @author Dibesh
	 * 
	 *         This method persists the medical representative data as well as
	 *         update mr
	 * 
	 *         POST /mr/add
	 *         controllers.MRController.medicalRepresentativeProccess()
	 */

	public static Result medicalRepresentativeProccess() {

		final Form<MedicalRepresentativeBean> filledForm = mrForm
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			Logger.info("*** user bad request");
			final List<MedicalRepresentative> mrList = MedicalRepresentative.find
					.where().eq("appUser.role", "MR").findList();
			return ok(views.html.mr.medicalRepresentative
					.render(mrForm, mrList));
		}

		else {

			final MedicalRepresentativeBean medicalRepresentativeBean = filledForm
					.get();

			final MedicalRepresentative mr = medicalRepresentativeBean
					.toMedicalRepresentative();

			final AppUser appUser = medicalRepresentativeBean.toAppUser();

			final PharmaceuticalCompany company = LoginController
					.getLoggedInUser().getMedicalRepresentative().pharmaceuticalCompany;

			final MedicalRepresentative adminMr = LoginController
					.getLoggedInUser().getMedicalRepresentative();
			if (mr.id == null) {
				appUser.save();
				mr.pharmaceuticalCompany = company;
				mr.appUser = appUser;
				if (medicalRepresentativeBean.manager != null) {
					mr.manager = MedicalRepresentative.find
							.byId(medicalRepresentativeBean.manager);
				}

				mr.save();
				adminMr.pharmaceuticalCompany.mrList.add(mr);
				adminMr.pharmaceuticalCompany.update();

			} else {
				Logger.info("not null");

				appUser.update();

				mr.pharmaceuticalCompany = company;
				mr.appUser = appUser;
				mr.manager = MedicalRepresentative.find
						.byId(medicalRepresentativeBean.manager);
				mr.update();
				adminMr.pharmaceuticalCompany.mrList.add(mr);
				adminMr.pharmaceuticalCompany.update();

			}

		}
		return redirect(routes.MRController.mrList());
	}

	public static Result headQuarter() {
		return ok(views.html.mr.headQuarter.render(headquarter));
	}

	/**
	 * @author anand
	 * @discription : this method is saving the headQuarter
	 * 
	 *              url : POST POST /mr/add-head-quarter
	 * 
	 * 
	 * url :	POST  POST   /mr/add-head-quarter
	 * */
	public static Result addHeadQuarter() {
		final MedicalRepresentative loggedInMr = LoginController
				.getLoggedInUser().getMedicalRepresentative();

		final Form<HeadQuarter> filledHeadQuarterForm = headquarter
				.bindFromRequest();

		if (filledHeadQuarterForm.hasErrors()) {
			return ok(views.html.mr.headQuarter.render(filledHeadQuarterForm));
		} else {
			final HeadQuarter headQuarter = filledHeadQuarterForm.get();
			loggedInMr.headQuarterList.add(headQuarter);
			loggedInMr.update();

		}
		return ok();
	}

	/**
	 * 
	 * @author Dibesh
	 * 
	 *         This method displays the all the mr present under the logged in
	 *         mr
	 * 
	 *         GET /mr/list-mr controllers.MRController.mrList()
	 */

	public static Result mrList() {
		final MedicalRepresentative loggedInMR = LoginController
				.getLoggedInUser().getMedicalRepresentative();
		// Logger.info(mrList.);
		// final List<MedicalRepresentative> mrList =
		// MedicalRepresentative.find.where().eq("companyName",
		// loggedInMR.companyName).findList();
		return ok(views.html.mr.mrList
				.render(MedicalRepresentative.find
						.where()
						.eq("pharmaceutical_company_id",
								LoginController.getLoggedInUser()
								.getMedicalRepresentative().pharmaceuticalCompany.id)
								.findList()));
	}

	/**
	 * 
	 * @author Dibesh
	 * 
	 *         This method changes the status of mr
	 * 
	 *         GET /mr/del controllers.MRController.removeMR(id: Long)
	 */

	public static Result removeMR(final Long id) {
		final MedicalRepresentative loggedInMR = LoginController
				.getLoggedInUser().getMedicalRepresentative();
		final MedicalRepresentative mr = MedicalRepresentative.find.byId(id);
		mr.isActive = false;
		mr.update();
		return ok(views.html.mr.mrList
				.render(MedicalRepresentative.find
						.where()
						.eq("pharmaceutical_company_id",
								LoginController.getLoggedInUser()
								.getMedicalRepresentative().pharmaceuticalCompany.id)
								.findList()));

	}

	/**
	 * 
	 * @author Dibesh
	 * 
	 *         This method includes functionality to edit mr
	 * 
	 *         GET /mr/edit/:id controllers.MRController.editMR(id: Long)
	 */

	public static Result editMR(final Long id) {

		final MedicalRepresentative filledMr = MedicalRepresentative.find
				.byId(id);

		Logger.info("filled mr id is : " + filledMr.id);

		final Form<MedicalRepresentativeBean> editForm = mrForm.fill(filledMr
				.toBean());
		// final List<AppUser> mrList
		// =AppUser.find.where().eq("role","MR").findList();
		final List<MedicalRepresentative> mrList = MedicalRepresentative.find
				.where().eq("appUser.role", "MR").findList();
		return ok(views.html.mr.medicalRepresentative.render(editForm, mrList));
	}

	/**
	 * @author anand
	 * 
	 *         url : GET /doctor-list
	 * 
	 * @description: It shows all the doctor which is join through this
	 *               application
	 * 
	 * */
	public static Result doctorList() {
		final List<Doctor> doctorList = Doctor.find.all();
		return ok(views.html.mr.doctorList.render(doctorList));
	}

	/**
	 * @author anand
	 * 
	 *         url : GET /mr/add-doctor/:id
	 * 
	 * @description: this method is used to add the doctor in particular Admin
	 *               mr from avilable doctor in this appps
	 * */
	public static Result addDoctor(final Long id) {
		final MedicalRepresentative loggedInMr = LoginController
				.getLoggedInUser().getMedicalRepresentative();
		if (loggedInMr.doctorList.contains(Doctor.find.byId(id)) != true) {
			loggedInMr.doctorList.add(Doctor.find.byId(id));

		}
		loggedInMr.update();

		return redirect(routes.MRController.mrDoctorList());

	}

	/**
	 * @author anand
	 * 
	 *         url : GET /mr/doctor-list
	 * 
	 * @description: whatever the doctor added to the admin mr ,It shows all of
	 *               them.
	 * */
	public static Result mrDoctorList() {
		final MedicalRepresentative loggedInMr = LoginController
				.getLoggedInUser().getMedicalRepresentative();
		return ok(views.html.mr.mrDoctor.render(loggedInMr.doctorList));

	}

	/**
	 * @author anand
	 * 
	 *         url : GET /mr/remove-doctor/:id
	 * 
	 * @description:this method is used to remove the doctor which belongs to
	 *                   Admin mr
	 * 
	 * */
	public static Result removeDoctor(final Long id) {
		final MedicalRepresentative loggedInMr = LoginController
				.getLoggedInUser().getMedicalRepresentative();
		int indexOfDoctorList = -1;
		final Doctor doctor = Doctor.find.byId(id);
		for (final Doctor doc : loggedInMr.doctorList) {
			indexOfDoctorList++;
			if (doctor.appUser.name.equals(doc.appUser.name)) {
				break;
			}
		}

		// return TODO;
		loggedInMr.doctorList.remove(indexOfDoctorList);
		loggedInMr.update();
		// loggedInMr.doctorList.
		return redirect(routes.MRController.mrDoctorList());

	}

	/**
	 * @author anand
	 * 
	 * @description: to search the doctor for Admin mr.
	 * 
	 *               url : GET /search
	 * 
	 * */
	public static Result search() {

		final DynamicForm requestData = Form.form().bindFromRequest();

		final String searchStr = requestData.get("searchStr");

		// if string is empty return zero
		if (searchStr != null && !searchStr.isEmpty()) {

			// it is a string, search by namDailyCallRe
			if (searchStr.matches("[a-zA-Z]+")) {

				final List<Doctor> doctorList = Doctor.find.where()
						.like("appUser.name", searchStr + "%").findList();

				return ok(views.html.mr.doctorList.render(doctorList));
			} else {
				return redirect(routes.MRController.doctorList());
			}

		} else {
			return redirect(routes.MRController.doctorList());
		}

	}

	/**
	 * @author anand@quarter.state
	 * 
	 *         url : GET /mr/dcr-list
	 * 
	 * @description: this method shows list of all dcr date wise for particular
	 *               Admin Mr
	 * 
	 * */
	public static Result listDCR() {
		final MedicalRepresentative loggedInMr = LoginController
				.getLoggedInUser().getMedicalRepresentative();
		final Map<State, List<HeadQuarter>> headQmap = new LinkedHashMap<State, List<HeadQuarter>>();

		for (final HeadQuarter headQuarter : loggedInMr.headQuarterList) {
			if (headQmap.containsKey(headQuarter.state)) {
				final List<HeadQuarter> headQuarterList = headQmap
						.get(headQuarter.state);
				headQuarterList.add(headQuarter);

			} else {
				final List<HeadQuarter> headQuarterList = new ArrayList<HeadQuarter>();
				headQuarterList.add(headQuarter);
				headQmap.put(headQuarter.state, headQuarterList);
			}
		}
		final List<MedicalRepresentative> mySubordinatelist = loggedInMr.getSubordinates();
		//final List<DailyCallReport> mySubordinateDCRList =  loggedInMr.getSubordinatesDCRList();
		final List<DailyCallReport> mySubordinateDCRList =  new ArrayList<DailyCallReport>();
		return ok(views.html.mr.dcrList.render(loggedInMr.dcrList, headQmap,mySubordinatelist,mySubordinateDCRList));
	}

	/**
	 * @author anand
	 * 
	 * @description: this method is used to capture date and store into database
	 *               and date related server side validation
	 * 
	 *               url : POST /mr/new-dcr
	 * 
	 * */
	public static Result processNewDCR() {
		boolean isExistingDCRDate = false;
		final MedicalRepresentative loggedInMr = LoginController
				.getLoggedInUser().getMedicalRepresentative();

		final DailyCallReport dcr = new DailyCallReport();
		final DynamicForm requestData = Form.form().bindFromRequest();
		final String strDate = requestData.get("forDate");
		Logger.info("entered date is : "+strDate);
		final String quarterIdString = requestData.get("headQuarter");
		if (dcr.dcrStatus == null) {
			dcr.dcrStatus = DCRStatus.DRAFT;
		}
		if (quarterIdString != "") {
			final Long headQuarterId = Long.parseLong(quarterIdString);
			dcr.headQuarter = HeadQuarter.find.byId(headQuarterId);
		}
		// dcr.save();
		if (strDate == "") {
			flash().put(
					"alert",
					new Alert("alert-danger", "Please Enter the Date. ")
					.toString());
		} else {
			final DateTimeFormatter formatter = DateTimeFormat
					.forPattern("dd-MM-yyyy");
			final DateTime myDate = formatter.parseDateTime(strDate);

			final DateTime today = new DateTime();
			final int dayInterval = Days
					.daysBetween(myDate.withTimeAtStartOfDay(),
							today.withTimeAtStartOfDay()).getDays();

			// Logger.info("dayInterval : "+dayInterval);
			if (dayInterval > 5) {
				flash().put(
						"alert",
						new Alert("alert-danger",
								"You have exceeded your DCR submission date")
						.toString());
			} else {
				for (final DailyCallReport dCR : loggedInMr.dcrList) {
					// Logger.info("dcr date  is : "+dCR.forDate);
					if (dCR.forDate.equals(myDate.toDate())) {
						isExistingDCRDate = true;
						flash().put(
								"alert",
								new Alert("alert-danger",
										"You have already created DCR for this date")
								.toString());
						break;
					}
				}
				if (isExistingDCRDate == false) {
					dcr.forDate = myDate.toDate();
					// Logger.info("within 5 days");
					loggedInMr.dcrList.add(dcr);
					loggedInMr.update();
				}
			}
		}
		return redirect(routes.MRController.listDCR());

	}

	/**
	 * @author anand
	 * 
	 *         url : GET /mr/show-dcr/:id
	 * 
	 * @description: to show dcr-line-item to adding in daily call report
	 * **/
	public static Result addDCRLineItem(final Long id) {
		final DailyCallReport dcr = DailyCallReport.find.byId(id);
		final MedicalRepresentative loggedInMr = LoginController
				.getLoggedInUser().getMedicalRepresentative();
		final List<Doctor> disabledDoctorList = new ArrayList<Doctor>();
		for (final DCRLineItem lineItem : dcr.dcrLineItemList) {
			disabledDoctorList.add(lineItem.doctor);
		}

		return ok(views.html.mr.dcrLineItem.render(dcr, dcrLineItemForm,
				loggedInMr.doctorList, disabledDoctorList,
				loggedInMr.pharmaceuticalCompany.productList,loggedInMr));

	}

	/**
	 * @author anand
	 * 
	 * url :GET    /mr/approve-dcr/:dcrid
	 * 
	 * @description : manager can approve mr's submitted dcr
	 * 
	 * 
	 * */

	public static Result approveDCRLineItem( final Long dcrId){

		final DailyCallReport dcr = DailyCallReport.find.byId(dcrId);

		final MedicalRepresentative loggedInMr = LoginController
				.getLoggedInUser().getMedicalRepresentative();
		dcr.dcrStatus = DCRStatus.APPROVED;
		dcr.approver = loggedInMr;
		dcr.responseOn = new Date();
		dcr.update();


		return redirect(routes.MRController.listDCR());
	}

	/**
	 * @author anand
	 * 
	 * url :GET    /mr/reject-dcr/:dcrid
	 * 
	 * @description : manager can reject mr's submitted dcr
	 * 
	 * 
	 * */
	public static Result rejectDCRLineItem(final Long dcrId){
		final DailyCallReport dcr = DailyCallReport.find.byId(dcrId);
		final MedicalRepresentative loggedInMr = LoginController
				.getLoggedInUser().getMedicalRepresentative();
		dcr.dcrStatus = DCRStatus.REJECTED;
		dcr.approver = loggedInMr;
		dcr.responseOn = new Date();
		dcr.update();

		return redirect(routes.MRController.listDCR());
	}
	/**
	 * @author anand
	 * 
	 * url :GET    /mr/delete-dcr/:dcrid
	 * 
	 * @description : mr can delete own dcr which is not  submitted.
	 * 
	 * 
	 * */

	public static Result deleteDCR(final Long dcrid){
		final MedicalRepresentative loggedInMr = LoginController
				.getLoggedInUser().getMedicalRepresentative();
		final DailyCallReport dcr = DailyCallReport.find.byId(dcrid);
		Logger.info(dcr.submitter.appUser.name);
		loggedInMr.dcrList.remove(dcr);
		loggedInMr.update();
		dcr.delete();

		return redirect(routes.MRController.listDCR());
	}

	/**
	 * @author anand
	 * 
	 *         url : POST /mr/dcr/add-line-item
	 * 
	 * @description: this shows the added dcrlinetime in daily call report of
	 *               particular mr
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static Result processDCRLineItem() {

		final MedicalRepresentative loggedInMr = LoginController
				.getLoggedInUser().getMedicalRepresentative();

		final String dcrId = request().body().asFormUrlEncoded().get("dcrId")[0];
		final String doctorId = request().body().asFormUrlEncoded()
				.get("doctor")[0];

		final String sampleList[] = request().body().asFormUrlEncoded()
				.get("sampleList");
		final String qtyList[] = request().body().asFormUrlEncoded()
				.get("qtyList");
		final String promotionList[] = request().body().asFormUrlEncoded()
				.get("promotionList");
		final String inTime = request().body().asFormUrlEncoded().get("inTime")[0];
		final String outTime = request().body().asFormUrlEncoded()
				.get("outTime")[0];
		final String pob = request().body().asFormUrlEncoded().get("pob")[0];
		final String remarks = request().body().asFormUrlEncoded()
				.get("remarks")[0];

		final DCRLineItem dcrLineItem = new DCRLineItem();
		final DailyCallReport dcr = DailyCallReport.find.byId(Long
				.parseLong(dcrId));

		dcrLineItem.doctor = Doctor.find.byId(Long.parseLong(doctorId));

		// Logger.info("doctor Id : "+doctorId);
		for (int i = 0; i < sampleList.length; i++) {
			final Sample sample = new Sample();

			if ((sampleList[i].compareToIgnoreCase("") == 0)) {
			} else {
				sample.product = PharmaceuticalProduct.find.byId(Long
						.parseLong(sampleList[i]));
				if (qtyList[i] == "") {
					sample.quantity = 0;
				} else {
					sample.quantity = Integer.parseInt(qtyList[i]);
				}
				dcrLineItem.sampleList.add(sample);
			}
		}

		if (promotionList == null) {
		} else {
			for (int i = 0; i < promotionList.length; i++) {
				dcrLineItem.promotionList.add(PharmaceuticalProduct.find.byId(Long
						.parseLong(promotionList[i])));
			}
		}

		final Date dcrDate = dcr.forDate;
		final DateTimeFormatter formatter = DateTimeFormat.forPattern("kk:mm");

		final DateTime inDateTime = new DateTime(dcrDate);
		final DateTime outDateTime = new DateTime(dcrDate);
		if (inTime.compareToIgnoreCase("") == 0) {
		} else {
			final DateTime fromTime = formatter.parseDateTime(inTime);
			final DateTime inDateTimeHours = inDateTime.plusHours(fromTime
					.getHourOfDay());
			final DateTime inDateTimeHoursMin = inDateTimeHours
					.plusMinutes(fromTime.getMinuteOfDay()
							- (60 * fromTime.getHourOfDay()));
			dcrLineItem.inTime = inDateTimeHoursMin.toDate();
		}
		if (outTime.compareToIgnoreCase("") == 0) {
		} else {
			final DateTime toTime = formatter.parseDateTime(outTime);
			final DateTime outDateTimeHours = outDateTime.plusHours(toTime
					.getHourOfDay());
			final DateTime outDateTimeHoursMin = outDateTimeHours
					.plusMinutes(toTime.getMinuteOfDay()
							- (60 * toTime.getHourOfDay()));
			dcrLineItem.outTime = outDateTimeHoursMin.toDate();
		}

		if (pob == "") {
			dcrLineItem.pob = 0;
		} else {
			dcrLineItem.pob = Integer.parseInt(pob);
		}

		dcrLineItem.remarks = remarks;

		dcr.dcrLineItemList.add(dcrLineItem);

		dcr.update();

		return ok(views.html.mr.filledDCRLineItem.render(dcr.dcrLineItemList));
	}

	/**
	 * @author anand
	 * 
	 *         url : POST /mr/dcr/delete-line-item/:dcrid/:lineitemid
	 * 
	 * @description: to remove the added dcr-line-item
	 * 
	 * @param dcrId
	 * @param lineItemId
	 * 
	 */
	public static Result removeDCRLineItem(final Long dcrId,
			final Long lineItemId) {
		final DailyCallReport dcr = DailyCallReport.find.byId(dcrId);
		final DCRLineItem lineItem = DCRLineItem.find.byId(lineItemId);
		dcr.dcrLineItemList.remove(lineItem);
		lineItem.delete();
		dcr.update();
		return ok(views.html.mr.filledDCRLineItem.render(dcr.dcrLineItemList));
	}

	/**
	 * @author anand
	 * 
	 * @description : this method is used to submit the mr's dcr line items to
	 *              crossponding manager
	 * 
	 * url: POST /mr/submit-dcr-line-items/:dcrid
	 * 
	 * @param dcrId
	 * 
	 * */

	public static Result submitDCRLineItems(final Long dcrId) {

		final MedicalRepresentative loggedInMr = LoginController
				.getLoggedInUser().getMedicalRepresentative();
		final DailyCallReport dcr = DailyCallReport.find.byId(dcrId);
		dcr.submitter = loggedInMr;
		dcr.submittedDate = new Date();
		dcr.dcrStatus = DCRStatus.SUBMITTED;
		dcr.update();

		return redirect(routes.MRController.listDCR());
	}
	/**
	 * @author anand
	 * 
	 * @description : this method is used to display the dcr of which u selected
	 * 
	 * url: GET /mr/search-subordinate-dcr
	 * 
	 * */
	public static Result searchSubordinateDcr(){
		boolean isSearchDCRForAll = false;
		final MedicalRepresentative loggedInMr = LoginController.getLoggedInUser().getMedicalRepresentative();
		final String subordinateId[] = request().body().asFormUrlEncoded().get("dcr-subordinate");
		final List<MedicalRepresentative> subordinateList = new ArrayList<MedicalRepresentative>();
		for(int i=0;i<subordinateId.length;i++){
			if(subordinateId[i].compareToIgnoreCase("0")==0){
				isSearchDCRForAll = true;
				subordinateList.removeAll(subordinateList);
				break;
			}else{
				subordinateList.add(MedicalRepresentative.find.byId(Long.parseLong(subordinateId[i])));
			}
		}

		final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
		Date dcrFromDate = new Date();
		Date dcrToDate = new Date();

		final String dcrFromDateStr = request().body().asFormUrlEncoded().get("dcr-from-date")[0];
		if(dcrFromDateStr != ""){
			dcrFromDate = formatter.parseDateTime(dcrFromDateStr).toDate();
		}

		final String dcrToDateStr = request().body().asFormUrlEncoded().get("dcr-to-date")[0];
		if(dcrToDateStr != ""){
			dcrToDate = formatter.parseDateTime(dcrToDateStr).toDate();
		}

		List<DailyCallReport> mySubordinateDCRList = new ArrayList<DailyCallReport>();
		if(isSearchDCRForAll){
			if(dcrFromDateStr != "" && dcrToDateStr != ""){
				mySubordinateDCRList = DailyCallReport.find.where().in("submitter", loggedInMr.getSubordinates()).ne("dcrStatus",DCRStatus.DRAFT).le("forDate", dcrToDate).ge("forDate", dcrFromDate).orderBy("forDate DESC").findList();
			}else{
				if(dcrFromDateStr != ""){
					mySubordinateDCRList = DailyCallReport.find.where().in("submitter", loggedInMr.getSubordinates()).ne("dcrStatus",DCRStatus.DRAFT).ge("forDate", dcrFromDate).orderBy("forDate DESC").findList();
				}else{
					if(dcrToDateStr != ""){
						mySubordinateDCRList = DailyCallReport.find.where().in("submitter", loggedInMr.getSubordinates()).ne("dcrStatus",DCRStatus.DRAFT).le("forDate", dcrToDate).orderBy("forDate DESC").findList();
					}else{
						//mySubordinateDCRList =  loggedInMr.getSubordinatesDCRList();
						mySubordinateDCRList = DailyCallReport.find.where().in("submitter", loggedInMr.getSubordinates()).ne("dcrStatus",DCRStatus.DRAFT).orderBy("forDate DESC").findList();
					}
				}
			}

		}else{
			if(dcrFromDateStr != "" && dcrToDateStr != ""){
				mySubordinateDCRList = DailyCallReport.find.where().in("submitter", subordinateList).ne("dcrStatus",DCRStatus.DRAFT).le("forDate", dcrToDate).ge("forDate", dcrFromDate).orderBy("forDate DESC").findList();
			}else{
				if(dcrFromDateStr != ""){
					mySubordinateDCRList = DailyCallReport.find.where().in("submitter", subordinateList).ne("dcrStatus",DCRStatus.DRAFT).ge("forDate", dcrFromDate).orderBy("forDate DESC").findList();
				}else{
					if(dcrToDateStr != ""){
						mySubordinateDCRList = DailyCallReport.find.where().in("submitter", subordinateList).ne("dcrStatus",DCRStatus.DRAFT).le("forDate", dcrToDate).orderBy("forDate DESC").findList();
					}else{
						mySubordinateDCRList = DailyCallReport.find.where().in("submitter", subordinateList).ne("dcrStatus",DCRStatus.DRAFT).orderBy("forDate DESC").findList();
					}
				}
			}
			//mySubordinateDCRList = DailyCallReport.find.where().in("submitter", subordinateList).ne("dcrStatus",DCRStatus.DRAFT).le("forDate", dcrToDate).ge("forDate", dcrFromDate).orderBy("forDate DESC").findList();
		}

		final Map<State, List<HeadQuarter>> headQmap = new LinkedHashMap<State, List<HeadQuarter>>();

		for (final HeadQuarter headQuarter : loggedInMr.headQuarterList) {
			if (headQmap.containsKey(headQuarter.state)) {
				final List<HeadQuarter> headQuarterList = headQmap
						.get(headQuarter.state);
				headQuarterList.add(headQuarter);

			} else {
				final List<HeadQuarter> headQuarterList = new ArrayList<HeadQuarter>();
				headQuarterList.add(headQuarter);
				headQmap.put(headQuarter.state, headQuarterList);
			}
		}
		final List<MedicalRepresentative> mySubordinatelist = loggedInMr.getSubordinates();
		return ok(views.html.mr.dcrList.render(loggedInMr.dcrList, headQmap,mySubordinatelist,mySubordinateDCRList));


	}

	// schedule appointment for mr
	public static Result scheduleAppointment(final String docID) {
		List<Appointment> listAppointments = null;
		final Map<Date, List<Appointment>> appointmentMap = new LinkedHashMap<Date, List<Appointment>>();
		final Doctor doctor = Doctor.find.byId(Long.parseLong(docID));
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		// calendar.set(Calendar.HOUR_OF_DAY,doctor.doctorClinicInfoList.get(0).fromHrsMr);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		int size = 0;

		for (int i = 0; i < 20; i++) {
			listAppointments = Appointment.getAvailableMrAppointmentList(
					doctor, calendar.getTime());
			if (listAppointments.size() != 0) {
				appointmentMap.put(calendar.getTime(), listAppointments);
				size = listAppointments.size();
			}
			// Logger.error(listAppointments.size()+"Test");

			calendar.add(Calendar.DATE, 1);
			// calendar.set(Calendar.HOUR_OF_DAY,doctor.doctorClinicInfoList.get(0).fromHrsMr);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			System.out.print(calendar.getTime());
		}
		return ok(views.html.patient.scheduleAppointment.render(appointmentMap,
				size));
	}

	/**
	 * 
	 * @author Dibesh
	 * 
	 *         This method displays hierarchy of MR
	 * 
	 *         GET /mr/hierarchy controllers.MRController.viewHierarchy()
	 */
	public static Result viewHierarchy() {

		final List<MedicalRepresentative> mrList = MedicalRepresentative.find
				.where()
				.eq("pharmaceutical_company_id",
						LoginController.getLoggedInUser()
						.getMedicalRepresentative().pharmaceuticalCompany.id)
						.findList();
		Logger.info(mrList.toString());
		return ok(views.html.mr.organizationStructure.render(mrList));

	}
	/**
	 * @author anand
	 * 
	 * @description : this method render to the monthlyTourPlan view to take input as month and year
	 * 
	 * url : GET    /mr/monthly-tour-plan
	 * 
	 * */
	public static Result tourPlan(){
		final MedicalRepresentative loggedInMr = LoginController.getLoggedInUser().getMedicalRepresentative();
		return ok(views.html.mr.tourPlan.render(loggedInMr.tourPlanList));
	}

	/**
	 * @author anand
	 * 
	 * @description : this method is save month and year and make templete to fill the tour plan
	 * 
	 * url : POST	/mr/add-month-for-tour-plan
	 * 
	 * */
	public static Result addTourPlan(){

		final MedicalRepresentative loggedInmr = LoginController.getLoggedInUser().getMedicalRepresentative();
		final String monthForTourPlanStr = request().body().asFormUrlEncoded().get("dateForTourPlan")[0];
		Logger.info("Date for tour plan : "+monthForTourPlanStr);
		final DateTimeFormatter formatter = DateTimeFormat.forPattern("MM-yyyy");
		final Date monthForTourPlan = formatter.parseDateTime(monthForTourPlanStr).toDate();
		Logger.info("Date for tour plan : "+monthForTourPlan);
		final TourPlan tourPlan = new TourPlan();
		tourPlan.forMonth = monthForTourPlan;
		loggedInmr.tourPlanList.add(tourPlan);
		loggedInmr.update();


		return ok(views.html.mr.tourPlan.render(loggedInmr.tourPlanList));
	}

	/**
	 * @author anand
	 * 
	 * @description : this method is rendering to tour plan page,where MR has to fil the tourPlan
	 * 
	 * url : GET    /mr/tourplan-lineitem
	 * 
	 * 
	 * */
	public static Result tourPlanLineItem(final Long lineItemId){

		final TourPlan tourPlan = TourPlan.find.byId(lineItemId);
		final Calendar calender = Calendar.getInstance();
		calender.setTime(tourPlan.forMonth);
		final int maxDaysInMonth = calender.getActualMaximum(Calendar.DAY_OF_MONTH);
		final Map<Integer,TourPlan> tourPlanMap = new LinkedHashMap<Integer,TourPlan>();

		for(int i=1;i<=maxDaysInMonth;i++){
			tourPlanMap.put(i, tourPlan);
		}
		return ok(views.html.mr.tpLineItem.render(tourPlanMap));
	}
}

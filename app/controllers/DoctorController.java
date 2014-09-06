package controllers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import models.Alert;
import models.AppUser;
import models.MasterDiagnosticTest;
import models.MasterProduct;
import models.Role;
import models.diagnostic.DiagnosticCentre;
import models.diagnostic.DiagnosticCentrePrescriptionInfo;
import models.diagnostic.DiagnosticCentrePrescritionStatus;
import models.doctor.Appointment;
import models.doctor.AppointmentStatus;
import models.doctor.Clinic;
import models.doctor.Day;
import models.doctor.DaySchedule;
import models.doctor.DiagnosticTestLineItem;
import models.doctor.Doctor;
import models.doctor.DoctorAward;
import models.doctor.DoctorClinicInfo;
import models.doctor.DoctorDiagnosticTest;
import models.doctor.DoctorEducation;
import models.doctor.DoctorExperience;
import models.doctor.DoctorProduct;
import models.doctor.DoctorSocialWork;
import models.doctor.MasterSpecialization;
import models.doctor.MedicineLineItem;
import models.doctor.Prescription;
import models.doctor.QuestionAndAnswer;
import models.doctor.SigCode;
import models.pharmacist.Pharmacy;
import models.pharmacist.PharmacyPrescriptionInfo;
import models.pharmacist.PharmacyPrescriptionStatus;

import org.json.JSONArray;

import play.Logger;
import play.data.Form;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;
import utils.EmailService;
import utils.SMSService;
import actions.BasicAuth;
import actions.ConfirmAppUser;
import beans.DoctorClinicInfoBean;
import beans.PrescriptionBean;
import beans.QuestionAndAnswerBean;

import com.avaje.ebean.Ebean;
import com.google.common.io.Files;

@BasicAuth
public class DoctorController extends Controller {

	public static Form<DoctorClinicInfoBean> clinicForm = Form.form(DoctorClinicInfoBean.class);
	public static Form<QuestionAndAnswerBean> questionAndAnswerForm = Form.form(QuestionAndAnswerBean.class);
	public static Form<DoctorClinicInfo> doctorClinicForm = Form.form(DoctorClinicInfo.class);
	public static Form<PrescriptionBean> prescriptionForm = Form.form(PrescriptionBean.class);

	/**
	 * Action to update basic field of doctor like name, specialization, degree
	 * etc. POST /secure-doctor/doctor-basic-update
	 */
	public static Result basicUpdate() {
		final Map<String, String[]> requestMap = request().body()
				.asFormUrlEncoded();
		try {
			final Doctor doctor = Doctor.find.byId(Long.parseLong(requestMap
					.get("doctorId")[0]));

			// Server side validation
			if (doctor.id.longValue() != LoginController.getLoggedInUser()
					.getDoctor().id.longValue()) {
				Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
				Logger.warn("update attempted for doctor id: " + doctor.id);
				Logger.warn("logged in AppUser: "
						+ LoginController.getLoggedInUser().id);
				Logger.warn("logged in Doctor: "
						+ LoginController.getLoggedInUser().getDoctor().id);
				return redirect(routes.LoginController.processLogout());
			}

			if (requestMap.get("fullname") != null
					&& !(requestMap.get("fullname")[0].trim().isEmpty())) {
				doctor.appUser.name = requestMap.get("fullname")[0].trim();
			}

			if (requestMap.get("specialization") != null ) {	//&& !(requestMap.get("specialization").length > 0)
				final List<MasterSpecialization> oldSpezList = new ArrayList<MasterSpecialization>();
				oldSpezList.addAll(doctor.specializationList);
				doctor.specializationList.removeAll(oldSpezList);// .clear() wasnt working
				for (final String specializationId : requestMap.get("specialization")) {
					final MasterSpecialization spez = MasterSpecialization.find.byId(Long.parseLong(specializationId));
					doctor.specializationList.add(spez);
				}
			}

			if (requestMap.get("degree") != null
					&& !(requestMap.get("degree")[0].trim().isEmpty())) {
				doctor.degree = requestMap.get("degree")[0].trim();
			}

			if (requestMap.get("expYear") != null
					&& !(requestMap.get("expYear")[0].trim().isEmpty())) {
				doctor.experience = Integer
						.parseInt(requestMap.get("expYear")[0].trim());
			}

			if (requestMap.get("description") != null
					&& !(requestMap.get("description")[0].trim().isEmpty())) {
				doctor.description = requestMap.get("description")[0].trim();
			}

			if (requestMap.get("email") != null && !(requestMap.get("email")[0].trim().isEmpty())) {
				final String oldEmail = doctor.appUser.email;
				if (oldEmail.trim().compareToIgnoreCase(requestMap.get("email")[0].trim()) != 0) {
					if(AppUser.find.where().ieq("email", requestMap.get("email")[0].trim()).findRowCount()>0){
						flash().put("alert", new Alert("alert-danger", "Sorry! Another User with email id "+requestMap.get("email")[0].trim()+" already exists!").toString());
						return redirect(routes.UserActions.dashboard());
					}
					doctor.appUser.email = requestMap.get("email")[0].trim().toLowerCase();
					doctor.appUser.emailConfirmed = false;
				}
			}

			if (requestMap.get("mobileNumber") != null && !(requestMap.get("mobileNumber")[0].trim().isEmpty())) {
				final Long oldNumber = doctor.appUser.mobileNumber;
				final Long newNumber = Long.parseLong(requestMap.get("mobileNumber")[0].trim());
				if (oldNumber == null || (oldNumber.longValue() != newNumber.longValue())) {
					doctor.appUser.mobileNumber = Long.parseLong(requestMap.get("mobileNumber")[0].trim());
					doctor.appUser.mobileNumberConfirmed = false;
				}
			}

			if (requestMap.get("registrationNumber") != null
					&& !(requestMap.get("registrationNumber")[0].trim()
							.isEmpty())) {
				doctor.registrationNumber = requestMap
						.get("registrationNumber")[0].trim();
			}

			if (requestMap.get("slugUrl") != null
					&& !(requestMap.get("slugUrl")[0].trim().isEmpty())) {
				final String newSlug = requestMap.get("slugUrl")[0].trim();
				if (!newSlug.matches("^[a-z0-9\\-]+$")) {
					flash().put(
							"alert",
							new Alert("alert-danger",
									"Invalid charactrer provided in Url.")
							.toString());
					return redirect(routes.UserActions.dashboard());
				}
				if (requestMap.get("slugUrl")[0].trim().compareToIgnoreCase(
						doctor.slugUrl) != 0) {
					final int availableSlug = Doctor.find.where()
							.eq("slugUrl", requestMap.get("slugUrl")[0].trim())
							.findRowCount();
					if (availableSlug == 0) {
						doctor.slugUrl = requestMap.get("slugUrl")[0].trim();
					} else {
						flash().put(
								"alert",
								new Alert("alert-danger",
										"Requested Url is not available.")
								.toString());
						return redirect(routes.UserActions.dashboard());
					}
				}
			}

			doctor.appUser.update();
			doctor.update();
			return redirect(routes.UserActions.dashboard());
		} catch (final NumberFormatException e) {
			Logger.error("ERROR WHILE UPDATING BASIC DOCTOR INFO");
			e.printStackTrace();
			flash().put(
					"alert",
					new Alert("alert-danger",
							"Sorry! Numbers could not be read. Please try again.")
					.toString());
			return redirect(routes.UserActions.dashboard());
		} catch (final Exception e) {
			Logger.error("ERROR WHILE UPDATING BASIC DOCTOR INFO");
			e.printStackTrace();
			flash().put(
					"alert",
					new Alert("alert-danger",
							"Sorry! Something went wrong. Please try again.")
					.toString());
			return redirect(routes.UserActions.dashboard());
		}
	}

	/**
	 * Action to update profile and background images of Doctor POST
	 * /secure-doctor/doctor-images-update
	 */
	public static Result imagesUpdate() {
		final MultipartFormData formData = request().body()
				.asMultipartFormData();
		final Doctor doctor = Doctor.find.byId(Long.parseLong(formData
				.asFormUrlEncoded().get("doctorId")[0]));
		// Server side validation
		if (doctor.id.longValue() != LoginController.getLoggedInUser()
				.getDoctor().id.longValue()) {
			Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
			Logger.warn("update attempted for doctor id: " + doctor.id);
			Logger.warn("logged in AppUser: "
					+ LoginController.getLoggedInUser().id);
			Logger.warn("logged in Doctor: "
					+ LoginController.getLoggedInUser().getDoctor().id);
			return redirect(routes.LoginController.processLogout());
		}
		try {
			if (formData.getFile("profileimage") != null) {
				final File profileImageFile = formData.getFile("profileimage")
						.getFile();
				doctor.profileImage = Files.toByteArray(profileImageFile);
			}
			if (formData.getFile("backgroundimage") != null) {
				final File backgroundImageFile = formData.getFile(
						"backgroundimage").getFile();
				doctor.backgroundImage = Files.toByteArray(backgroundImageFile);
			}
			doctor.update();
		} catch (final Exception e) {
			Logger.error("ERROR WHILE UPDATING IMAGES OF DOCTOR");
			e.printStackTrace();
		}
		return redirect(routes.UserActions.dashboard());
	}

	/**
	 * Action to create or update DoctorExperience entity for the loggedIn
	 * Doctor POST /secure-doctor/add-work-experience
	 */
	public static Result addWorkExperience() {

		final Map<String, String[]> requestMap = request().body()
				.asFormUrlEncoded();
		final Doctor doctor = Doctor.find.byId(Long.parseLong(requestMap
				.get("doctorId")[0]));

		// Server side validation
		if (doctor.id.longValue() != LoginController.getLoggedInUser()
				.getDoctor().id.longValue()) {
			Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
			Logger.warn("update attempted for doctor id: " + doctor.id);
			Logger.warn("logged in AppUser: "
					+ LoginController.getLoggedInUser().id);
			Logger.warn("logged in Doctor: "
					+ LoginController.getLoggedInUser().getDoctor().id);
			return redirect(routes.LoginController.processLogout());
		}

		try {
			final DoctorExperience experience = new DoctorExperience();
			experience.institutionName = requestMap.get("name")[0];
			experience.workedFrom = Integer.parseInt(requestMap.get("from")[0]);

			if (requestMap.get("position")[0] != null
					&& requestMap.get("position")[0].compareToIgnoreCase("") != 0) {
				experience.position = requestMap.get("position")[0];
			}

			if (requestMap.get("to")[0] != null
					&& requestMap.get("to")[0].compareToIgnoreCase("") != 0) {
				experience.workedTo = Integer.parseInt(requestMap.get("to")[0]);
			}

			if (requestMap.get("to")[0] != null
					&& requestMap.get("to")[0].compareToIgnoreCase("") == 0) {
				experience.workedTo = 0;
			}

			if (requestMap.get("description")[0] != null
					&& requestMap.get("description")[0].compareToIgnoreCase("") != 0) {
				experience.description = requestMap.get("description")[0];
			}
			if (requestMap.get("doctorExperienceId") != null
					&& requestMap.get("doctorExperienceId")[0]
							.compareToIgnoreCase("") != 0) {
				experience.id = Long.parseLong(requestMap
						.get("doctorExperienceId")[0]);
				experience.update();
			} else {
				doctor.doctorExperienceList.add(experience);
				doctor.update();
			}
			return redirect(routes.UserActions.dashboard());
		} catch (final NumberFormatException e) {
			Logger.error("NumberFormatException WHILE PARSING DOCTOR'S WORK EXPERIENCE");
			e.printStackTrace();
			flash().put(
					"alert",
					new Alert("alert-danger", "Year field(s) invalid.")
					.toString());
			return redirect(routes.UserActions.dashboard());
		} catch (final Exception e) {
			Logger.error("ERROR WHILE SAVING DOCTOR'S WORK EXPERIENCE");
			e.printStackTrace();
			flash().put(
					"alert",
					new Alert("alert-danger",
							"Sorry! Something went wrong. Please try again.")
					.toString());
			return redirect(routes.UserActions.dashboard());
		}
	}

	/**
	 * Action to remove a DoctorExperience entity for the loggedIn Doctor GET
	 * /secure-doctor/remove-work-experience/:docId/:id
	 */
	public static Result removeWorkExperience(final Long docId, final Long id) {
		final Doctor doctor = Doctor.find.byId(docId);

		// Server side validation
		if (doctor.id.longValue() != LoginController.getLoggedInUser()
				.getDoctor().id.longValue()) {
			Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
			Logger.warn("update attempted for doctor id: " + doctor.id);
			Logger.warn("logged in AppUser: "
					+ LoginController.getLoggedInUser().id);
			Logger.warn("logged in Doctor: "
					+ LoginController.getLoggedInUser().getDoctor().id);
			return redirect(routes.LoginController.processLogout());
		}

		final DoctorExperience experience = DoctorExperience.find.byId(id);
		doctor.doctorExperienceList.remove(experience);
		experience.delete();
		doctor.update();
		return redirect(routes.UserActions.dashboard());
	}

	/**
	 * Action to create / edit an Award entity of the loggedIn Doctor POST
	 * /secure-doctor/add-award
	 */
	public static Result addAward() {
		final Map<String, String[]> requestMap = request().body()
				.asFormUrlEncoded();
		final Doctor doctor = Doctor.find.byId(Long.parseLong(requestMap
				.get("doctorId")[0]));

		// Server side validation
		if (doctor.id.longValue() != LoginController.getLoggedInUser()
				.getDoctor().id.longValue()) {
			Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
			Logger.warn("update attempted for doctor id: " + doctor.id);
			Logger.warn("logged in AppUser: "
					+ LoginController.getLoggedInUser().id);
			Logger.warn("logged in Doctor: "
					+ LoginController.getLoggedInUser().getDoctor().id);
			return redirect(routes.LoginController.processLogout());
		}

		try {
			final DoctorAward award = new DoctorAward();

			if (requestMap.get("name")[0] != null
					&& requestMap.get("name")[0].compareToIgnoreCase("") != 0) {
				award.awardName = requestMap.get("name")[0];
			}

			if (requestMap.get("by")[0] != null
					&& requestMap.get("by")[0].compareToIgnoreCase("") != 0) {
				award.awardedBy = requestMap.get("by")[0];
			}

			if (requestMap.get("year")[0] != null
					&& requestMap.get("year")[0].compareToIgnoreCase("") != 0) {
				award.year = requestMap.get("year")[0];
			}

			if (requestMap.get("description")[0] != null
					&& requestMap.get("description")[0].compareToIgnoreCase("") != 0) {
				award.description = requestMap.get("description")[0];
			}

			if (requestMap.get("doctorAwardId") != null
					&& requestMap.get("doctorAwardId")[0]
							.compareToIgnoreCase("") != 0) {
				award.id = Long.parseLong(requestMap.get("doctorAwardId")[0]);
				award.update();
			} else {
				doctor.doctorAwardList.add(award);
				doctor.update();
			}
			return redirect(routes.UserActions.dashboard());
		} catch (final Exception e) {
			Logger.error("ERROR WHILE SAVING DOCTOR'S AWARD");
			e.printStackTrace();
			flash().put(
					"alert",
					new Alert("alert-danger",
							"Sorry! Something went wrong. Please try again.")
					.toString());
			return redirect(routes.UserActions.dashboard());
		}
	}

	/**
	 * Action to remove a Award entity from the loggedIn Doctor GET
	 * /secure-doctor/remove-award/:docId/:id
	 */
	public static Result removeAward(final Long docId, final Long id) {
		final Doctor doctor = Doctor.find.byId(docId);
		// Server side validation
		if (doctor.id.longValue() != LoginController.getLoggedInUser()
				.getDoctor().id.longValue()) {
			Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
			Logger.warn("update attempted for doctor id: " + doctor.id);
			Logger.warn("logged in AppUser: "
					+ LoginController.getLoggedInUser().id);
			Logger.warn("logged in Doctor: "
					+ LoginController.getLoggedInUser().getDoctor().id);
			return redirect(routes.LoginController.processLogout());
		}
		final DoctorAward award = DoctorAward.find.byId(id);
		doctor.doctorAwardList.remove(award);
		award.delete();
		doctor.update();
		return redirect(routes.UserActions.dashboard());
	}

	/**
	 * Action to create new/update DoctorEducation entity for the loggedIn
	 * Doctor POST /secure-doctor/add-education
	 */
	public static Result addEducation() {
		final Map<String, String[]> requestMap = request().body()
				.asFormUrlEncoded();
		final Doctor doctor = Doctor.find.byId(Long.parseLong(requestMap
				.get("doctorId")[0]));
		// Server side validation
		if (doctor.id.longValue() != LoginController.getLoggedInUser()
				.getDoctor().id.longValue()) {
			Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
			Logger.warn("update attempted for doctor id: " + doctor.id);
			Logger.warn("logged in AppUser: "
					+ LoginController.getLoggedInUser().id);
			Logger.warn("logged in Doctor: "
					+ LoginController.getLoggedInUser().getDoctor().id);
			return redirect(routes.LoginController.processLogout());
		}
		try {
			final DoctorEducation education = new DoctorEducation();
			education.institutionName = requestMap.get("name")[0];
			education.fromYear = Integer.parseInt(requestMap.get("from")[0]);

			if (requestMap.get("degree")[0] != null
					&& requestMap.get("degree")[0].compareToIgnoreCase("") != 0) {
				education.degree = requestMap.get("degree")[0];
			}

			if (requestMap.get("to")[0] != null
					&& requestMap.get("to")[0].compareToIgnoreCase("") != 0) {
				education.toYear = Integer.parseInt(requestMap.get("to")[0]);
			}
			if (requestMap.get("to")[0] != null
					&& requestMap.get("to")[0].compareToIgnoreCase("") == 0) {
				education.toYear = 0;
			}
			if (requestMap.get("description")[0] != null
					&& requestMap.get("description")[0].compareToIgnoreCase("") != 0) {
				education.description = requestMap.get("description")[0];
			}

			if (requestMap.get("doctorEducationId") != null
					&& requestMap.get("doctorEducationId")[0]
							.compareToIgnoreCase("") != 0) {
				education.id = Long.parseLong(requestMap
						.get("doctorEducationId")[0]);
				education.update();
			} else {
				doctor.doctorEducationList.add(education);
				doctor.update();
			}
			return redirect(routes.UserActions.dashboard());
		} catch (final NumberFormatException e) {
			Logger.error("NumberFormatException WHILE PARSING DOCTOR'S EDUCATION");
			e.printStackTrace();
			flash().put(
					"alert",
					new Alert("alert-danger", "Year field(s) invalid.")
					.toString());
			return redirect(routes.UserActions.dashboard());
		} catch (final Exception e) {
			Logger.error("ERROR WHILE SAVING DOCTOR'S EDUCATION");
			e.printStackTrace();
			flash().put(
					"alert",
					new Alert("alert-danger",
							"Sorry! Something went wrong. Please try again.")
					.toString());
			return redirect(routes.UserActions.dashboard());
		}
	}

	/**
	 * Action to remove a DoctorEducation entity from the loggedIn Doctor GET
	 * /secure-doctor/remove-education/:docId/:id
	 */
	public static Result removeEducation(final Long docId, final Long id) {
		final Doctor doctor = Doctor.find.byId(docId);
		// Server side validation
		if (doctor.id.longValue() != LoginController.getLoggedInUser()
				.getDoctor().id.longValue()) {
			Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
			Logger.warn("update attempted for doctor id: " + doctor.id);
			Logger.warn("logged in AppUser: "
					+ LoginController.getLoggedInUser().id);
			Logger.warn("logged in Doctor: "
					+ LoginController.getLoggedInUser().getDoctor().id);
			return redirect(routes.LoginController.processLogout());
		}
		final DoctorEducation education = DoctorEducation.find.byId(id);
		doctor.doctorEducationList.remove(education);
		education.delete();
		doctor.update();
		return redirect(routes.UserActions.dashboard());
	}

	/**
	 * Action to create/update a SocialWork entity for the loggedIn Doctor POST
	 * /secure-doctor/add-social-work
	 */
	public static Result addSocialWork() {
		final Map<String, String[]> requestMap = request().body()
				.asFormUrlEncoded();
		final Doctor doctor = Doctor.find.byId(Long.parseLong(requestMap
				.get("doctorId")[0]));
		// Server side validation
		if (doctor.id.longValue() != LoginController.getLoggedInUser()
				.getDoctor().id.longValue()) {
			Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
			Logger.warn("update attempted for doctor id: " + doctor.id);
			Logger.warn("logged in AppUser: "
					+ LoginController.getLoggedInUser().id);
			Logger.warn("logged in Doctor: "
					+ LoginController.getLoggedInUser().getDoctor().id);
			return redirect(routes.LoginController.processLogout());
		}
		try {
			final DoctorSocialWork socialWork = new DoctorSocialWork();
			socialWork.title = requestMap.get("title")[0];
			socialWork.description = requestMap.get("description")[0];

			if (requestMap.get("doctorSocialId") != null
					&& requestMap.get("doctorSocialId")[0]
							.compareToIgnoreCase("") != 0) {
				socialWork.id = Long
						.parseLong(requestMap.get("doctorSocialId")[0]);
				socialWork.update();
			} else {
				doctor.getDoctorSocialWorkList().add(socialWork);
				doctor.update();
			}
			return redirect(routes.UserActions.dashboard());
		} catch (final Exception e) {
			Logger.error("ERROR WHILE SAVING DOCTOR'S SOCIAL WORK");
			e.printStackTrace();
			flash().put(
					"alert",
					new Alert("alert-danger",
							"Sorry! Something went wrong. Please try again.")
					.toString());
			return redirect(routes.UserActions.dashboard());
		}
	}

	/**
	 * Action to remove a SocialWork entity from the loggedIn Doctor GET
	 * /secure-doctor/remove-social-work/:docId/:id
	 */
	public static Result removeSocialWork(final Long docId, final Long id) {
		final Doctor doctor = Doctor.find.byId(docId);
		// Server side validation
		if (doctor.id.longValue() != LoginController.getLoggedInUser()
				.getDoctor().id.longValue()) {
			Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
			Logger.warn("update attempted for doctor id: " + doctor.id);
			Logger.warn("logged in AppUser: "
					+ LoginController.getLoggedInUser().id);
			Logger.warn("logged in Doctor: "
					+ LoginController.getLoggedInUser().getDoctor().id);
			return redirect(routes.LoginController.processLogout());
		}

		final DoctorSocialWork socialWork = DoctorSocialWork.find.byId(id);
		doctor.doctorSocialWorkList.remove(socialWork);
		socialWork.delete();
		doctor.update();
		return redirect(routes.UserActions.dashboard());
	}

	/**
	 * @author Mitesh Action to render a page with form for adding new clinic of
	 *         the loggedInDoctor GET /secure-doctor/new-clinic
	 */
	@ConfirmAppUser
	public static Result newClinic() {
		return ok(views.html.doctor.newClinic.render(clinicForm));
	}

	/**
	 * @author Mitesh Action to process adding new clinic of the loggedInDoctor
	 *         by creating a clinicInfo object and then calls
	 *         DoctorController.createAppointment(clinicInfo) method to create
	 *         requisite appointments POST /secure-doctor/new-clinic
	 */
	@ConfirmAppUser
	public static Result processNewClinic() {
		final Form<DoctorClinicInfoBean> filledForm = clinicForm
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			return ok(views.html.doctor.newClinic.render(clinicForm));
		} else {
			final DoctorClinicInfo clinicInfo = filledForm.get()
					.toDoctorClinicInfo();
			clinicInfo.doctor = LoginController.getLoggedInUser().getDoctor();
			clinicInfo.save();
			return DoctorController.createAppointment(clinicInfo);
		}
	}

	/**
	 * @author Mitesh Action to process adding new clinic of the loggedInDoctor
	 *         by creating a clinicInfo object and then calls
	 *         DoctorController.createAppointment(clinicInfo) method to create
	 *         requisite appointments NO ROUTE - called internally from
	 *         DoctorController.processNewClinic() and from
	 *         DoctorController.reCreateAppointment(clinicInfo)
	 */
	@ConfirmAppUser
	private static Result createAppointment(final DoctorClinicInfo docClinicInfo) {
		// Server side validation
		if (docClinicInfo.doctor.id.longValue() != LoginController
				.getLoggedInUser().getDoctor().id.longValue()) {
			Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
			Logger.warn("update attempted for doctor id: "
					+ docClinicInfo.doctor.id);
			Logger.warn("logged in AppUser: "
					+ LoginController.getLoggedInUser().id);
			Logger.warn("logged in Doctor: "
					+ LoginController.getLoggedInUser().getDoctor().id);
			return redirect(routes.LoginController.processLogout());
		}
		try {
			final Calendar calendar1 = Calendar.getInstance();
			final Calendar calendar2 = Calendar.getInstance();
			final SimpleDateFormat dateFormat = new SimpleDateFormat("kk:mm");
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			for (int date = 0; date < 90; date++) {
				for (final DaySchedule schedule : docClinicInfo.scheduleDays) {
					if (schedule.day == Day.getDay(calendar
							.get(Calendar.DAY_OF_WEEK) - 1)) {
						try {
							calendar1
							.setTime(dateFormat.parse(schedule.toTime));
							calendar2.setTime(dateFormat
									.parse(schedule.fromTime));
						} catch (final Exception e) {
							e.printStackTrace();
						}
						final int hoursToClinic = calendar1
								.get(Calendar.HOUR_OF_DAY)
								- calendar2.get(Calendar.HOUR_OF_DAY);
						final int minutsToClinic = calendar1
								.get(Calendar.MINUTE)
								- calendar2.get(Calendar.MINUTE);
						calendar.set(Calendar.HOUR_OF_DAY,
								calendar2.get(Calendar.HOUR_OF_DAY));
						calendar.set(Calendar.MINUTE,
								calendar2.get(Calendar.MINUTE));
						calendar.set(Calendar.SECOND, 0);
						calendar.set(Calendar.MILLISECOND, 0);
						if (schedule.requester.equals(Role.PATIENT)) {
							for (int j2 = 0; j2 < (((hoursToClinic * 60) + minutsToClinic) / docClinicInfo.slot); j2++) {
								if (Appointment.find
										.where()
										.eq("doctorClinicInfo", docClinicInfo)
										.eq("appointmentTime",
												calendar.getTime())
												.findUnique() == null) {
									final Appointment appointment = new Appointment();
									appointment.appointmentStatus = AppointmentStatus.AVAILABLE;
									appointment.appointmentTime = calendar
											.getTime();
									appointment.doctorClinicInfo = docClinicInfo;
									appointment.save();
									calendar.add(Calendar.MINUTE,
											docClinicInfo.slot);
								} else {
									calendar.add(Calendar.MINUTE,
											docClinicInfo.slot);
								}
							}
						} else {
							for (int j2 = 0; j2 < (((hoursToClinic * 60) + minutsToClinic) / docClinicInfo.slotMR); j2++) {
								if (Appointment.find
										.where()
										.eq("doctorClinicInfo", docClinicInfo)
										.eq("appointmentTime",
												calendar.getTime())
												.findUnique() == null) {
									Logger.info("  " + calendar.getTime());
									final Appointment appointment = new Appointment();
									appointment.appointmentStatus = AppointmentStatus.AVAILABLE;
									appointment.appointmentTime = calendar
											.getTime();
									appointment.doctorClinicInfo = docClinicInfo;
									appointment.save();
									calendar.add(Calendar.MINUTE,
											docClinicInfo.slotMR);
								} else {
									calendar.add(Calendar.MINUTE,
											docClinicInfo.slotMR);
								}
							}
						}

					}

				}
				calendar.add(Calendar.DATE, 1);
			}
			flash().put(
					"alert",
					new Alert("alert-success", docClinicInfo.clinic.name
							+ " created successfully.").toString());
			return redirect(routes.DoctorController.myClinics());
		} catch (final Exception e) {
			Logger.error("ERROR WHILE CREATING APPOINTMENTS.");
			e.printStackTrace();
			flash().put(
					"alert",
					new Alert("alert-danger",
							"Sorry. Something went wrong. Please try again.")
					.toString());
			return redirect(routes.DoctorController.myClinics());
		}
	}

	/**
	 * @author Mitesh Action to show all active clinics of the loggedIn Doctor
	 *         GET /secure-doctor/clinics
	 */
	@ConfirmAppUser
	public static Result myClinics() {
		final Doctor loggedInDoctor = LoginController.getLoggedInUser()
				.getDoctor();
		return ok(views.html.doctor.myClinics.render(loggedInDoctor
				.getActiveClinic()));
	}

	/**
	 * @author Mitesh Action to show form to edit one of loggedIn doctor's
	 *         clinic GET /secure-doctor/edit-clinic/:id Depricated on 18th July 2014.
	 *         Use DoctorController.editClinicInfo(Long docClinicId) and
	 *         DoctorController.editClinicSchedule(Long docClinicId) instead.
	 */
	@ConfirmAppUser
	@Deprecated
	public static Result manageClinic(final Long docClinicId) {
		final DoctorClinicInfo doctorClinicInfo = DoctorClinicInfo.find
				.byId(docClinicId);
		// Server side validation
		if (doctorClinicInfo.doctor.id.longValue() != LoginController
				.getLoggedInUser().getDoctor().id.longValue()) {
			Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
			Logger.warn("update attempted for doctor id: "
					+ doctorClinicInfo.doctor.id);
			Logger.warn("logged in AppUser: "
					+ LoginController.getLoggedInUser().id);
			Logger.warn("logged in Doctor: "
					+ LoginController.getLoggedInUser().getDoctor().id);
			return redirect(routes.LoginController.processLogout());
		}
		final DoctorClinicInfoBean bean = doctorClinicInfo.toBean();
		final Form<DoctorClinicInfoBean> filledForm = clinicForm
				.fill(doctorClinicInfo.toBean());
		for (final String from : bean.fromHrs) {
			Logger.warn(from);
		}
		return ok(views.html.doctor.editClinic.render(filledForm,
				bean.daysOfWeek, bean.daysOfWeekMr));
	}

	/**
	 * @author Mitesh Action to update one of loggedInDoctor's clinics
	 *         (non-schedule) information like name, address etc. POST
	 *         /secure-doctor/update-clinic
	 */
	@ConfirmAppUser
	public static Result processUpdateClinicInfo() {
		final Form<DoctorClinicInfoBean> filledForm = clinicForm
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			return ok(views.html.doctor.editClinicInfo.render(filledForm));

		} else {
			final DoctorClinicInfo clinicInfo = filledForm.get()
					.toDoctorClinicInfo();
			// Server side validation
			if (clinicInfo.doctor.id.longValue() != LoginController
					.getLoggedInUser().getDoctor().id.longValue()) {
				Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
				Logger.warn("update attempted for doctor id: "
						+ clinicInfo.doctor.id);
				Logger.warn("logged in AppUser: "
						+ LoginController.getLoggedInUser().id);
				Logger.warn("logged in Doctor: "
						+ LoginController.getLoggedInUser().getDoctor().id);
				return redirect(routes.LoginController.processLogout());
			}
			final DoctorClinicInfo clinicInfoPrevious = DoctorClinicInfo.find
					.byId(clinicInfo.id);
			clinicInfoPrevious.clinic.name = clinicInfo.clinic.name;
			clinicInfoPrevious.clinic.contactNo = clinicInfo.clinic.contactNo;
			clinicInfoPrevious.clinic.contactPersonName = clinicInfo.clinic.contactPersonName;
			clinicInfoPrevious.clinic.address.addressLine1 = clinicInfo.clinic.address.addressLine1;
			clinicInfoPrevious.clinic.address.area = clinicInfo.clinic.address.area;
			clinicInfoPrevious.clinic.address.state = clinicInfo.clinic.address.state;
			clinicInfoPrevious.clinic.address.city = clinicInfo.clinic.address.city;
			clinicInfoPrevious.clinic.address.latitude = clinicInfo.clinic.address.latitude;
			clinicInfoPrevious.clinic.address.longitude = clinicInfo.clinic.address.longitude;
			clinicInfoPrevious.clinic.address.update();
			clinicInfoPrevious.update();
			flash().put(
					"alert",
					new Alert("alert-success", "Successfully Updated")
					.toString());
			return redirect(routes.DoctorController.myClinics());
		}
	}

	/**
	 * @author Mitesh Action to update one of loggedInDoctor's clinics
	 *         appointments/schedule information POST
	 *         /secure-doctor/update-clinic-schedule
	 */
	@ConfirmAppUser
	public static Result processUpdateClinicSchedule() {

		final Form<DoctorClinicInfoBean> filledForm = clinicForm
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			return redirect(routes.DoctorController.myClinics());
		} else {
			final DoctorClinicInfo clinicInfo = filledForm.get()
					.toDoctorClinicInfo();
			// server-side check
			if (clinicInfo.doctor.id.longValue() != LoginController
					.getLoggedInUser().getDoctor().id.longValue()) {
				Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
				Logger.warn("update attempted for doctor id: "
						+ clinicInfo.doctor.id);
				Logger.warn("logged in AppUser: "
						+ LoginController.getLoggedInUser().id);
				Logger.warn("logged in Doctor: "
						+ LoginController.getLoggedInUser().getDoctor().id);
				return redirect(routes.LoginController.processLogout());
			}
			final DoctorClinicInfo clinicInfoPrevious = DoctorClinicInfo.find
					.byId(clinicInfo.id);
			for (final DaySchedule sc4 : clinicInfo.scheduleDays) {
				Logger.info("test day" + sc4.day);
			}
			Ebean.delete(clinicInfoPrevious.scheduleDays);
			clinicInfoPrevious.scheduleDays = clinicInfo.scheduleDays;
			clinicInfoPrevious.slot = clinicInfo.slot;
			clinicInfoPrevious.slotMR = clinicInfo.slotMR;
			clinicInfoPrevious.update();
			flash().put(
					"alert",
					new Alert("alert-success", "Successfully Updated")
					.toString());
			return DoctorController.reCreateAppointment(clinicInfoPrevious);
		}
	}

	/**
	 * @author Mitesh Action to update one of loggedIn doctor's clinic's
	 *         schedule/appointments This action will delete non-booked
	 *         appointments and will then call
	 *         DoctorController.createAppointment(clinicInfo) to create new
	 *         appointments with the changed timings. NO ROUTE - called
	 *         internally from DoctorController.processUpdateClinicSchedule()
	 */
	@ConfirmAppUser
	private static Result reCreateAppointment(final DoctorClinicInfo clinicInfo) {
		Logger.debug("entered");
		final List<Appointment> appointments = Appointment.find.where()
				.eq("doctorClinicInfo", clinicInfo)
				.eq("appointmentStatus", AppointmentStatus.AVAILABLE)
				.findList();
		Ebean.delete(appointments);
		return DoctorController.createAppointment(clinicInfo);
	}

	/**
	 * @author Mitesh Action to delete (make inActive) one of loggedInDoctor's
	 *         clinics GET /secure-doctor/delete-clinic/:id
	 */
	@ConfirmAppUser
	public static Result deleteClinic(final Long id) {
		final DoctorClinicInfo clinicInfo = DoctorClinicInfo.find.byId(id);
		// Server side validation
		if (clinicInfo.doctor.id.longValue() != LoginController
				.getLoggedInUser().getDoctor().id.longValue()) {
			Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
			Logger.warn("update attempted for doctor id: "
					+ clinicInfo.doctor.id);
			Logger.warn("logged in AppUser: "
					+ LoginController.getLoggedInUser().id);
			Logger.warn("logged in Doctor: "
					+ LoginController.getLoggedInUser().getDoctor().id);
			return redirect(routes.LoginController.processLogout());
		}
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 00);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		calendar.set(Calendar.MILLISECOND, 00);

		final List<Appointment> futureApptList = Appointment.find.where()
				.eq("doctorClinicInfo", clinicInfo)
				.ge("appointmentTime", calendar.getTime())
				.ne("appointmentStatus", AppointmentStatus.SERVED).findList();

		Ebean.delete(futureApptList);

		clinicInfo.active = false;
		clinicInfo.doctor.update();
		clinicInfo.update();
		flash().put("alert",
				new Alert("alert-success", "Successfully Deleted").toString());
		return redirect(routes.DoctorController.myClinics());
	}

	/**
	 * @author Mitesh Action to show form to edit one of loggedIn doctor's
	 *         clinic information GET /secure-doctor/edit-clinic-info/:id
	 */
	@ConfirmAppUser
	public static Result editClinicInfo(final Long docClinicId) {

		final DoctorClinicInfo doctorClinicInfo = DoctorClinicInfo.find
				.byId(docClinicId);
		// server-side check
		if (doctorClinicInfo.doctor.id.longValue() != LoginController
				.getLoggedInUser().getDoctor().id.longValue()) {
			return redirect(routes.LoginController.processLogout());
		}
		final Form<DoctorClinicInfoBean> filledForm = clinicForm
				.fill(doctorClinicInfo.toBean());
		return ok(views.html.doctor.editClinicInfo.render(filledForm));

	}

	/**
	 * @author Mitesh Action to show form to edit one of loggedIn doctor's
	 *         clinic schedule GET /secure-doctor/edit-clinic-schedule/:id
	 */
	@ConfirmAppUser
	public static Result editClinicSchedule(final Long docClinicId) {
		final DoctorClinicInfo doctorClinicInfo = DoctorClinicInfo.find
				.byId(docClinicId);
		// server-side check
		if (doctorClinicInfo.doctor.id.longValue() != LoginController
				.getLoggedInUser().getDoctor().id.longValue()) {
			return redirect(routes.LoginController.processLogout());
		}
		final DoctorClinicInfoBean bean = doctorClinicInfo.toBean();
		final Form<DoctorClinicInfoBean> filledForm = clinicForm
				.fill(doctorClinicInfo.toBean());
		return ok(views.html.doctor.editClinicSchedule.render(filledForm,
				bean.daysOfWeek, bean.daysOfWeekMr));

	}

	/**
	 * @author Mitesh Action to Display appointment requested to logged-in
	 *         DOCTOR GET /secure-doctor/all-appointments
	 */
	@ConfirmAppUser
	public static Result viewAllAppointments() {

		/*
		 * final Calendar calendar=Calendar.getInstance(); calendar.setTime(new
		 * Date()); calendar.set(Calendar.HOUR_OF_DAY, 0);
		 * calendar.set(Calendar.MINUTE, 0); calendar.set(Calendar.SECOND,0);
		 * calendar.set(Calendar.MILLISECOND,0);
		 */
		final Doctor loggedIndoctor = LoginController.getLoggedInUser()
				.getDoctor();
		final List<DoctorClinicInfo> docclinicInfo = DoctorClinicInfo.find
				.where().eq("doctor", loggedIndoctor).findList();
		// final List<Appointment> appointments =
		// Appointment.find.where().in("doctorClinicInfo",
		// docclinicInfo).eq("appointmentStatus",
		// AppointmentStatus.APPROVED).ge("appointmentTime",
		// calendar.getTime()).findList();
		final List<AppointmentStatus> statusList = new ArrayList<AppointmentStatus>();
		statusList.add(AppointmentStatus.APPROVED);
		statusList.add(AppointmentStatus.SERVED);
		final List<Appointment> appointments = Appointment.find.where()
				.in("doctorClinicInfo", docclinicInfo)
				.in("appointmentStatus", statusList).orderBy("appointmentTime")
				.findList();
		return ok(views.html.doctor.doctorAllAppointments.render(appointments,
				docclinicInfo));

	}

	/**
	 * @author Mitesh Action to Display appointment requested to logged-in
	 *         DOCTOR GET /secure-doctor/todays-appointments
	 */
	@ConfirmAppUser
	public static Result viewTodaysAppointments() {
		final Date now = new Date();
		final Calendar calendarFrom = Calendar.getInstance();
		calendarFrom.setTime(now);
		calendarFrom.set(Calendar.HOUR_OF_DAY, 0);
		calendarFrom.set(Calendar.MINUTE, 0);
		calendarFrom.set(Calendar.SECOND, 0);
		calendarFrom.set(Calendar.MILLISECOND, 0);

		final Calendar calendarTo = Calendar.getInstance();
		calendarTo.setTime(now);
		calendarTo.set(Calendar.HOUR_OF_DAY, 23);
		calendarTo.set(Calendar.MINUTE, 59);
		calendarTo.set(Calendar.SECOND, 59);
		calendarTo.set(Calendar.MILLISECOND, 999);

		final Doctor loggedIndoctor = LoginController.getLoggedInUser()
				.getDoctor();
		final List<DoctorClinicInfo> docclinicInfo = DoctorClinicInfo.find
				.where().eq("doctor", loggedIndoctor).findList();
		final List<AppointmentStatus> statusList = new ArrayList<AppointmentStatus>();
		statusList.add(AppointmentStatus.APPROVED);
		statusList.add(AppointmentStatus.SERVED);
		final List<Appointment> appointments = Appointment.find.where()
				.in("doctorClinicInfo", docclinicInfo)
				.in("appointmentStatus", statusList)
				.ge("appointmentTime", calendarFrom.getTime())
				.le("appointmentTime", calendarTo.getTime())
				.orderBy("appointmentTime").findList();
		return ok(views.html.doctor.doctorTodaysAppointments.render(
				appointments, docclinicInfo));

	}

	/**
	 * Action to render list of Sig Codes of the loggedInDoctor GET
	 * /secure-doctor/sig-codes
	 */
	@ConfirmAppUser
	public static Result showSigCodes() {
		final Doctor doctor = LoginController.getLoggedInUser().getDoctor();
		return ok(views.html.doctor.sigCodes.render(doctor.sigCodeList));
	}

	/**
	 * Action to save a sig-code to the loggedInDoctor's sigcode List POST
	 * /secure-doctor/add-sig-code
	 */
	@ConfirmAppUser
	public static Result addSigCode() {
		final Doctor doctor = LoginController.getLoggedInUser().getDoctor();
		final Map<String, String[]> requestMap = request().body()
				.asFormUrlEncoded();
		// server-side check
		if (Long.parseLong(requestMap.get("doctorId")[0]) != doctor.id
				.longValue()) {
			return redirect(routes.LoginController.processLogout());
		}
		if (requestMap.get("sigCode") != null
				&& requestMap.get("description") != null
				&& !requestMap.get("sigCode")[0].trim().isEmpty()
				&& !requestMap.get("description")[0].trim().isEmpty()) {
			final SigCode sigCode = new SigCode();
			sigCode.code = requestMap.get("sigCode")[0].trim();
			sigCode.description = requestMap.get("description")[0].trim();
			doctor.sigCodeList.add(sigCode);
			doctor.update();
		}
		return redirect(routes.DoctorController.showSigCodes());
	}

	/**
	 * Action to render the prescription form to the loggedInDoctor GET
	 * /secure-doctor/prescription/:appointmentId
	 */
	@ConfirmAppUser
	public static Result showPrescriptionForm(final Long appointmentId) {
		final Appointment appointment = Appointment.find.byId(appointmentId);
		// server-side check
		if (appointment.doctorClinicInfo.doctor.id.longValue() != LoginController
				.getLoggedInUser().getDoctor().id.longValue()) {
			return redirect(routes.LoginController.processLogout());
		}
		return ok(views.html.doctor.doctorPrescription.render(prescriptionForm,
				appointment));
	}

	/**
	 * Action to save prescription of the loggedInDoctor POST
	 * /secure-doctor/save-prescription
	 */
	@ConfirmAppUser
	public static Result savePrescription() {
		final Form<PrescriptionBean> filledForm = prescriptionForm
				.bindFromRequest();
		final PrescriptionBean bean = filledForm.get();
		final Doctor doctor = LoginController.getLoggedInUser().getDoctor();
		// server-side check
		if (bean.doctorId.longValue() != doctor.id.longValue()) {
			return redirect(routes.LoginController.processLogout());
		}

		Prescription prescription = new Prescription();
		prescription = bean.toEntity();
		prescription.save();

		final Appointment appointment = prescription.appointment;
		appointment.appointmentStatus = AppointmentStatus.SERVED;
		appointment.update();

		final List<MedicineLineItem> medLineItemList = prescription.medicineLineItemList;
		final List<DoctorProduct> doctorProductList = new ArrayList<DoctorProduct>();
		for (final MedicineLineItem medLineItem : medLineItemList) {
			if (DoctorProduct.find.where()
					.ieq("fullName", medLineItem.medicineFullName.trim())
					.findRowCount() == 0) {
				final DoctorProduct doctorProduct = new DoctorProduct();
				doctorProduct.fullName = medLineItem.medicineFullName.trim();
				doctorProductList.add(doctorProduct);
			}
		}

		if (doctorProductList.size() > 0) {
			doctor.myProductList.addAll(doctorProductList);
			doctor.update();
		}

		final List<DiagnosticTestLineItem> diagnosticTestLineItemList = prescription.diagnosticTestLineItemList;
		final List<DoctorDiagnosticTest> doctorDiagnosticTestList = new ArrayList<DoctorDiagnosticTest>();
		for (final DiagnosticTestLineItem diagLineItem : diagnosticTestLineItemList) {
			if (DoctorDiagnosticTest.find.where()
					.ieq("name", diagLineItem.fullNameOfDiagnosticTest.trim())
					.findRowCount() == 0) {
				final DoctorDiagnosticTest doctorDiagnosticTest = new DoctorDiagnosticTest();
				doctorDiagnosticTest.name = diagLineItem.fullNameOfDiagnosticTest
						.trim();
				doctorDiagnosticTestList.add(doctorDiagnosticTest);
			}
		}

		if (doctorDiagnosticTestList.size() > 0) {
			doctor.myDiagnosticTestList.addAll(doctorDiagnosticTestList);
			doctor.update();
		}

		final AppUser patient = prescription.patient.appUser;
		// Async Execution
		Promise.promise(new Function0<Integer>() {
			//@Override
			@Override
			public Integer apply() {
				int result = 0;
				if(!EmailService.sendPrescriptionSaveMessage(patient, doctor.appUser)){
					result=1;
				}

				return result;
			}
		});
		// End of async
		SMSService.sendSMS(prescription.patient.appUser.mobileNumber.toString(), "Your priscription by Dr. "
				+doctor.appUser.name+" has been saved.");
		flash().put("alert",
				new Alert("alert-success", "Prescription saved!").toString());
		return redirect(routes.DoctorController
				.showPrescription(prescription.id));
	}

	/**
	 * Action to show logged In doctor a page to assign a prescription to a
	 * pharmacy / diagnostic centre GET /secure-doctor/show-prescription
	 */
	@ConfirmAppUser
	public static Result showPrescription(final Long prescriptionId) {
		final Doctor doctor = LoginController.getLoggedInUser().getDoctor();
		final Prescription prescription = Prescription.find
				.byId(prescriptionId);
		// server-side check
		if (prescription.doctor.id.longValue() != doctor.id.longValue()) {
			return redirect(routes.LoginController.processLogout());
		}
		return ok(views.html.doctor.doctorSharePrescription
				.render(prescription));
	}

	/**
	 * Action to show logged In doctor a page to assign a prescription to a
	 * pharmacy / diagnostic centre GET /secure-doctor/share-prescription
	 */
	@ConfirmAppUser
	public static Result sharePrescription(final Long prId,
			final String pharmacyId, final String diagnosticId,
			final Boolean consent) {
		final Doctor doctor = LoginController.getLoggedInUser().getDoctor();
		final Prescription prescription = Prescription.find.byId(prId);
		// server-side check
		if (prescription.doctor.id.longValue() != doctor.id.longValue()) {
			return redirect(routes.LoginController.processLogout());
		}

		if (!consent) {
			flash().put(
					"alert",
					new Alert("alert-info", "Patient's Consent is Mandatory!")
					.toString());
			return redirect(routes.DoctorController.showPrescription(prId));
		}

		final StringBuilder sharedWith = new StringBuilder();

		if (pharmacyId != null && !pharmacyId.trim().isEmpty()) {
			final Pharmacy pharmacy = Pharmacy.find.byId(Long
					.parseLong(pharmacyId));
			final PharmacyPrescriptionInfo ppInfo = PharmacyPrescriptionInfo.find
					.where().eq("pharmacy", pharmacy)
					.eq("prescription", prescription).findUnique();
			if (ppInfo == null) {
				final PharmacyPrescriptionInfo phprInfo = new PharmacyPrescriptionInfo();
				phprInfo.pharmacy = pharmacy;
				phprInfo.prescription = prescription;
				phprInfo.sharedBy = doctor.appUser;
				phprInfo.sharedDate = new Date();
				phprInfo.pharmacyPrescriptionStatus = PharmacyPrescriptionStatus.RECEIVED;
				phprInfo.patientsConsent = consent;
				phprInfo.save();
				// send sms and email to pharmacy
				final StringBuilder message = new StringBuilder();
				message.append("<html><body>");
				message.append("<p>Dear "+pharmacy.adminPharmacist.appUser.name+",<br><br>Prescription of Dr."+doctor.appUser.name+" for patient "+prescription.patient.appUser.name+" has been shared with you.");
				message.append("<br><br>Best regards,<br>MedNetwork.in</p>");
				message.append("</body></html>");
				// Async Execution
				Promise.promise(new Function0<Integer>() {
					//@Override
					@Override
					public Integer apply() {
						int result = 0;
						if(!EmailService.sendSimpleHtmlEMail(pharmacy.adminPharmacist.appUser.email, "Prescription Shared", message.toString())){
							result=1;
						}

						return result;
					}
				});
				// End of async

				final StringBuilder smsToPharmacy = new StringBuilder();
				smsToPharmacy.append("Prescription of Dr."+doctor.appUser.name+" for patient has been shared with you.");
				SMSService.sendSMS(pharmacy.adminPharmacist.appUser.mobileNumber.toString(), smsToPharmacy.toString());


				sharedWith.append(phprInfo.pharmacy.name);
			}
		}

		if (diagnosticId != null && !diagnosticId.trim().isEmpty()) {
			final DiagnosticCentre diagnosticCentre = DiagnosticCentre.find
					.byId(Long.parseLong(diagnosticId));
			final DiagnosticCentrePrescriptionInfo dcpInfo = DiagnosticCentrePrescriptionInfo.find
					.where().eq("diagnosticCentre", diagnosticCentre)
					.eq("prescription", prescription).findUnique();
			if (dcpInfo == null) {
				final DiagnosticCentrePrescriptionInfo diagPrescriptionInfo = new DiagnosticCentrePrescriptionInfo();
				diagPrescriptionInfo.diagnosticCentre = diagnosticCentre;
				diagPrescriptionInfo.prescription = prescription;
				diagPrescriptionInfo.sharedBy = doctor.appUser;
				diagPrescriptionInfo.sharedDate = new Date();
				diagPrescriptionInfo.diagnosticCentrePrescritionStatus = DiagnosticCentrePrescritionStatus.RECEIVED;
				diagPrescriptionInfo.save();
				// send sms and email to dc
				final StringBuilder message = new StringBuilder();
				message.append("<html><body>");
				message.append("<p>Dear "+diagnosticCentre.diagnosticRepAdmin.appUser.name+",<br><br>Prescription of Dr."+doctor.appUser.name+" for patient "+prescription.patient.appUser.name+" has been shared with you.");
				message.append("<br><br>Best regards,<br>MedNetwork.in</p>");
				message.append("</body></html>");
				// Async Execution
				Promise.promise(new Function0<Integer>() {
					//@Override
					@Override
					public Integer apply() {
						int result = 0;
						if(!EmailService.sendSimpleHtmlEMail(diagnosticCentre.diagnosticRepAdmin.appUser.email, "Prescription Shared", message.toString())){
							result=1;
						}

						return result;
					}
				});
				// End of async


				final StringBuilder smsTodiagnosticCentre = new StringBuilder();
				smsTodiagnosticCentre.append(" Prescription of Dr."+doctor.appUser.name+" for patient has been shared with you.");
				SMSService.sendSMS(diagnosticCentre.diagnosticRepAdmin.appUser.mobileNumber.toString(), smsTodiagnosticCentre.toString());

				if (sharedWith.length() > 0) {
					sharedWith.append(" and ");
				}
				sharedWith.append(diagnosticCentre.name);
			}
		}

		if(!(
				(pharmacyId == null || pharmacyId.trim().isEmpty())
				&&
				(diagnosticId == null || diagnosticId.trim().isEmpty())
				)){

			//send mail to patient and doctor
			//send sms to patient
			final StringBuilder message = new StringBuilder();
			message.append("<html><body>");
			message.append("<p>Dear "+prescription.patient.appUser.name+",<br><br>Your prescription of Dr."+doctor.appUser.name+" has been shared with "+sharedWith.toString()+".");
			message.append("<br><br>Best regards,<br>MedNetwork.in</p>");
			message.append("</body></html>");
			// Async Execution
			Promise.promise(new Function0<Integer>() {
				//@Override
				@Override
				public Integer apply() {
					int result = 0;
					if(!EmailService.sendSimpleHtmlEMail(prescription.patient.appUser.email, "Prescription Shared", message.toString())){
						result=1;
					}

					return result;
				}
			});
			// End of async
			final StringBuilder messageToDoctor = new StringBuilder();
			messageToDoctor.append("<html><body>");
			messageToDoctor.append("<p>Dear Dr."+doctor.appUser.name+",<br><br>You have shared prescription for patient "+prescription.patient.appUser.name+" with "+sharedWith.toString()+".");
			messageToDoctor.append("<br><br>Best regards,<br>MedNetwork.in</p>");
			messageToDoctor.append("</body></html>");
			// Async Execution
			Promise.promise(new Function0<Integer>() {
				//@Override
				@Override
				public Integer apply() {
					int result = 0;
					if(!EmailService.sendSimpleHtmlEMail(doctor.appUser.email, "Prescription Shared", messageToDoctor.toString())){
						result=1;
					}

					return result;
				}
			});
			// End of async
			final StringBuilder smsToPatient = new StringBuilder();
			smsToPatient.append("Your prescription of Dr."+doctor.appUser.name+" has been shared with "+sharedWith.toString()+".");
			SMSService.sendSMS(prescription.patient.appUser.mobileNumber.toString(), smsToPatient.toString());
			flash().put("alert",new Alert("alert-success", "Prescription shared with "+ sharedWith.toString()).toString());
		}

		return redirect(routes.DoctorController.viewTodaysAppointments());
	}

	/**
	 * Action to show todays prescription created by loggedIn doctor GET
	 * /secure-doctor/todays-prescriptions
	 */
	@ConfirmAppUser
	public static Result viewTodaysPrescription() {
		final Date now = new Date();
		final Calendar calendarFrom = Calendar.getInstance();
		calendarFrom.setTime(now);
		calendarFrom.set(Calendar.HOUR_OF_DAY, 0);
		calendarFrom.set(Calendar.MINUTE, 0);
		calendarFrom.set(Calendar.SECOND, 0);
		calendarFrom.set(Calendar.MILLISECOND, 0);

		final Calendar calendarTo = Calendar.getInstance();
		calendarTo.setTime(now);
		calendarTo.set(Calendar.HOUR_OF_DAY, 23);
		calendarTo.set(Calendar.MINUTE, 59);
		calendarTo.set(Calendar.SECOND, 59);
		calendarTo.set(Calendar.MILLISECOND, 999);
		final Doctor doctor = LoginController.getLoggedInUser().getDoctor();
		final List<Prescription> prescriptionList = Prescription.find.where()
				.eq("doctor", doctor)
				.ge("prescriptionDate", calendarFrom.getTime())
				.le("prescriptionDate", calendarTo.getTime())
				.orderBy("prescriptionDate").findList();
		final List<DoctorClinicInfo> docclinicInfo = DoctorClinicInfo.find
				.where().eq("doctor", doctor).findList();
		return ok(views.html.doctor.doctorPrescriptionList.render(
				prescriptionList, docclinicInfo));
	}

	/**
	 * Action to show all prescription created by loggedIn doctor GET
	 * /secure-doctor/all-prescriptions
	 */
	@ConfirmAppUser
	public static Result viewAllPrescription() {
		final Doctor doctor = LoginController.getLoggedInUser().getDoctor();
		final List<Prescription> prescriptionList = Prescription.find.where()
				.eq("doctor", doctor).orderBy("prescriptionDate").findList();
		final List<DoctorClinicInfo> docclinicInfo = DoctorClinicInfo.find
				.where().eq("doctor", doctor).findList();
		return ok(views.html.doctor.doctorPrescriptionList.render(
				prescriptionList, docclinicInfo));
	}

	/**
	 * Action to get all Products' names GET /secure-doctor/products/get-json
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result getAllProductsJson() {
		final List<DoctorProduct> loggedInDoctorProductList = LoginController
				.getLoggedInUser().getDoctor().myProductList;
		final int arrayLength = MasterProduct.find.findRowCount()
				+ loggedInDoctorProductList.size();
		final String[] result = new String[arrayLength];
		int i = 0;
		for (final DoctorProduct product : loggedInDoctorProductList) {
			result[i] = product.fullName;
			i++;
		}
		final List<MasterProduct> masterProductList = MasterProduct.find.all();
		for (final MasterProduct product : masterProductList) {
			result[i] = product.fullName;
			i++;
		}
		final JSONArray jsonArray = new JSONArray(Arrays.asList(result));
		return ok(jsonArray.toString());
	}

	/**
	 * Action to get all Products' names GET /secure-doctor/diagnostic-tests/get-json
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result getAllDiagnosticJson() {
		final List<DoctorDiagnosticTest> loggedInDoctorDiagTestList = LoginController
				.getLoggedInUser().getDoctor().myDiagnosticTestList;
		final int arrayLength = MasterDiagnosticTest.find.findRowCount()
				+ loggedInDoctorDiagTestList.size();
		final String[] result = new String[arrayLength];
		int i = 0;
		for (final DoctorDiagnosticTest diagTest : loggedInDoctorDiagTestList) {
			result[i] = diagTest.name;
			i++;
		}
		final List<MasterDiagnosticTest> masterDiagnosticTestList = MasterDiagnosticTest.find
				.all();
		for (final MasterDiagnosticTest diagTest : masterDiagnosticTestList) {
			result[i] = diagTest.name;
			i++;
		}
		final JSONArray jsonArray = new JSONArray(Arrays.asList(result));
		return ok(jsonArray.toString());
	}



	public static Result displayAnswer() {
		final AppUser user = LoginController.getLoggedInUser();
		final Doctor doctor = user.getDoctor();
		List<QuestionAndAnswer> qaList = new ArrayList<QuestionAndAnswer>();
		if (doctor != null) {
			qaList = QuestionAndAnswer.find.where()
					.eq("answerBy.id", doctor.id).findList();
		}
		return ok(views.html.doctor.ansQuestion.render(qaList));
	}

	// Question Answered By Doctor
	public static Result answerQuestion(final Long qaId) {
		final QuestionAndAnswerBean qaBean = questionAndAnswerForm
				.bindFromRequest().get();
		final QuestionAndAnswer qa = QuestionAndAnswer.find.byId(qaId);
		qa.answer = qaBean.answer;
		qa.answerDate = new Date();
		qa.update();
		flash().put("alert", "saved answer successfully");
		return redirect(routes.DoctorController.displayAnswer());
	}

	public static boolean isListSame(final List<DaySchedule> arrayList1,
			final List<DaySchedule> arrayList2) {
		if (arrayList1.size() != arrayList2.size()) {
			Logger.info("if 1");
			return false;
		}
		for (int i = 0; i < arrayList1.size(); i++) {
			final DaySchedule schedule = arrayList1.get(i);
			final DaySchedule scheduleMr = arrayList2.get(i);
			if (!schedule.equals(scheduleMr)) {
				return schedule.equals(scheduleMr);
			}
		}
		return true;
	}

	public static Result processPrescriptionForm() {
		return ok();
	}

	public static Result doctorPrescription() {
		return ok(views.html.doctor.doctor_prescription.render());
	}

	public static Result doctorAppointments() {

		return ok(views.html.doctor.doctor_all_appointments.render());

	}

	public static Result doctorSearchAppointment() {

		return ok(views.html.doctor.doctor_search_appointments.render());

	}

	public static Result doctorViewAppointment() {

		return ok(views.html.doctor.doctor_view_appointment.render());
	}

	/**
	 * @author lakshmi Action to add favorite pharmacy of the Doctor to the list
	 *         of Doctor of loggedin DOCTOR
	 *         GET/doctor/add-favorite-pharmacy/:pharmacyId/:str
	 */
	@ConfirmAppUser
	public static Result addFavoritePharmacy(final Long pharmacyId,
			final String searchStr) {
		final Doctor doctor = LoginController.getLoggedInUser().getDoctor();
		final Pharmacy pharmacy = Pharmacy.find.byId(pharmacyId);

		if (doctor.pharmacyList.contains(pharmacy) != true) {
			doctor.pharmacyList.add(pharmacy);
			doctor.update();

		} else {
			flash().put(
					"alert",
					new Alert("alert-info", pharmacy.name
							+ " Already existed in the Favorite List.")
					.toString());
		}
		final List<Pharmacy> pharmacyList = new ArrayList<Pharmacy>();
		for (final Pharmacy pharmacy2 : Pharmacy.find.where()
				.like("searchIndex", "%" + searchStr + "%").findList()) {
			if (doctor.pharmacyList.contains(pharmacy2) != true) {
				pharmacyList.add(pharmacy2);
			}
		}
		return ok(views.html.pharmacist.searched_pharmacies.render(true,
				searchStr, pharmacyList));
	}

	/**
	 * @author lakshmi Action to list out favorite Pharmacies of Doctor of
	 *         loggedin DOCTOR GET/secure-doctor/my-favorite-pharmacies
	 */
	@ConfirmAppUser
	public static Result myFavoritePharmacies() {
		final Doctor doctor = LoginController.getLoggedInUser().getDoctor();
		return ok(views.html.pharmacist.favorite_pharmacy_list
				.render(doctor.pharmacyList));
	}

	/**
	 * @author lakshmi Action to add favorite pharmacy of the Doctor to the list
	 *         of Doctor of loggedin DOCTOR
	 *         GET/doctor/add-favorite-pharmacy/:pharmacyId/:str
	 */
	@ConfirmAppUser
	public static Result addFavoriteDiagnosticCentre(final Long diagnosticId,
			final String searchStr) {
		final Doctor doctor = LoginController.getLoggedInUser().getDoctor();
		final DiagnosticCentre diagnosticCentre = DiagnosticCentre.find
				.byId(diagnosticId);

		if (!doctor.diagnosticCentreList.contains(diagnosticCentre)) {
			doctor.diagnosticCentreList.add(diagnosticCentre);
			doctor.update();

		} else {
			flash().put(
					"alert",
					new Alert("alert-info", diagnosticCentre.name
							+ " Already existed in the Favorite List.")
					.toString());
		}
		return ok(views.html.diagnostic.searched_diagnostic_Centres.render(
				true, searchStr, doctor.diagnosticCentreList));
		// return redirect(routes.UserActions.dashboard());
	}

	/**
	 * @author lakshmi Action to list out favorite Diagnostic Centre of Doctor
	 *         of loggedin DOCTOR GET/doctor/favorite-diagnostic-centres
	 */
	@ConfirmAppUser
	public static Result myFavoriteDiagnosticCentres() {
		final Doctor doctor = LoginController.getLoggedInUser().getDoctor();
		return ok(views.html.diagnostic.favorite_diagnosticCentre_list
				.render(doctor.diagnosticCentreList));
	}

	@ConfirmAppUser
	public static Result requestAppointment() {
		final String param[] = request().body().asFormUrlEncoded()
				.get("datetime");
		try {
			final Appointment appointment = Appointment.find.byId(Long
					.parseLong(param[1]));
			appointment.problemStatement = param[0];
			appointment.requestedBy = LoginController.getLoggedInUser();
			appointment.appointmentStatus = AppointmentStatus.APPROVED;
			appointment.update();
			return ok("0");
		} catch (final Exception e) {
			e.printStackTrace();
			return ok("-1");
		}
	}

}

package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.AppUser;
import models.doctor.Doctor;
import models.doctor.MasterSpecialization;
import models.mr.DCRLineItem;
import models.mr.DCRStatus;
import models.mr.DailyCallReport;
import models.mr.MedicalRepresentative;
import models.mr.PharmaceuticalProduct;
import models.mr.Sample;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;

import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import beans.android.LoginBean;
import beans.android.DCRBean;
import beans.android.DCRLineItemBean;

public class AndroidController extends Controller{

	public static Form<LoginBean> loginBeanform = Form.form(LoginBean.class);
	public static Form<DCRBean> dcrBeanform = Form.form(DCRBean.class);
	public static Form<DCRLineItemBean> dcrLineItemBeanForm = Form.form(DCRLineItemBean.class);


	@BodyParser.Of(BodyParser.Json.class)
	public static Result login() {
		final LoginBean bean = loginBeanform.bindFromRequest().get();
		final String email = bean.email;
		final String password = bean.password;
		Logger.info("Android Login attempted with email: "+email);
		final List<AppUser> appUsers = AppUser.find.where().eq("email", email.trim().toLowerCase()).findList();
		if(appUsers.size() < 1) {
			Logger.error("NO APPUSER FOUND WITH EMAIL: "+email);
			final HashMap<String,String> map = new HashMap<String,String>();
			map.put("STATUS", "FALSE");
			final org.json.JSONObject json = new JSONObject(map);
			return ok(json.toString());
		}
		if(appUsers.size() == 1) {
			if(!(appUsers.get(0).matchPassword(password))){
				Logger.error("APPUSER FOUND BUT PASSWORD INCORRECT");
				final HashMap<String,String> map = new HashMap<String,String>();
				map.put("STATUS", "FALSE");
				final org.json.JSONObject json = new JSONObject(map);
				return ok(json.toString());
			}
			else{
				final AppUser appUser = appUsers.get(0);
				final MedicalRepresentative mr = appUser.getMedicalRepresentative();
				if(mr != null){
					Logger.info("Android Login Successful. AppUser Authenticated: "+email);
					final HashMap<String,String> map = new HashMap<String,String>();
					map.put("STATUS", "TRUE");
					map.put("NAME", appUser.name);
					map.put("APPUSER_ID", appUser.id+"");
					map.put("MR_ID", mr.id+"");
					final org.json.JSONObject json = new JSONObject(map);
					return ok(json.toString());
				}
				else{
					Logger.error("APPUSER FOUND & PASSWORD CORRECT BUT APPUSER IS NOT AN MR. APPUSER IS: "+appUser.role);
					final HashMap<String,String> map = new HashMap<String,String>();
					map.put("STATUS", "FALSE");
					final org.json.JSONObject json = new JSONObject(map);
					return ok(json.toString());
				}
			}
		}
		if(appUsers.size() > 1) {
			Logger.error(appUsers.size()+" APPUSERS FOUND WITH EMAIL: "+email);
			final HashMap<String,String> map = new HashMap<String,String>();
			map.put("STATUS", "FALSE");
			final org.json.JSONObject json = new JSONObject(map);
			return ok(json.toString());
		}
		final HashMap<String,String> map = new HashMap<String,String>();
		map.put("STATUS", "FALSE");
		final org.json.JSONObject json = new JSONObject(map);
		return ok(json.toString());

	}


	@BodyParser.Of(BodyParser.Json.class)
	public static Result getDoctors(final Long mrId){
		final MedicalRepresentative mr = MedicalRepresentative.find.byId(mrId);
		final List<Doctor> mrDoctors = mr.doctorList;
		final List<HashMap<String,String>> docMapList = new ArrayList<HashMap<String, String>>();
		for (final Doctor doctor : mrDoctors) {
			final HashMap<String, String> map = new HashMap<String, String>();
			map.put("DOCTOR_ID", doctor.id+"");
			map.put("NAME", doctor.appUser.name);
			map.put("DEGR", doctor.degree);
			final List<MasterSpecialization> spezList = doctor.specializationList;
			final StringBuilder sb = new StringBuilder();
			for (final MasterSpecialization spez : spezList) {
				sb.append(spez.name);
				sb.append(", ");
			}
			final String specString = sb.toString();
			if(specString.length() == 0){
				map.put("SPEC", "");
			}
			else{
				map.put("SPEC", specString.substring(0, (specString.length()-2)));
			}
			docMapList.add(map);
		}
		final JSONArray jsonArray = new JSONArray(docMapList);
		return ok(jsonArray.toString());
	}


	@BodyParser.Of(BodyParser.Json.class)
	public static Result getProducts(final Long mrId){
		final MedicalRepresentative mr = MedicalRepresentative.find.byId(mrId);
		final List<PharmaceuticalProduct> mrProducts = mr.pharmaceuticalCompany.pharmaceuticalProductList;
		final List<HashMap<String,String>> productMapList = new ArrayList<HashMap<String, String>>();
		for (final PharmaceuticalProduct product : mrProducts) {
			final HashMap<String, String> map = new HashMap<String, String>();
			map.put("PRODUCT_ID", product.id+"");
			map.put("NAME", product.fullName);
			productMapList.add(map);
		}
		final JSONArray jsonArray = new JSONArray(productMapList);
		return ok(jsonArray.toString());
	}



	@BodyParser.Of(BodyParser.Json.class)
	public static Result submitDCRLineItem(){
		Logger.info("Request: "+request().body().asJson());
		final JSONObject json = new JSONObject(request().body().asJson().toString());


		Logger.info(json.getString("MR_ID"));
		Logger.info(json.getString("FOR_DATE"));
		Logger.info(json.getString("DOCTOR_ID"));
		Logger.info(json.getString("IN_TIME"));
		Logger.info(json.getString("OUT_TIME"));
		Logger.info(json.getString("POB"));
		Logger.info(json.getString("REMARKS"));

		/*
		final JSONObject samples = json.getJSONObject("SAMPLES");
		final Set<String> sampleKeys = samples.keySet();
		for (final String key : sampleKeys) {
			Logger.info(key+"----"+samples.get(key));
		}

		final JSONArray promotions = json.getJSONArray("PROMOTIONS");
		final int l = promotions.length();
		for(int i=0; i<l; i++){
			Logger.info(promotions.getString(i));
		}
		 */

		final String forDateStr =  json.getString("FOR_DATE").trim();
		final SimpleDateFormat sdf = new SimpleDateFormat("");
		Date forDate = null;
		try{
			forDate = sdf.parse(forDateStr);
		}
		catch (final ParseException e){
			Logger.error("COULDN'T PARSE FOR-DATE WITH FOR-DATE STRING: "+forDateStr);
			Logger.error("RETURNING WITH ERROR CODE -1");
			final HashMap<String,String> map = new HashMap<String,String>();
			map.put("STATUS", "FALSE");
			map.put("ERROR", "-1");
			return ok(new JSONObject(map).toString());
		}

		//@TODO add other field validations -- mandatory fields are: ForDate, MR_ID, AppUserID, Doctor_Id, Verification_Code(Security)
		final String mrIdStr = json.getString("MR_ID").trim();
		final String docIdStr = json.getString("DOCTOR_ID").trim();
		final String pobStr = json.getString("POB").trim();
		final String remarks = json.getString("REMARKS").trim();


		final MedicalRepresentative mr = MedicalRepresentative.find.byId(Long.parseLong(mrIdStr));
		DailyCallReport dcr = DailyCallReport.find.where().eq("submitter",mr).eq("forDate",forDate).findUnique();

		// DCR Validation -- If MR's DCR with forDate exists, lineItem will be saved only if that DCR is in DRAFT, REJECTED or REOPENED state
		if(dcr != null){
			if(dcr.dcrStatus.equals(DCRStatus.SUBMITTED) || dcr.dcrStatus.equals(DCRStatus.APPROVED)){
				Logger.error("DCR FOR MR ID "+mrIdStr+" FOR DATE "+forDateStr+" IS IN "+dcr.dcrStatus+" STATE.");
				Logger.error("RETURNING WITH ERROR CODE "+dcr.dcrStatus);
				final HashMap<String,String> map = new HashMap<String,String>();
				map.put("STATUS", "FALSE");
				map.put("ERROR", dcr.dcrStatus.toString());
				return ok(new JSONObject(map).toString());
			}

			//@TODO: Add validation incase a lineItem for given Doc_ID already exists in the DCR
			// either replace the existing line item or
			// ask the android user (submitter-mr) what to do.

		}
		else{
			dcr = new DailyCallReport();
			dcr.forDate = forDate;
			dcr.submitter = mr;
			dcr.save();
		}

		final DCRLineItem dcrLineItem = new DCRLineItem();
		dcrLineItem.doctor = Doctor.find.byId(Long.parseLong(docIdStr));
		// @TODO convert POB from Integer to FLOAT
		dcrLineItem.pob = Integer.parseInt(pobStr);
		dcrLineItem.remarks = remarks.trim();


		final JSONObject samples = json.getJSONObject("SAMPLES");
		final Set<String> sampleKeys = samples.keySet();
		for (final String key : sampleKeys) {
			//Logger.info(key+"----"+samples.get(key));
			if(		samples.get(key) != null &&
					(!samples.get(key).toString().isEmpty()) &&
					(samples.get(key).toString().trim().compareTo("0") != 0)
					){
				final Sample sample = new Sample();
				sample.pharmaceuticalProduct = PharmaceuticalProduct.find.byId(Long.parseLong(key));
				sample.quantity = Integer.parseInt(samples.get(key).toString().trim());
				dcrLineItem.sampleList.add(sample);
			}
		}

		final JSONArray promotions = json.getJSONArray("PROMOTIONS");
		final int l = promotions.length();
		for(int i=0; i<l; i++){
			//Logger.info(promotions.getString(i));
			final Long phProdId = Long.parseLong(promotions.getString(i).trim());
			dcrLineItem.promotionList.add(PharmaceuticalProduct.find.byId(phProdId));
		}



		dcr.dcrLineItemList.add(dcrLineItem);
		dcr.update();

		final HashMap<String,String> map = new HashMap<String,String>();
		map.put("STATUS", "TRUE");
		return ok(new JSONObject(map).toString());
	}







}

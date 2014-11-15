package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.AppUser;
import models.doctor.Doctor;
import models.doctor.MasterSpecialization;
import models.mr.MedicalRepresentative;
import models.mr.PharmaceuticalProduct;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;

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

		Logger.info(json.getString("FOR_DATE"));
		Logger.info(json.getString("DOCTOR_ID"));
		Logger.info(json.getString("IN_TIME"));
		Logger.info(json.getString("OUT_TIME"));
		Logger.info(json.getString("POB"));
		Logger.info(json.getString("REMARKS"));

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




		return ok();
	}







}

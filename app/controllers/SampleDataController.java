package controllers;


import models.PrimaryCity;
import models.State;
import play.mvc.Controller;
import play.mvc.Result;

public class SampleDataController extends Controller {


	public static Result addPrimaryCity(){
		if(PrimaryCity.find.all().size() == 0){
			final PrimaryCity primaryCity = new PrimaryCity();
			primaryCity.name = "Hyderabad";
			primaryCity.state = State.TELANGANA;
			primaryCity.save();
		}
		return ok();
	}


}


package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import utils.Constants;
import controllers.LoginController;
import controllers.PublicController;

@SuppressWarnings("serial")
@Entity
public class Locality extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@OneToOne
	public PrimaryCity primaryCity;

	public String name;

	public String pinCode;

	public static Finder<Long, Locality> find = new Finder<Long, Locality>(Long.class, Locality.class);
	/*public static List<Locality> getLocalitiesInCity(){

		final PrimaryCity primaryCity = PrimaryCity.find.byId( PublicController.getPrimatyCity());
		final AppUser appUser = LoginController.getLoggedInUser();
		PrimaryCity primaryCity = null;
		if(appUser.role.equals(Role.DOCTOR)){
			primaryCity = appUser.getDoctor().primaryCity;
		}
		if(appUser.role.equals(Role.ADMIN_DIAGREP)){
			primaryCity = appUser.getDiagnosticRepresentative().diagnosticCentre.primaryCity;
		}
		if(appUser.role.equals(Role.ADMIN_PHARMACIST)){
			primaryCity = appUser.getPharmacist().pharmacy.primaryCity;
		}
		if(primaryCity != null){
			return Locality.find.where().eq("primaryCity", primaryCity).findList();
		}else{
			return new ArrayList<Locality>();
		}
	}*/

}

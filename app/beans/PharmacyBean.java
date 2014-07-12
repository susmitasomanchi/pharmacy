package beans;

import java.io.Serializable;

import models.Address;
import models.Country;
import models.State;
import models.pharmacist.Pharmacy;
import play.Logger;

public class PharmacyBean implements Serializable{

	public  Long id;

	public String name;

	public Address address;

	public String contactNo;

	public String testField;

	public String addrressLine1;

	public String addrressLine2;

	public String addrressLine3;



	public String description;


	//@Required
	public String city;


	//	@Required
	public State state;

	//	@Required
	public Long pinCode;

	//	@Required
	public Country country;



	public Pharmacy toPharmacy(){
		//final Pharmacy pharmacy = new Pharmacy();

		Logger.info(""+this.id);
		final Pharmacy pharmacy = Pharmacy.find.byId(this.id);
		//		final Pharmacy pharmacy=LoginController.getLoggedInUser().getPharmacist().pharmacy;

		if(this.id != null){
			pharmacy.id=this.id;
		}

		if(this.name != null) {
			pharmacy.name= this.name;
		}
		if(this.description != null) {
			pharmacy.description= this.description;
		}

		//if(this.address != null) {
		//	pharmacy.address = this.toAddress();
		//}


		if(this.contactNo != null) {
			pharmacy.contactNo= this.contactNo;
		}

		if(this.testField != null) {
			pharmacy.testField= this.testField;
		}

		final Address address = new Address();
		final Address oldAddress = Pharmacy.find.byId(this.id).address;
		//	Logger.info("OldAddress"+oldAddress.toString());
		if(oldAddress != null){
			address.id = oldAddress.id;
		}
		if(this.addrressLine1 != null){
			address.addrressLine1 = this.addrressLine1;
		}
		if(this.addrressLine2 != null){
			address.addrressLine2 = this.addrressLine2;
		}
		if(this.addrressLine3 != null){
			address.addrressLine3 = this.addrressLine3;
		}
		if(this.city!=null){
			address.city = this.city;
		}
		if(this.state != null){
			address.state = this.state;
		}
		if(this.country != null){
			address.country = this.country;
		}
		if(this.pinCode != null){
			address.pinCode = this.pinCode;
		}
		pharmacy.address = address;
		return pharmacy;
	}





}

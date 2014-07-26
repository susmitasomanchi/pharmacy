package beans;

import java.io.Serializable;

import models.Address;
import models.Country;
import models.State;
import models.pharmacist.Pharmacy;
import play.Logger;

@SuppressWarnings("serial")
public class PharmacyBean implements Serializable{

	public  Long id;

	public String name;

	public Address address;

	public String contactNumber;

	public String contactPerson;

	public String addressLine1;

	public String addressLine2;

	public String addressLine3;



	public String description;


	//@Required
	public String city;


	//	@Required
	public State state;

	//	@Required
	public String pinCode;

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


		if(this.contactNumber != null) {
			pharmacy.contactNumber= this.contactNumber;
		}

		if(this.contactPerson != null) {
			pharmacy.contactPerson= this.contactPerson;
		}

		final Address address = new Address();
		final Address oldAddress = Pharmacy.find.byId(this.id).address;
		//	Logger.info("OldAddress"+oldAddress.toString());
		if(oldAddress != null){
			address.id = oldAddress.id;
		}
		if(this.addressLine1 != null){
			address.addressLine1 = this.addressLine1;
		}
		if(this.addressLine2 != null){
			address.addressLine2 = this.addressLine2;
		}
		if(this.addressLine3 != null){
			address.addressLine3 = this.addressLine3;
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

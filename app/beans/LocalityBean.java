package beans;

import java.io.Serializable;

import models.Locality;
import models.PrimaryCity;
import play.Logger;

@SuppressWarnings("serial")
public class LocalityBean implements Serializable {

	public Long id;

	public Long primaryCityId;

	public String name;

	public String pinCode;

	public Locality toLocality() {

		Locality locality;
		if (this.id != null) {
			Logger.info(this.id+"idssssssssss");
			locality = Locality.find.byId(this.id);
		}
		else{
			locality = new Locality();

		}

		if(this.name != null){
			locality.name = this.name;
		}
		if(this.primaryCityId != null){
			locality.primaryCity = PrimaryCity.find.byId(this.primaryCityId);
		}
		if(this.pinCode != null){
			locality.pinCode = this.pinCode;
		}

		return locality;
	}
}

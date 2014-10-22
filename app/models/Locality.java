package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import beans.LocalityBean;

@SuppressWarnings("serial")
@Entity
public class Locality extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;


	@OneToOne
	public PrimaryCity primaryCity;

	@Required
	public String name;

	public String pinCode;

	public static Model.Finder<Long, Locality> find = new Model.Finder<Long, Locality>(Long.class, Locality.class);

	public LocalityBean toBean(){
		final LocalityBean bean = new LocalityBean();

		if(this.id != null) {
			bean.id = this.id;
		}

		if(this.name != null) {
			bean.name = this.name;
		}

		if(this.primaryCity != null) {
			bean.primaryCityId = this.primaryCity.id;
		}

		if(this.pinCode != null) {
			bean.pinCode = this.pinCode;
		}

		return bean;
	}


}

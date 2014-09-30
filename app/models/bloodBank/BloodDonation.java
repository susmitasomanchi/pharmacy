package models.bloodBank;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import models.AppUser;
import models.BaseEntity;
import models.BloodGroup;
@SuppressWarnings("serial")
@Entity
public class BloodDonation extends BaseEntity{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	@OneToOne
	public BloodBank bloodBank;

	public Date dateDonated;

	public float quantityDonated;

	public float weight;

	public float hemoglobinLevel;

	public BloodGroup bloodGroup;

	public static Finder<Long, BloodDonation> find = new Finder<Long, BloodDonation>(Long.class, BloodDonation.class);

	public static Date getLastDonatedDate(final AppUser appUser){
		return BloodDonation.find.where().eq("app_user_id", appUser.id).orderBy("dateDonated DESC").findList().get(0).dateDonated;
	}


}

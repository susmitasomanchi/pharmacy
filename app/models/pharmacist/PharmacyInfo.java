package models.pharmacist;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class PharmacyInfo {
	@Id
	public Long id;

	@OneToOne
	public Pharmacy  pharmacy;

	public static Model.Finder<Long, PharmacyInfo> find = new Finder<Long, PharmacyInfo>(Long.class, PharmacyInfo.class);

}

package models.doctor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import models.Address;
import models.BaseEntity;
import models.PrimaryCity;
import models.clinic.ClinicAdministrator;
import models.pharmacist.Pharmacist;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class Clinic extends BaseEntity{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	public String name;

	public String contactPersonName;

	public String contactNo;

	@OneToOne
	public Address address;

	@OneToOne
	public ClinicAdministrator clinicAdministrator;

	@OneToOne
	public PrimaryCity primaryCity;

	public static Model.Finder<Long, Clinic> find = new Finder<Long, Clinic>(Long.class, Clinic.class);

	@Override
	public boolean equals(final Object arg0) {
		if(!this.name.equals(((Clinic)arg0).name)){
			return this.name.equals(((Clinic)arg0).name);
		}
		if(!this.contactPersonName.equals(((Clinic)arg0).contactPersonName)){
			return this.contactPersonName.equals(((Clinic)arg0).contactPersonName);
		}

		if(!this.contactNo.equals(((Clinic)arg0).contactNo)){
			return this.contactNo.equals(((Clinic)arg0).contactNo);
		}

		return super.equals(arg0);
	}

}

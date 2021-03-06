package models.pharmacist;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import models.AppUser;
import models.BaseEntity;
import models.doctor.Prescription;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class PharmacyPrescriptionInfo extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@OneToOne
	public Pharmacy pharmacy;

	@OneToOne
	public Prescription prescription;

	public PharmacyPrescriptionStatus pharmacyPrescriptionStatus = PharmacyPrescriptionStatus.RECEIVED;

	@OneToOne
	public AppUser sharedBy;

	public Date sharedDate;

	public Date servedDate;

	public Boolean patientsConsent = Boolean.FALSE;

	public static Model.Finder<Long, PharmacyPrescriptionInfo> find = new Finder<Long, PharmacyPrescriptionInfo>(Long.class, PharmacyPrescriptionInfo.class);

}

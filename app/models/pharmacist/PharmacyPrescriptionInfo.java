package models.pharmacist;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import models.BaseEntity;
import models.doctor.Prescription;

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
	
	public Date receivedDate;
	
	public static Model.Finder<Long, PharmacyPrescriptionInfo> find = new Finder<Long, PharmacyPrescriptionInfo>(Long.class, PharmacyPrescriptionInfo.class);
	
}

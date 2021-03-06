package models.pharmacist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.BaseEntity;
import models.doctor.Prescription;

@SuppressWarnings("serial")
@Entity
public class PharmacyOrder extends BaseEntity{


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
	@OneToOne
	public Prescription prescription;

	public PharmacyPrescriptionStatus pharmacyPrescriptionStatus = PharmacyPrescriptionStatus.RECEIVED;

	public Date OrderedDate;
	
	public Date servedDate;

	/*@OneToMany(cascade=CascadeType.ALL)
	public List<OrderLineItem> orderLineItemList = new ArrayList<OrderLineItem>();*/

	public Float totalAmount;

	public static Finder<Long, PharmacyOrder> find = new Finder<Long, PharmacyOrder>(Long.class, PharmacyOrder.class);

}

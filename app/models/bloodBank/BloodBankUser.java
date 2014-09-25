package models.bloodBank;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import models.AppUser;
import models.BaseEntity;
@SuppressWarnings("serial")
@Entity
public class BloodBankUser extends BaseEntity{
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	@OneToOne
	public AppUser appUser;

	@ManyToOne(cascade=CascadeType.ALL)
	public BloodBank bloodBank;

	public static Finder<Long, BloodBankUser> find = new Finder<Long, BloodBankUser>(Long.class, BloodBankUser.class);



}

package models.bloodBank;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.Address;
import models.BaseEntity;
import models.FileEntity;
import models.PrimaryCity;

@SuppressWarnings("serial")
@Entity
public class BloodBank extends BaseEntity{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	public String name;

	public String contactPersonName;

	public String contactNo;

	@OneToOne
	public Address address;

	@OneToOne
	public PrimaryCity primaryCity;

	@OneToOne
	public BloodBankUser bloodBankAdmin;

	@OneToMany(cascade=CascadeType.ALL)
	List<BloodBankUser> bloodBankUserList = new ArrayList<BloodBankUser>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<FileEntity> fileEntities = new ArrayList<FileEntity>();

	public static Finder<Long, BloodBank> find = new Finder<Long, BloodBank>(Long.class, BloodBank.class);


}

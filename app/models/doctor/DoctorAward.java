package models.doctor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.BaseEntity;

@SuppressWarnings("serial")
@Entity
public class DoctorAward extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	public String awardName;

	public String awardedBy;

	public String year;

	@Column(columnDefinition="TEXT")
	public String description;

	public Integer position;

	public static Finder<Long, DoctorAward> find = new Finder<Long, DoctorAward>(Long.class, DoctorAward.class);

}

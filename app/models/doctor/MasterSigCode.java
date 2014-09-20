package models.doctor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.BaseEntity;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class MasterSigCode extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	public String code;

	@Column(columnDefinition="TEXT")
	public String description;

	public static Model.Finder<Long, MasterSigCode> find = new Finder<Long, MasterSigCode>(Long.class, MasterSigCode.class);
}

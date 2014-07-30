package models.doctor;

import javax.persistence.Column;
import javax.persistence.Entity;

import models.BaseEntity;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class MasterSigCode extends BaseEntity{

	public String code;

	@Column(columnDefinition="TEXT")
	public String description;

	public static Model.Finder<Long, MasterSigCode> find = new Finder<Long, MasterSigCode>(Long.class, MasterSigCode.class);
}

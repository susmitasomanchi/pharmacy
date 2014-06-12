package models;

import javax.persistence.Entity;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class SigCode extends BaseEntity{

	public String code;
	public String description;

	public static Model.Finder<Long, SigCode> find = new Finder<Long, SigCode>(Long.class, SigCode.class);
}

package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.db.ebean.Model;


@SuppressWarnings("serial")
@Entity
public class DiagnosticRep extends Model{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	@OneToOne
	AppUser appUser;

	public String diagnosticType;

	public static Finder<Long, DiagnosticRep> find = new Finder<Long, DiagnosticRep>(Long.class, DiagnosticRep.class);


}

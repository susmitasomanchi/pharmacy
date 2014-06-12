package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;


@SuppressWarnings("serial")
@Entity
public class DiagnosticRepresentative extends BaseEntity{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	@OneToOne
	public AppUser appUser;

	public String diagnosticType;

	public static Finder<Long, DiagnosticRepresentative> find = new Finder<Long, DiagnosticRepresentative>(Long.class, DiagnosticRepresentative.class);


}

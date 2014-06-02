package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@SuppressWarnings("serial")
@Entity
public class DiagnosticRep extends AppUser{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	public String diagnosticType;

	public static Finder<Long, DiagnosticRep> find = new Finder<>(Long.class, DiagnosticRep.class);


}

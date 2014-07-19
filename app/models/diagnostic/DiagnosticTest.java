package models.diagnostic;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.BaseEntity;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
@Entity
public class DiagnosticTest extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
	public String name;
	
	public String description;
	
	public Float price;
	

	public static Model.Finder<Long, DiagnosticTest> find = new Finder<Long, DiagnosticTest>(
			Long.class, DiagnosticTest.class);
	public String toString(){
		return name+"~~~~~~~~~~~~"+description+"~~~~~~~~~~~~"+price;
	}

}

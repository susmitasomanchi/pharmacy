package models.doctor;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.BaseEntity;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class MasterSpecialization extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@Column(columnDefinition="TEXT")
	public String name;

	@Column(columnDefinition="TEXT")
	public String remarks;

	public static Model.Finder<Long,MasterSpecialization> find = new Finder<Long, MasterSpecialization>(Long.class, MasterSpecialization.class);

	public static List<MasterSpecialization> getAll(){
		return find.orderBy("name").findList();
	}

}

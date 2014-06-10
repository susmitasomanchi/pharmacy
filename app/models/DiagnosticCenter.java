package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import akka.event.slf4j.Logger;
@Entity
public class DiagnosticCenter extends Model{
	@Id
	public Long id;
	@Required
	public String diagnoCenterName;
	@Required
	public String services;
	@Required
	public String contactPersonName;
	@Required
	public String address;
	public String city;
	@Required
	public String mobileNo;
	@Email @Required
	public String emailId;
	public String websiteName;
	
	@ManyToOne
	List<DiagnosticRepresentative> pharmacistlist = new ArrayList<DiagnosticRepresentative>();
	
	
	public static Model.Finder<Long, DiagnosticCenter> find = new Finder<Long, DiagnosticCenter>(
			Long.class, DiagnosticCenter.class);
	 public static List<DiagnosticCenter> all() {
			/*List<DiagnosticCenterForm> list=find.all();*/
		/*	Logger.info("fyjfuuygjh     ",list);*/
				  return find.all();
			   
			  }
	 public static List<DiagnosticCenter> getDetails(String name2) {
			// TODO Auto-generated method stub
			List<DiagnosticCenter> c1 = find.where().eq("diagnoCenterName", name2).findList();

			return c1;
		}
	
	public String toString(){
		return id+"  "+diagnoCenterName+"  "+services+"  "+contactPersonName+"  "+address+"  "+mobileNo+"  "+emailId+"  "+websiteName;
	}
	public static void delete(Long id2) {
		// TODO Auto-generated method stub
		 
			  find.ref(id2).delete();
		 
		
	}
	
	

}

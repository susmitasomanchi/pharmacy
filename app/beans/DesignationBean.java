package beans;

import java.io.Serializable;

import models.mr.Designation;


@SuppressWarnings("serial")
public class DesignationBean implements Serializable{
	
	public Long id;
	
	public String name;
	
	public String description;
	
	public Designation toDesignation(){
		Designation designation = new Designation();
		
		if(this.id != null){
			designation.id=this.id;
		}
		if(this.name != null){
			designation.name=this.name;
		}
		if(this.description != null){
			designation.description=this.description;
		}
		return designation;
	}
	
	
}

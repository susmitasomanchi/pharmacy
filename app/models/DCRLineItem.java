package models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.elasticsearch.common.joda.time.DateTime;

@SuppressWarnings("serial")
@Entity
public class DCRLineItem extends BaseEntity{
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;
	
	@OneToOne
	public Doctor doctor;
	
	public DateTime fromTime;
	
	public DateTime toTime;
	
	public Integer pob;
	
	public String remarks;
	

}

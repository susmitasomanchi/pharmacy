package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@SuppressWarnings("serial")
@Entity
public class DailyCallReport extends BaseEntity{
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;
	
	public Date forDate;
	
	//@OneToMany
	//public List<DCRLineItem> dcrLineItemList = new ArrayList<DCRLineItem>(); 
	
	public DCRLineItem dcrLineItem = new DCRLineItem();
	public static Finder<Long, DailyCallReport> find = new Finder<Long, DailyCallReport>(Long.class, DailyCallReport.class);

}

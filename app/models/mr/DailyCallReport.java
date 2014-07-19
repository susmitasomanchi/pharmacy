package models.mr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.BaseEntity;

@SuppressWarnings("serial")
@Entity
public class DailyCallReport extends BaseEntity{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	public Date forDate;

	@OneToOne
	public MedicalRepresentative submitter;

	@OneToOne
	public MedicalRepresentative approver;

	public DCRStatus dcrStatus;

	public Date submittedDate;

	public Date approvedDate;

	public Date rejectedDate;

	public Date reOpenedDate;

	@OneToOne
	public HeadQuarter headQuarter = new HeadQuarter();

	@OneToMany(cascade=CascadeType.ALL)
	public List<DCRLineItem> dcrLineItemList = new ArrayList<DCRLineItem>();

	public static Finder<Long, DailyCallReport> find = new Finder<Long, DailyCallReport>(Long.class, DailyCallReport.class);

}

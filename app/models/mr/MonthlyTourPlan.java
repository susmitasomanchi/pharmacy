package models.mr;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.BaseEntity;


@SuppressWarnings("serial")
@Entity
public class MonthlyTourPlan extends BaseEntity{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	public Integer srNo;

	public Date forMonth;

	public MedicalRepresentative submitter;

	public MedicalRepresentative approver;

	public DCRStatus status;

	public Date submitDate;

	public static Finder<Long, MonthlyTourPlan> find = new Finder<Long, MonthlyTourPlan>(Long.class, MonthlyTourPlan.class);
}

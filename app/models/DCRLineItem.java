package models;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import models.doctor.Doctor;


@SuppressWarnings("serial")
@Entity
public class DCRLineItem extends BaseEntity{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	@OneToOne
	public Doctor doctor;

	public Date fromTime;

	public Date toTime;

	public Integer pob;

	public String remarks;


}

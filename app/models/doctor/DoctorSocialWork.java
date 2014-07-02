package models.doctor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.BaseEntity;

@SuppressWarnings("serial")
@Entity
public class DoctorSocialWork extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;
	
	//Social Work
	
	public String socialWorkTittle;
	
	public String CommentSocialWork;
	
	
	
	
	

}

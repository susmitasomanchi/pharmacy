package models.clinic;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.db.ebean.Model;
import models.AppUser;
import models.BaseEntity;
import models.doctor.Clinic;
import models.doctor.Doctor;

@SuppressWarnings("serial")
@Entity
public class ClinicInvite extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@OneToOne
	public Doctor doctor;

	@OneToOne
	public Clinic clinic;

	@OneToOne
	public AppUser invitedBy;

	@OneToOne
	public AppUser acceptedBy;

	public Boolean accepted = false;

	public Date dateInvited;

	public Date dateAccepted;

	@Column(columnDefinition="TEXT")
	public String verificationCode;

	public static Model.Finder<Long,ClinicInvite> find = new Model.Finder<Long, ClinicInvite>(Long.class, ClinicInvite.class);

	public static List<ClinicInvite> getInvites(final Doctor doctor, final Clinic clinic){
		return ClinicInvite.find.where().eq("doctor", doctor).eq("clinic", clinic).eq("accepted", false).findList();
	}
}

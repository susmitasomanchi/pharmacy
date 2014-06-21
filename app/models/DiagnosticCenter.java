package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class DiagnosticCenter extends BaseEntity {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	public Long id;

	@Required
	public String name;

	@Required
	public String services;

	@Required
	public String costOfServices;

	@Required
	public String contactPersonName;

	@Required
	public String address;

	@Required
	public String mobileNo;

	@Email
	@Required
	public String emailId;

	public String websiteName;

	@OneToOne
	public DiagnosticRepresentative diagnosticRepAdmin;

	@ManyToOne(cascade=CascadeType.ALL)
	List<DiagnosticRepresentative> pharmacistlist = new ArrayList<DiagnosticRepresentative>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<DiagnosticTest> diagnosticTestList = new ArrayList<DiagnosticTest>();

	public static Model.Finder<Long, DiagnosticCenter> find = new Finder<Long, DiagnosticCenter>(
			Long.class, DiagnosticCenter.class);

	public static List<DiagnosticCenter> all() {
		return find.all();

	}

	public static List<DiagnosticCenter> getDetails(final String name2) {
		// TODO Auto-generated method stub
		final List<DiagnosticCenter> c1 = find.where().eq("name", name2)
				.findList();

		return c1;
	}

	@Override
	public String toString() {
		return this.id + "  " + this.name + "  " + this.services + "  "
				+ this.contactPersonName + "  " + this.address + "  "
				+ this.mobileNo + "  " + this.emailId + "  " + this.websiteName;
	}

	public static void delete(final Long id2) {
		// TODO Auto-generated method stub

		find.ref(id2).delete();

	}

}

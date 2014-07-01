package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@SuppressWarnings("serial")
@Entity
public class Prescription extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	public String problemStatement;

	public String prognosis;

	@OneToMany(cascade=CascadeType.ALL)
	List<PrescribedMedicine> prescriptionMedicineList = new ArrayList<PrescribedMedicine>();

	@OneToMany(cascade=CascadeType.ALL)
	List<PrescribedDiagnosticTest> diagnosticTestList = new ArrayList<PrescribedDiagnosticTest>();

	public String remarks;

	public String contactNo;


}

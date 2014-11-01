
package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
@Entity
@SuppressWarnings("serial")
public class Feedback extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@OneToOne
	public AppUser appUser;

	public String name;

	public Role role;

	public String email;

	public Date date;

	public String ipAddress;

	@Column(columnDefinition="TEXT")
	public String remarks;

	public static Finder<Long, Feedback> find = new Finder<Long, Feedback>(Long.class, Feedback.class);
}

package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@SuppressWarnings("serial")
@Entity
public class BlogCommentReply extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@Column(columnDefinition="TEXT")
	public String message;

	public Date date;

	@OneToOne
	public AppUser by;

	@OneToOne
	public SocialUser socialBy;

	public BlogCommentatorType blogCommentatorType;

	public static Finder<Long,BlogCommentReply> find = new Finder<>(Long.class, BlogCommentReply.class);

}


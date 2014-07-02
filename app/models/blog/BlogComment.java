package models.blog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.AppUser;
import models.BaseEntity;

@SuppressWarnings("serial")
@Entity
public class BlogComment extends BaseEntity{

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

	@OneToMany(cascade = CascadeType.ALL)
	public List<BlogCommentReply> replyList = new ArrayList<BlogCommentReply>();

	public static Finder<Long,BlogComment> find = new Finder<>(Long.class, BlogComment.class);

	public List<BlogCommentReply> replyListInOrder(){
		return BlogCommentReply.find.where().eq("blog_comment_id", this.id).orderBy("date DESC").findList();
	}

}

package models.blog;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.BaseEntity;
import play.db.ebean.Model;
import utils.Util;

@SuppressWarnings("serial")
@Entity
public class Article extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@Column(columnDefinition="TEXT")
	public String name;

	public Integer position;

	@Column(columnDefinition="TEXT")
	public String shortDescription;

	@Column(columnDefinition="TEXT")
	public String onHoverContent;

	@Column(columnDefinition="TEXT")
	public String content;

	@Column(columnDefinition="TEXT")
	public String slugURL;

	@Lob
	public byte[] thumbnail;

	@Lob
	public byte[] image;

	@Column(columnDefinition="TEXT")
	public String htmlTitle;

	@Column(columnDefinition="TEXT")
	public String htmlMetaDescription;

	@Column(columnDefinition="TEXT")
	public String htmlKeywords;

	@OneToOne
	public ArticleCategory category;

	@OneToMany(cascade = CascadeType.ALL)
	public List<BlogComment> commentList = new ArrayList<>();

	public static Model.Finder<Long,Article> find = new Finder<Long, Article>(Long.class, Article.class);

	@Override
	public void save(){
		this.slugURL = Util.simpleSlugify(this.name.trim()+"-"+this.shortDescription.trim());
		super.save();
	}

	@Override
	public void update(){
		this.slugURL = Util.simpleSlugify(this.name.trim()+"-"+this.shortDescription.trim());
		super.update();
	}

	public List<BlogComment> commentListInOrder(){
		return BlogComment.find.where().eq("article_id", this.id).orderBy("date DESC").findList();
	}

}

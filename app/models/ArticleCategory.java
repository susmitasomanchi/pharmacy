package models;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import play.db.ebean.Model;
import utils.Util;

@SuppressWarnings("serial")
@Entity
public class ArticleCategory extends BaseEntity{

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

	public static Model.Finder<Long,ArticleCategory> find = new Finder<Long, ArticleCategory>(Long.class, ArticleCategory.class);

	public List<Article> getArticles(){
		return Article.find.where().eq("category.id", this.id).findList();
	}

	public Integer getArticleSize(){
		return Article.find.where().eq("category.id", this.id).findRowCount();
	}

	public List<Article> getArticlesInOrder(){
		return Article.find.where().eq("category.id", this.id).orderBy("position").findList();
	}

	public static List<ArticleCategory> getCategoriesInOrder(){
		return find.orderBy("position").findList();
	}

	@Override
	public void save(){
		this.slugURL = Util.simpleSlugify(this.name);
		super.save();
	}

	@Override
	public void update(){
		this.slugURL = Util.simpleSlugify(this.name);
		super.update();
	}

}

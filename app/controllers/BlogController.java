package controllers;

import java.io.File;
import java.util.List;

import models.Alert;
import models.Article;
import models.ArticleCategory;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import com.google.common.io.Files;


// @TODO: Blog Auth
public class BlogController extends Controller {

	public static Form<Article> articleForm =Form.form(Article.class);
	public static Form<ArticleCategory> categoryForm =Form.form(ArticleCategory.class);

	public static Result blogHome(){
		return ok(views.html.blog.home.render());
	}

	public static Result categoryForm(){
		return ok(views.html.blog.categoryForm.render(categoryForm));
	}

	public static Result editCategoryForm(final Long id){
		final Form<ArticleCategory> filledForm =  categoryForm.fill(ArticleCategory.find.byId(id));
		return ok(views.html.blog.categoryForm.render(filledForm));
	}

	public static Result processCategoryForm(){
		final Form<ArticleCategory> filledForm = categoryForm.bindFromRequest();
		if(filledForm.hasErrors()) {
			return badRequest(views.html.blog.categoryForm.render(filledForm));
		}
		else {
			final ArticleCategory category = filledForm.get();

			final List<ArticleCategory> categories = ArticleCategory.find.all();

			if(category.id == null) {
				for (final ArticleCategory c : categories) {
					if(c.name.trim().compareToIgnoreCase(category.name.trim())==0){
						flash().put("alert", new Alert("alert-danger", "Another Category Named "+c.name+" Already Exists.").toString());
						categoryForm.fill(category);
						return ok(views.html.blog.categoryForm.render(categoryForm));
					}
				}
				category.save();

			} else {
				category.update();
			}
		}
		return redirect(routes.BlogController.categories());
	}

	public static Result categories(){
		return ok(views.html.blog.categories.render());
	}

	public static Result articleForm(){
		return ok(views.html.blog.articleForm.render(articleForm, new Article()));
	}

	public static Result editArticleForm(final Long id){
		final Article article = Article.find.byId(id);
		final Form<Article> filledForm =  articleForm.fill(Article.find.byId(id));
		return ok(views.html.blog.articleForm.render(filledForm,article));
	}

	public static Result processArticleForm(){
		final Article article = articleForm.bindFromRequest().get();
		final Long categoryId = Long.parseLong(request().body().asMultipartFormData().asFormUrlEncoded().get("categoryid")[0]);
		final ArticleCategory category = ArticleCategory.find.byId(categoryId);
		if(article.id == null){
			article.category = category;
			final File thumbnail = request().body().asMultipartFormData().getFile("thumbnail").getFile();
			final File image = request().body().asMultipartFormData().getFile("image").getFile();
			try{
				article.thumbnail = Files.toByteArray(thumbnail);
				article.image = Files.toByteArray(image);
			}
			catch (final Exception e){
				Logger.error("Error While Saving File");
				e.printStackTrace();
			}
			article.save();
		}
		else{
			if(request().body().asMultipartFormData().getFile("thumbnail") != null){
				final File thumbnail = request().body().asMultipartFormData().getFile("thumbnail").getFile();
				try{
					article.thumbnail = Files.toByteArray(thumbnail);
				}
				catch (final Exception e){
					Logger.error("Error While Saving Thumbnail");
					e.printStackTrace();
				}
			}
			if(request().body().asMultipartFormData().getFile("image") != null){
				final File image = request().body().asMultipartFormData().getFile("image").getFile();
				try{
					article.image = Files.toByteArray(image);
				}
				catch (final Exception e){
					Logger.error("Error While Saving Main Image");
					e.printStackTrace();
				}
			}
			article.category = category;
			article.update();
		}

		return redirect(routes.BlogController.articles(article.category.id));
	}

	public static Result articles(final Long categoryId){
		final ArticleCategory category = ArticleCategory.find.byId(categoryId);
		return ok(views.html.blog.articles.render(category));
	}

	public static Result showArticleWithoutSlug(final Long id){
		final Article article = Article.find.byId(id);
		final List<Article> lessThanList = Article.find.where().le("position", article.position).findList();
		final int offset = lessThanList.size();
		return ok(views.html.blog.showArticle.render(article,offset));
	}

	public static Result showArticle(final Long id, final String slug){
		final Article article = Article.find.byId(id);
		final List<Article> lessThanList = Article.find.where().le("position", article.position).findList();
		final int offset = lessThanList.size();
		return ok(views.html.blog.showArticle.render(article,offset));
	}

	public static Result getThumbnail(final Long id) {
		return ok(Article.find.byId(id).thumbnail).as("image/jpeg");
	}

	public static Result getMainImage(final Long id) {
		return ok(Article.find.byId(id).image).as("image/jpeg");
	}

}

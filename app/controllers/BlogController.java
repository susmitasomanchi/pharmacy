package controllers;

import java.io.File;
import java.util.Date;
import java.util.List;

import models.Alert;
import models.Article;
import models.ArticleCategory;
import models.BlogComment;
import models.BlogCommentReply;
import models.BlogCommentatorType;
import models.SocialUser;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import actions.BasicAuth;

import com.google.common.io.Files;


public class BlogController extends Controller {

	public static Form<Article> articleForm =Form.form(Article.class);
	public static Form<ArticleCategory> categoryForm =Form.form(ArticleCategory.class);

	public static Result blogHome(){
		return ok(views.html.blog.home.render());
	}
	

	@BasicAuth
	public static Result categoryForm(){
		return ok(views.html.blog.categoryForm.render(categoryForm));
	}

	@BasicAuth
	public static Result editCategoryForm(final Long id){
		final Form<ArticleCategory> filledForm =  categoryForm.fill(ArticleCategory.find.byId(id));
		return ok(views.html.blog.categoryForm.render(filledForm));
	}

	@BasicAuth
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

	@BasicAuth
	public static Result categories(){
		return ok(views.html.blog.categories.render());
	}

	@BasicAuth
	public static Result articleForm(){
		return ok(views.html.blog.articleForm.render(articleForm, new Article()));
	}

	@BasicAuth
	public static Result editArticleForm(final Long id){
		final Article article = Article.find.byId(id);
		final Form<Article> filledForm =  articleForm.fill(Article.find.byId(id));
		return ok(views.html.blog.articleForm.render(filledForm,article));
	}

	@BasicAuth
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

	@BasicAuth
	public static Result articles(final Long categoryId){
		final ArticleCategory category = ArticleCategory.find.byId(categoryId);
		return ok(views.html.blog.articles.render(category));
	}

	public static Result showArticle(final Long id, final String slug){
		final Article article = Article.find.byId(id);
		final List<Article> lessThanList = Article.find.where().le("position", article.position).findList();
		final int offset = lessThanList.size();
		return ok(views.html.blog.showArticle.render(article,offset));
	}

	public static Result showArticleWithIdOrSlug(final String idOrSlug){
		Article article;
		try{
			final Long id = Long.parseLong(idOrSlug);
			Logger.info("Found the article with id");
			article = Article.find.byId(id);
		}
		catch(final NumberFormatException e){
			Logger.info("Could not find the article with id, Trying with slug");
			final List<Article> articles = Article.find.where().eq("slugURL", idOrSlug.trim()).findList();
			if((articles.size() == 0) || (articles.size() > 1)){
				Logger.info("Could not find the article even with slug");
				//TODO: Redirect To 404
				return redirect(routes.BlogController.blogHome());
			}
			Logger.info("Found the article with slug");
			article = articles.get(0);
		}
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

	public static Result addComment(final Long id, final String message){
		try{
			final Article article = Article.find.byId(id);
			final BlogComment comment = new BlogComment();
			comment.by = LoginController.getLoggedInUser();
			comment.date = new Date();
			comment.message = message;
			comment.blogCommentatorType = BlogCommentatorType.APP_USER;
			article.commentList.add(comment);
			article.update();
			return ok(comment.id+"");
		}
		catch(final Exception e){
			Logger.error("Error while saving AppUser's comment.");
			e.printStackTrace();
			return ok("-1");
		}
	}

	public static Result addCommentReply(final Long id, final Long cid, final String message){
		try{
			final Article article = Article.find.byId(id);
			final BlogComment comment = BlogComment.find.byId(cid);
			final BlogCommentReply reply = new BlogCommentReply();
			reply.by = LoginController.getLoggedInUser();
			reply.date = new Date();
			reply.message = message;
			reply.blogCommentatorType = BlogCommentatorType.APP_USER;
			article.commentList.remove(comment);
			comment.replyList.add(reply);
			article.commentList.add(comment);
			article.update();
			return ok(reply.id+"");
		}
		catch(final Exception e){
			Logger.error("Error while saving SocialUser's comment.");
			e.printStackTrace();
			return ok("-1");
		}

	}


	public static Result addSocialComment(){
		final Long articleId = Long.parseLong(request().body().asFormUrlEncoded().get("articleId")[0]);
		final String name = request().body().asFormUrlEncoded().get("name")[0];
		final String email = request().body().asFormUrlEncoded().get("email")[0];
		final String message = request().body().asFormUrlEncoded().get("message")[0];
		final String type = request().body().asFormUrlEncoded().get("type")[0];

		final List<SocialUser> socialUserList = SocialUser.find.where().eq("email", email.trim().toLowerCase()).findList();
		SocialUser socialUser;
		if(socialUserList.size() == 0){
			socialUser = new SocialUser();
			socialUser.name = name;
			socialUser.email = email.trim().toLowerCase();
			socialUser.firstLogInVia = BlogCommentatorType.valueOf(type);
			socialUser.save();
		}
		else{
			socialUser = socialUserList.get(0);
		}

		try{
			final Article article = Article.find.byId(articleId);
			final BlogComment comment = new BlogComment();
			comment.socialBy = socialUser;
			comment.date = new Date();
			comment.message = message;
			comment.blogCommentatorType = BlogCommentatorType.valueOf(type);
			article.commentList.add(comment);
			article.update();
			return ok(comment.id+"");
		}
		catch(final Exception e){
			Logger.error("Error while saving AppUser's Reply.");
			e.printStackTrace();
			return ok("-1");
		}

	}


	public static Result addSocialCommentReply(){
		final Long articleId = Long.parseLong(request().body().asFormUrlEncoded().get("articleId")[0]);
		final Long commentId = Long.parseLong(request().body().asFormUrlEncoded().get("commentId")[0]);
		final String name = request().body().asFormUrlEncoded().get("name")[0];
		final String email = request().body().asFormUrlEncoded().get("email")[0];
		final String message = request().body().asFormUrlEncoded().get("message")[0];
		final String type = request().body().asFormUrlEncoded().get("type")[0];

		final List<SocialUser> socialUserList = SocialUser.find.where().eq("email", email.trim().toLowerCase()).findList();
		SocialUser socialUser;
		if(socialUserList.size() == 0){
			socialUser = new SocialUser();
			socialUser.name = name;
			socialUser.email = email.trim().toLowerCase();
			socialUser.firstLogInVia = BlogCommentatorType.valueOf(type);
			socialUser.save();
		}
		else{
			socialUser = socialUserList.get(0);
		}
		try{
			final Article article = Article.find.byId(articleId);
			final BlogComment comment = BlogComment.find.byId(commentId);
			final BlogCommentReply reply = new BlogCommentReply();
			reply.socialBy = socialUser;
			reply.date = new Date();
			reply.message = message;
			reply.blogCommentatorType = BlogCommentatorType.valueOf(type);
			article.commentList.remove(comment);
			comment.replyList.add(reply);
			article.commentList.add(comment);
			article.update();
			return ok(reply.id+"");
		}
		catch(final Exception e){
			e.printStackTrace();
			Logger.error("Error while saving SocialUser's Reply.");
			return ok("-1");
		}
	}

}

/*****

THIS IS AN AUTO GENERATED CODE
PLEASE DO NOT MODIFY IT BY HAND

 *****/
package models.doctor;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import models.AppUser;
import models.BaseEntity;
import beans.QuestionAndAnswerBean;


@SuppressWarnings("serial")
@Entity
public class QuestionAndAnswer extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;


	@Column(columnDefinition="TEXT")
	public String question;

	@Column(columnDefinition="TEXT")
	public String answer;

	public Date questionDate;

	public Date answerDate;

	@OneToOne
	public AppUser questionBy;

	@OneToOne
	public AppUser answerBy;

	public static Finder<Long,QuestionAndAnswer> find = new Finder<Long, QuestionAndAnswer>(Long.class, QuestionAndAnswer.class);

	public static Map<String, String> options(){
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final QuestionAndAnswer qa: QuestionAndAnswer.find.all()) {
			vals.put(qa.id+"", qa.question);
		}
		return vals;
	}

	public QuestionAndAnswerBean toBean(){

		final QuestionAndAnswerBean qaBean = new QuestionAndAnswerBean();
		qaBean.id = this.id;

		if(this.question != null) {
			qaBean.question= this.question;
		}

		if(this.answer != null) {
			qaBean.answer= this.answer;
		}


		if(this.questionDate != null) {
			qaBean.questionDate= this.questionDate;
		}

		if(this.answerDate != null) {
			qaBean.answerDate= this.answerDate;
		}


		if(this.questionBy != null) {
			qaBean.questionBy= this.questionBy.id;
		}

		if(this.answerBy != null) {
			qaBean.answerBy= this.answerBy.id;
		}

		return qaBean;
	}



}


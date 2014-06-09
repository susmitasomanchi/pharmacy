
/*****

THIS IS AN AUTO GENERATED CODE
PLEASE DO NOT MODIFY IT BY HAND

 *****/
package beans;

import java.io.Serializable;
import java.util.Date;

import models.AppUser;
import models.QuestionAndAnswer;

@SuppressWarnings("serial")
public class QuestionAndAnswerBean implements Serializable {

	public Long id;


	public String question;

	public String answer;

	public Date questionDate;

	public Date answerDate;

	public Long questionBy;

	public Long answerBy;

	public QuestionAndAnswer toEntity(){

		final QuestionAndAnswer qa = new QuestionAndAnswer();
		qa.id = this.id;

		if(this.question != null) {
			qa.question= this.question;
		}

		if(this.answer != null) {
			qa.answer= this.answer;
		}

		if(this.questionDate != null) {
			qa.questionDate= this.questionDate;
		}

		if(this.answerDate != null) {
			qa.answerDate= this.answerDate;
		}

		if(this.questionBy != null) {
			qa.questionBy= AppUser.find.byId(this.questionBy);
		}

		if(this.answerBy != null) {
			qa.answerBy= AppUser.find.byId(this.answerBy);
		}

		return qa;

	}

}


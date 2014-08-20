import static play.mvc.Results.internalServerError;
import static play.mvc.Results.notFound;

import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

import play.Application;
import play.GlobalSettings;
import play.libs.Akka;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.Http.RequestHeader;
import play.mvc.SimpleResult;
import scala.concurrent.duration.Duration;
import utils.EmailService;
import actor.MyActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import controllers.LoginController;


public class Global extends GlobalSettings {


	/**
	 *	Rendering 500 error page on error
	 */
	@Override
	public Promise<SimpleResult> onError(final RequestHeader request, final Throwable t) {
		final StringBuilder sb = new StringBuilder("");
		sb.append("<html>");
		sb.append("<body>");
		sb.append("<p>");
		sb.append("URL: ");sb.append(request.uri());
		sb.append("</p>");
		sb.append("<p>");
		sb.append("LoggedInAppUserId: ");
		if(LoginController.isLoggedIn()){
			sb.append(LoginController.getLoggedInUser().id);
		}
		else{
			sb.append("Public User");
		}
		sb.append("</p>");
		sb.append("<p>");
		sb.append("IP: ");sb.append(request.remoteAddress());
		sb.append("</p>");
		sb.append("<p>");sb.append(t.getMessage());sb.append("</p>");
		sb.append("<p>");
		for (final StackTraceElement e : t.getStackTrace()) {
			sb.append(e.toString());sb.append("<br>");
		}
		sb.append("</p>");
		sb.append("</body>");
		sb.append("</html>");
		// Async Execution
		Promise.promise(new Function0<Integer>() {
			@Override
			public Integer apply() {
				/*if(!EmailService.sendSimpleHtmlEMail("admin@mednetwork.in", "Production Error 500: "+t.getMessage(), sb.toString())){
					return 1;
				}*/
				return 0;
			}
		});
		// End of async
		return Promise.<SimpleResult>pure(internalServerError(
				views.html.page500.render()
				));
	}


	/**
	 *	Rendering 404 page on not found
	 */
	@Override
	public Promise<SimpleResult> onHandlerNotFound(final RequestHeader request) {
		return Promise.<SimpleResult>pure(notFound(
				views.html.page404.render()
				));
	}


	/**
	 *	Rendering 500 on bad request too
	 */
	@Override
	public Promise<SimpleResult> onBadRequest(final RequestHeader request, final String error) {
		return Promise.<SimpleResult>pure(internalServerError(
				views.html.page500.render()
				));
	}



	/**
	 * @author: Mitesh
	 *	 For testing
	 */
	/*
	@Override
	public void onStart(final Application app) {
		final ActorRef myActor = Akka.system().actorOf(new Props(MyActor.class));

		Akka.system().scheduler().schedule(
				Duration.create(0, TimeUnit.MILLISECONDS), //Initial delay 0 milliseconds
				Duration.create(1, TimeUnit.SECONDS),     //Frequency 30 minutes
				myActor,
				"tick",
				Akka.system().dispatcher(),
				null
				);
	}
	 */


	@Override
	public void onStart(final Application app) {
		final ActorRef myActor = Akka.system().actorOf(new Props(MyActor.class));

		Akka.system().scheduler().schedule(
				Duration.create(nextExecutionInSeconds(5, 0), TimeUnit.SECONDS),
				Duration.create(24, TimeUnit.HOURS),
				myActor,
				"tick",
				Akka.system().dispatcher(),
				null
				);
	}

	public static int nextExecutionInSeconds(final int hour, final int minute){
		return Seconds.secondsBetween(
				new DateTime(),
				nextExecution(hour, minute)
				).getSeconds();
	}

	public static DateTime nextExecution(final int hour, final int minute){
		final DateTime next = new DateTime()
		.withHourOfDay(hour)
		.withMinuteOfHour(minute)
		.withSecondOfMinute(0)
		.withMillisOfSecond(0);

		return (next.isBeforeNow())
				? next.plusHours(24)
						: next;
	}


}

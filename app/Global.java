import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

import play.Application;
import play.GlobalSettings;
import play.libs.Akka;
import scala.concurrent.duration.Duration;
import actor.MyActor;
import akka.actor.ActorRef;
import akka.actor.Props;


public class Global extends GlobalSettings {


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

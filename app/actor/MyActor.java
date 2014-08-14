package actor;

import akka.actor.UntypedActor;
import controllers.CronController;





public class MyActor extends UntypedActor  {

	@Override
	public void onReceive(final Object message) throws Exception {
		// TODO Auto-generated method stub
		//final Promise<WS.Response> homePage = WS.url("http://localhost:9001/cron/deleteAppt	").get();
		if (message.equals("tick")) {
			CronController.deleteOutdatedAppointments();
			CronController.createNextDayAppointment();
		} else {
			this.unhandled(message);
		}
	}

}


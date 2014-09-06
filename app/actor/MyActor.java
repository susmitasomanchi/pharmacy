package actor;

import play.libs.F.Function0;
import play.libs.F.Promise;
import utils.EmailService;
import akka.actor.UntypedActor;
import controllers.CronController;





public class MyActor extends UntypedActor  {

	@Override
	public void onReceive(final Object message) throws Exception {
		// TODO Auto-generated method stub
		//final Promise<WS.Response> homePage = WS.url("http://localhost:9001/cron/deleteAppt	").get();
		if (message.equals("tick")) {
			// Async Execution
			Promise.promise(new Function0<Integer>() {
				//@Override
				@Override
				public Integer apply() {
					int result = 0;
					if(!EmailService.sendSimpleHtmlEMail("admin@mednetwork.in", "Cron update", "<html>Appointment Cron has been started</html>")){
						result=1;
					}

					return result;
				}
			});
			// End of async

			CronController.deleteOutdatedAppointments();
			CronController.createNextDayAppointment();
			// Async Execution
			Promise.promise(new Function0<Integer>() {
				//@Override
				@Override
				public Integer apply() {
					int result = 0;
					if(!EmailService.sendSimpleHtmlEMail("admin@mednetwork.in", "Cron update", "<html>Appointment Cron has ended</html>")){
						result=1;
					}

					return result;
				}
			});
			// End of async

		} else {
			this.unhandled(message);
		}
	}

}


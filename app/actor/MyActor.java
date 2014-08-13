package actor;

import play.Logger;
import akka.actor.UntypedActor;





public class MyActor extends UntypedActor  {

	@Override
	public void onReceive(final Object arg0) throws Exception {
		// TODO Auto-generated method stub
		//final Promise<WS.Response> homePage = WS.url("http://localhost:9001/cron/deleteAppt	").get();
		Logger.info("MyActor executed");
	}

}


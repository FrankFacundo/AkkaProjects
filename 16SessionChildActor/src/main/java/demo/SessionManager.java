package demo;

import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.event.Logging;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public class SessionManager extends AbstractActor{
	private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);
	private ActorRef session;

    public SessionManager() {
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(SessionManager.class, () -> {
			return new SessionManager();
		});
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(MessageType1.class, this::receiveFunction)
			.build();
	  }

	public void receiveFunction(MessageType1 m){
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
		if (m.data.equals("create")){
			final ActorRef session1 = getContext().system().actorOf(Session.createActor(), "session1");
			this.session = session1;
			MessageType3 msg = new MessageType3(session1);
			getSender().tell(msg, getSelf());
		}

		else if (m.data.equals("uncreate")){
		
			this.session.tell(akka.actor.Kill.getInstance(), ActorRef.noSender());
			log.info("Session stopped.");
		}
	}
}
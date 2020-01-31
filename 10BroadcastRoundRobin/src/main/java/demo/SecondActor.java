package demo;

import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.event.Logging;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public class SecondActor extends AbstractActor{
	private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);
	private ActorRef actorRef;

    public SecondActor(ActorRef actorRef) {
		this.actorRef = actorRef;
		log.info("I'm linked to actor reference {}", this.actorRef.path().name());
	}

	// Static function creating actor
	public static Props createActor(ActorRef actorRef) {
		return Props.create(SecondActor.class, () -> {
			return new SecondActor(actorRef);
		});
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(MyMessage.class, this::receiveFunction)
			.build();
	  }

	public void receiveFunction(MyMessage m){
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
	}
}

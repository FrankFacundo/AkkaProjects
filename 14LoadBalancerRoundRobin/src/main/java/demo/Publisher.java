package demo;

import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.event.Logging;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import java.util.*;

public class Publisher extends AbstractActor{
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);
	private ActorRef actorRef;
	HashMap<String, ActorRef> topics;


    public Publisher(ActorRef actorRef) {
		this.actorRef = actorRef;
	}

	// Static function creating actor
	public static Props createActor(ActorRef actorRef) {
		return Props.create(Publisher.class, () -> {
			return new Publisher(actorRef);
		});
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(MessageType1.class, this::receiveFunction)
			.build();
	  }

	public void receiveFunction(MessageType1 m){

	}
}


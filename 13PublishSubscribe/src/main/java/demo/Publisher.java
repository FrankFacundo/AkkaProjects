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


    public Publisher(HashMap<String, ActorRef> topics) {
		this.topics = topics;
	}

	// Static function creating actor
	public static Props createActor(HashMap<String, ActorRef> topics) {
		return Props.create(Publisher.class, () -> {
			return new Publisher(topics);
		});
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(MessageType1.class, this::receiveFunction)
			.build();
	  }

	public void receiveFunction(MessageType1 m){
		MessageType1 myMsg = new MessageType1(m.data);
        (this.actorRef).tell(myMsg, getSelf());
	}
}


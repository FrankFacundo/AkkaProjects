package demo;

import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.event.Logging;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import java.util.*;

public class Session extends AbstractActor{
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);

    public Session() {
		log.info("[" + getSelf().path().name() +"] is created.");
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Session.class, () -> {
			return new Session();
		});
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
            .match(MessageType2.class, this::receiveFunction)
            .matchEquals("done", m -> getContext().stop(getSelf()))
			.build();
	  }

	public void receiveFunction(MessageType2 m){
		log.info("Session received : [" + m.data + "] from : [" + getSender().path().name() + "]");
		MessageType2 msg1 = new MessageType2("Second message");
		if ( !(m.data.equals("Third message")) ){
			getSender().tell(msg1, getSelf());
		}
	}
}


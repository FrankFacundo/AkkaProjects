package demo;

import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.event.Logging;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import java.util.*;

public class Actors extends AbstractActor{
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);

    public Actors() {
		log.info("Path: "+getSelf().path());
		log.info("Name: "+getSelf().path().name());
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Actors.class, () -> {
			return new Actors();
		});
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
            .match(Message.class, this::receiveFunction)
            .matchEquals("done", m -> getContext().stop(getSelf()))
			.build();
	  }

	public void receiveFunction(Message m){
        log.info(m.data + " from "+ getSender().path().name() );
	}
}


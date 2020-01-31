package demo;

import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.event.Logging;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import java.util.*;

public class FirstActor extends AbstractActor{
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);

    public FirstActor() {
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(FirstActor.class, () -> {
			return new FirstActor();
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
        log.info(m.data);
	}
}


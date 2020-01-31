package demo;

import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.event.Logging;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorIdentity;
import akka.actor.ActorSelection;
import akka.actor.Identify;
import java.util.*;

public class FirstActor extends AbstractActor{
	private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);
	private int actorIndex;

    public FirstActor() {
		actorIndex = 1;
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
		log.info("[" + getSelf().path().name() + "] received " + m.data + " from : [" + getSender().path().name() +"]");
		final ActorRef session = getContext().system().actorOf(Actors.createActor(), "actor" + actorIndex );
		actorIndex = actorIndex + 1;

		String name = new String("actor" + (actorIndex-1) );
		ActorSelection selection = getContext().actorSelection("/user/actor*");
		Message msg = new Message("new message");
		selection.tell(msg, getSelf());		

	}
}


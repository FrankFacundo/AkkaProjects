package demo;

import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.event.Logging;

import java.util.Set;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

import java.util.Arrays;
import java.util.*;

public class ActorType2 extends AbstractActor{
	private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);
	HashMap<String, ActorRef> topics;

    public ActorType2(HashMap<String, ActorRef> topics ) {
		this.topics = topics;
	}

	// Static function creating actor
	public static Props createActor(HashMap<String, ActorRef> topics) {
		return Props.create(ActorType2.class, () -> {
			return new ActorType2(topics);
		});
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(MessageType1.class, this::receiveFunction)
			.build();
	  }

	public void receiveFunction(MessageType1 m){
		log.info("["+getSelf().path().name()+"] received message from "+ getSender().path().name() +" with data: ["+m.data+"]");
	}
}

package demo;

import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.event.Logging;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.*;


public class Topic extends AbstractActor{
	private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);
	private HashMap<String, ActorRef> subscribers = new HashMap<>();
	private Set waitingList = new HashSet<>();


	public Topic() {
    }

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Topic.class, () -> {
			return new Topic();
		});
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(MessageType1.class, this::receiveFunctionMsg1)
			.match(MessageType3.class, this::receiveFunctionMsg3)
			.build();
	  }
	
	public void receiveFunctionMsg3(MessageType3 m){
		if (m.subscribed == true) {
			subscribers.put(getSender().path().name(), getSender());
			log.info(getSender().path().name() + " subscribed to " + getSelf().path().name());
		}
		
		else if (m.subscribed == false){
			subscribers.remove(getSender().path().name());
			log.info("[" + getSender().path().name() + "] unsubscribed of " + getSelf().path().name());
		}


	}

	public void receiveFunctionMsg1(MessageType1 m){
		log.info("Message received from : " + getSender().path().name() + " with message : " + m.data);
		for(ActorRef actor : subscribers.values()) {
			actor.tell(m, getSelf());
			
		}

	}

	
}

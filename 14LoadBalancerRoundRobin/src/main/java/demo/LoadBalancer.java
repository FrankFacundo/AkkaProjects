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


public class LoadBalancer extends AbstractActor{
	private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);
	private ArrayList<ActorRef> subscribers = new ArrayList<>();
	private int max = 0;
	private int i = 0;


	public LoadBalancer() {
    }

	// Static function creating actor
	public static Props createActor() {
		return Props.create(LoadBalancer.class, () -> {
			return new LoadBalancer();
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
			subscribers.add(getSender());
			max = max + 1;
			log.info("[" + getSender().path().name() + "] joined to " + getSelf().path().name());
		}
		
		else if (m.subscribed == false){
			subscribers.remove(getSender());
			max = max - 1;
			i = 0;
			log.info("[" + getSender().path().name() + "] unjoined of " + getSelf().path().name());

		}

	}

	public void receiveFunctionMsg1(MessageType1 m){
		subscribers.get(i).tell(m, getSelf());
		i = i + 1;
		if (i == max){
			i = 0;
		}
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
	}

	
}

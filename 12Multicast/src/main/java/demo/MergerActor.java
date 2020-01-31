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


public class MergerActor extends AbstractActor{
	private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);
	private HashMap<String, ActorRef> receivers;
	private HashMap<String, HashMap<String, ActorRef> > groups = new HashMap<String, HashMap<String, ActorRef> >();
	private Set waitingList = new HashSet<>();


	public MergerActor(HashMap<String, ActorRef> receivers) {
        this.receivers = receivers;
    }

	// Static function creating actor
	public static Props createActor(HashMap<String, ActorRef> receivers) {
		return Props.create(MergerActor.class, () -> {
			return new MergerActor(receivers);
		});
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(MessageType2.class, this::receiveFunctionMsg2)
			.match(MessageType3.class, this::receiveFunctionMsg3)
			.build();
	  }
	
	public void receiveFunctionMsg3(MessageType3 m){
		
		HashMap<String, ActorRef> groupLambda = groups.get(m.group);
		log.info("Sending : ["+ m.data + "] to [" + m.group + "]") ;

		for(ActorRef actor : groupLambda.values()) {
			MessageType1 msg = new MessageType1(m.data);
			actor.forward(msg,getContext());

		}
		
	}

	public void receiveFunctionMsg2(MessageType2 m){
		this.groups.put(m.data, m.subscribers);
		log.info("["+m.data+"] is joined to multicaster");
	}

	
}

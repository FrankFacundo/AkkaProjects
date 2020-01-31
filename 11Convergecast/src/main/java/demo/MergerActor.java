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


public class MergerActor extends AbstractActor{
	private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);
	private HashMap<String, ActorRef> subscribers = new HashMap<>();
	private ActorRef actorRef;
	private Set waitingList = new HashSet<>();

    public MergerActor(ActorRef actorRef) {
		this.actorRef = actorRef;
	}

	// Static function creating actor
	public static Props createActor(ActorRef actorRef) {
		return Props.create(MergerActor.class, () -> {
			return new MergerActor(actorRef);
		});
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(MyMessage.class, this::receiveFunction)
			.build();
	  }

	public void receiveFunction(MyMessage m){
		
		if (m.data == "join")
		{
			(this.subscribers).put(m.actor.path().name(), m.actor);
			log.info("["+ getSender().path().name() +"] joined ["+getSelf().path().name()+"]");
		}

		else if (m.data == "unjoin")
		{
			(this.subscribers).remove(m.actor.path().name());
			log.info("["+ getSender().path().name() +"] unjoined ["+getSelf().path().name()+"]");
		}

		else
		{
			this.waitingList.add(m.actor.path().name());
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
			
			Set keys = this.subscribers.keySet();
			
			if (keys.equals(waitingList)){
				MessageType2 msgToD = new MessageType2(m.data,this.subscribers);
				this.actorRef.forward(msgToD,getContext());
				this.waitingList.clear();
			}

		}
	}
}

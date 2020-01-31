package demo;

import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.event.Logging;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;

import java.util.ArrayList;
import java.util.*;
import java.util.Iterator;


public class TransmitterActor extends AbstractActor{
	private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);
	private ArrayList<ActorRef> subscribers = new ArrayList<ActorRef>();

    public TransmitterActor() {
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(TransmitterActor.class, () -> {
			return new TransmitterActor();
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
			(this.subscribers).add(m.actor);
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] to be: ["+m.data+"]");
		}

		else
		{
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
			//(m.actor).forward(m,getContext());

			for(ActorRef element : this.subscribers) {
				element.forward(m,getContext()); 
			}

		}
	}
}

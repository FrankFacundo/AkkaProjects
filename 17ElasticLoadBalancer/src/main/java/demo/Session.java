package demo;

import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.event.Logging;

import java.util.Set;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

import java.util.Arrays;
import java.util.*;

public class Session extends AbstractActor{
	private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);
	private ActorRef actorRef;
	private int numberOfTasks = 0;

    public Session(ActorRef actorRef ) {
		this.actorRef = actorRef;	
		log.info("New session : " + getSelf().path().name());
	}

	// Static function creating actor
	public static Props createActor(ActorRef actorRef) {
		return Props.create(Session.class, () -> {
			return new Session(actorRef);
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

		try {
			wait(1000);
			getSender().tell(new MessageType3(true), getSelf());
        }catch (InterruptedException e){
            e.printStackTrace();
        }		
	}

	public static void wait(int ms) throws InterruptedException{
		Thread.sleep(ms);
	}
}

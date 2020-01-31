package demo;

import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.event.Logging;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public class FirstActor extends AbstractActor{
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);
    private ActorRef actorRef;

    public FirstActor(ActorRef actorRef) {
        this.actorRef = actorRef;
        log.info("I was linked to actor reference {}", this.actorRef.path().name());
	}

	// Static function creating actor
	public static Props createActor(ActorRef actorRef) {
		return Props.create(FirstActor.class, () -> {
			return new FirstActor(actorRef);
		});
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(MyMessage.class, this::receiveFunction)
			.build();
	  }

	public void receiveFunction(MyMessage m){
        log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
        (this.actorRef).tell(m, getSelf());
	}
}


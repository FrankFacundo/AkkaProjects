package demo;

import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.event.Logging;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public class ActorType1 extends AbstractActor{
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);
    private ActorRef actorRef;

    public ActorType1(ActorRef actorRef) {
        this.actorRef = actorRef;
	}

	// Static function creating actor
	public static Props createActor(ActorRef actorRef) {
		return Props.create(ActorType1.class, () -> {
			return new ActorType1(actorRef);
		});
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(MessageType1.class, this::receiveFunction)
			.build();
	  }

	public void receiveFunction(MessageType1 m){
		MessageType1 myMsg = new MessageType1(m.data);
        (this.actorRef).tell(myMsg, getSelf());
	}
}


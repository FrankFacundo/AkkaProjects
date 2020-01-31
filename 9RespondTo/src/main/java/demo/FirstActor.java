package demo;

import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.event.Logging;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;

import akka.util.Timeout;
import java.util.concurrent.TimeUnit ;
import java.time.Duration;

import static akka.pattern.PatternsCS.ask;
import static akka.pattern.PatternsCS.pipe;

import java.util.concurrent.CompletableFuture;


public class FirstActor extends AbstractActor{
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);
	private final ActorRef actorRefB;
	private final ActorRef actorRefC;

    public FirstActor(final ActorRef actorRefB, final ActorRef actorRefC) {
		this.actorRefB = actorRefB;
		this.actorRefC = actorRefC;
		log.info("I am linked to actor reference {}", this.actorRefB.path().name());
		log.info("I am linked to actor reference {}", this.actorRefC.path().name());
	}

	// Static function creating actor
	public static Props createActor(final ActorRef actorRefB, final ActorRef actorRefC) {
		return Props.create(FirstActor.class, () -> {
			return new FirstActor(actorRefB, actorRefC);
		});
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(String.class, this::receiveMain)
			//.match(MyMessage.class, this::receiveFunction)
			.match(MyMessage2.class, this::receiveFunctionB1)
			.build();
	  }

	public void receiveMain(String s){
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+s+"]");
		MyMessage mymessage = new MyMessage(this.actorRefC, "First Message");
		(this.actorRefB).tell(mymessage, getSelf());
	}

	public void receiveFunctionB1(MyMessage2 m){
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
	}
}


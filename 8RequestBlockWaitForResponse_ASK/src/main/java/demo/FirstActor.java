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
    private final ActorRef actorRef;

    public FirstActor(final ActorRef actorRef) {
        this.actorRef = actorRef;
        log.info("I was linked to actor reference {}", this.actorRef.path().name());
	}

	// Static function creating actor
	public static Props createActor(final ActorRef actorRef) {
		return Props.create(FirstActor.class, () -> {
			return new FirstActor(actorRef);
		});
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(MyMessage.class, this::receiveFunction)
			.match(MyMessage2.class, this::receiveFunctionB1)
			.build();
	  }

	public void receiveFunction(final MyMessage m){
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
		
		final MyMessage2 mymessage1 = new MyMessage2("First Message");
		final MyMessage2 mymessage2 = new MyMessage2("Second Message");
		
		Timeout t = Timeout.create(Duration.ofSeconds(5));
		try {
			log.info("["+getSelf().path().name()+"] sends message to ["+ this.actorRef.path().name() +"] with data: ["+mymessage1.data+"]");
			final CompletableFuture<Object> future = ask(this.actorRef, mymessage1, t).toCompletableFuture();
			final MyMessage2 result = (MyMessage2)future.get();
			log.info("["+getSelf().path().name()+"] received message from ["+ this.actorRef.path().name() +"] with data: ["+result.data+"]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
		try {
			log.info("["+getSelf().path().name()+"] sends message to ["+ this.actorRef.path().name() +"] with data: ["+mymessage2.data+"]");
			final CompletableFuture<Object> future = ask(this.actorRef, mymessage2, t).toCompletableFuture();
			final MyMessage2 result = (MyMessage2)future.get();
			log.info("["+getSelf().path().name()+"] received message from ["+ this.actorRef.path().name() +"] with data: ["+result.data+"]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void receiveFunctionB1(MyMessage2 m){
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
	}
}


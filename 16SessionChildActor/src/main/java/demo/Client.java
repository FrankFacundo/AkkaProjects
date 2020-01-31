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


public class Client extends AbstractActor{
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);
	private final ActorRef actorRefB;
	private ActorRef session;

    public Client(final ActorRef actorRefB) {
		this.actorRefB = actorRefB;
		log.info("I am linked to actor reference {}", this.actorRefB.path().name());
	}

	// Static function creating actor
	public static Props createActor(final ActorRef actorRefB) {
		return Props.create(Client.class, () -> {
			return new Client(actorRefB);
		});
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(MessageType2.class, this::receiveFunction)
			.match(MessageType3.class, this::receiveFunctionB1)
			.build();
	  }

	public void receiveMain(String s){
	}

	public void receiveFunction(MessageType2 m){
		log.info("Client received : [" + m.data + "] from : [" + getSender().path().name() + "]");
		MessageType2 msg1 = new MessageType2("Third message");
		this.session.tell(msg1, getSelf());
	}

	public void receiveFunctionB1(MessageType3 m){
		this.session = m.actor;
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with Actor Reference : ["+m.actor.path().name()+"]");

		MessageType2 msg1 = new MessageType2("First message");
		this.session.tell(msg1, getSelf());

	}
}

package demo;

import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.event.Logging;
import akka.actor.AbstractActor;

public class ThirdActor extends AbstractActor{
	private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);

    public ThirdActor() {
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(ThirdActor.class, () -> {
			return new ThirdActor();
		});
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(MyMessage2.class, this::receiveFunction)
			.build();
	  }

	public void receiveFunction(MyMessage2 m){
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
	}
}

package demo;

import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.event.Logging;
import akka.actor.AbstractActor;

public class SecondActor extends AbstractActor{
	private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);

    public SecondActor() {
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(SecondActor.class, () -> {
			return new SecondActor();
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
		MyMessage2 return1 = new MyMessage2(m.data);
		getSender().tell(return1, getSelf());
	}
}

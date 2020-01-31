package demo;

import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.event.Logging;
import akka.actor.AbstractActor;

public class TransmitterActor extends AbstractActor{
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);

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
        log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
        (m.B1).forward(m,getContext());
	}
}

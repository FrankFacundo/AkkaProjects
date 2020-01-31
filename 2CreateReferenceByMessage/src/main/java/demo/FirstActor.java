package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class FirstActor extends UntypedAbstractActor{

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);
    private ActorRef actorRef;

    public FirstActor() {}

    public static Props createActor(ActorRef actorRef) {
        return Props.create(FirstActor.class, () -> {
            return new FirstActor(actorRef);
        });
    }

    @Override
    public void onReceive (Object message) throws Throwable{
        if (message instanceof ActorRef){
            this.actorRef = (ActorRef) message;
            log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"]");
            log.info("Actor reference updated ! New reference is: {}", this.actorRef);
        }
    }
}

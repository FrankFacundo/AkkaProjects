package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;

public class SecondActor extends UntypedAbstractActor {

    public SecondActor() {
    }

    public static Props createActor() {
        return Props.create(SecondActor.class, () -> {
            return new SecondActor();
        });
    }

    @Override
    public void onReceive(Object message) throws Throwable {

    }
}
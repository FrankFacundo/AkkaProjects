package demo;
import akka.actor.ActorRef;

public class MyMessage {
    public final String data;
    public final ActorRef actorRef;

    public MyMessage(ActorRef actorRef, String data) {
        this.actorRef = actorRef;
        this.data = data;
    }
}
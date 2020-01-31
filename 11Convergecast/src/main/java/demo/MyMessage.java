package demo;
import akka.actor.ActorRef;

public class MyMessage {
    public final String data;
    public final ActorRef actor;

    public MyMessage(String data, ActorRef actor) {
        this.data = data;
        this.actor = actor;
    }
}
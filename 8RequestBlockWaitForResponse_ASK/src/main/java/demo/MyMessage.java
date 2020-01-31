package demo;
import akka.actor.ActorRef;

public class MyMessage {
    public final String data;

    public MyMessage(String data) {
        this.data = data;
    }
}
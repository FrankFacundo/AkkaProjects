package demo;
import akka.actor.ActorRef;

public class MyMessage {
    public final String data;
    public final ActorRef B1;

    public MyMessage(String data, ActorRef B1) {
        this.data = data;
        this.B1 = B1;
    }
}
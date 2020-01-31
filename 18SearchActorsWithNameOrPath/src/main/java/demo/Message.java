package demo;
import akka.actor.ActorRef;

public class Message {
    public String data;

    public Message(String data) {
        this.data = data;
    }
}
package demo;
import akka.actor.ActorRef;

public class MessageType3 {
    public final String data;
    public final String group;

    public MessageType3(String group, String data) {
        this.data = data;
        this.group = group;
    }
}
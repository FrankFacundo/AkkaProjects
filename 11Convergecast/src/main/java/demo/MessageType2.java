package demo;
import akka.actor.ActorRef;
import java.util.HashMap;

public class MessageType2 {
    public final String data;
    private HashMap<String, ActorRef> subscribers;

    public MessageType2(String data, ActorRef actor) {
        this.data = data;
        this.subscribers = new HashMap<>();
        (this.subscribers).put(actor.path().name(), actor);
    }

    public MessageType2(String data, HashMap<String, ActorRef> subscribers) {
        this.data = data;
        this.subscribers = subscribers;
    }

    public void addSubscriber(ActorRef actor) {
        (this.subscribers).put(actor.path().name(), actor);
    }

    public HashMap<String, ActorRef> getSubscribers() {
        return subscribers;
    }

}
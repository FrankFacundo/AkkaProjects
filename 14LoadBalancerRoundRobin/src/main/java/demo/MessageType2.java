package demo;
import akka.actor.ActorRef;
import java.util.HashMap;
import java.util.Map;

public class MessageType2 {
    public final String data;
    public HashMap<String, ActorRef> subscribers;

    public MessageType2(String data, ActorRef actor) {
        this.data = data;
        this.subscribers = new HashMap<>();
        (this.subscribers).put(actor.path().name(), actor);
    }

    public MessageType2(String data, HashMap<String, ActorRef> subscribers) {
        this.data = data;

        this.subscribers = new HashMap<>();
        for (Map.Entry<String, ActorRef> entry : subscribers.entrySet())
        {
            this.subscribers.put(entry.getKey(),entry.getValue());
        }
    }

    public void addSubscriber(ActorRef actor) {
        (this.subscribers).put(actor.path().name(), actor);
    }

    public HashMap<String, ActorRef> getSubscribers() {
        return subscribers;
    }

}
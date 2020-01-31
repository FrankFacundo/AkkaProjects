package demo;
import akka.actor.ActorRef;

public class MessageType3 {
    public ActorRef actor;

    public MessageType3(ActorRef actor) {
        this.actor = actor;
    }
}
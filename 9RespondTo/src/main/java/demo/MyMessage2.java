package demo;
import akka.actor.ActorRef;

public class MyMessage2 {
    public final String data;

    public MyMessage2(String data) {
        this.data = data;
    }
}
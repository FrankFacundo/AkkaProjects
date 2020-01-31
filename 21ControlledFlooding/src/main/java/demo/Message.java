package demo;
import akka.actor.ActorRef;

public class Message {
    public String data;
    public int [] references;
    public Integer sequenceNumber;

    public Message(String data) {
        this.data = data;
    }

    public void setReferences(int[] refs)
    {
        this.references = refs.clone(); 
    }

    public void setSequenceNumber(int number)
    {
        this.sequenceNumber = number;
    }
}
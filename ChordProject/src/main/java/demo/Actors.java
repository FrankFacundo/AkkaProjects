package demo;

import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.event.Logging;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorIdentity;
import akka.actor.ActorSelection;
import akka.actor.Identify;

import java.util.Arrays;
import java.util.*;
import java.lang.*;

public class Actors extends AbstractActor{
	private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);
	// array of size of all actors
	public int [] references; 
	// array of size of just actors referenced 
	public int [] refs = {};
	public Integer sequenceNumber;
	

    public Actors() {
		//log.info("Address: "+getSelf());
		//log.info("Path: "+getSelf().path());
		log.info("Name: "+getSelf().path().name());
		//log.info( getContext().actorSelection("/user/*").toString() );
		sequenceNumber = null;
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Actors.class, () -> {
			return new Actors();
		});
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(Webpage.class, this::receiveFunction2)
			.match(Message.class, this::receiveFunction)
            .matchEquals("done", m -> getContext().stop(getSelf()))
			.build();
	  }

	public void receiveFunction2(Webpage m){
		log.info(getSelf().path().name() + " received the webpage : " + m.Webpage );
	}
	
	public void receiveFunction(Message m){
		if(m.data.equals("REFERENCES"))
		{
			this.references = m.references.clone(); 
			String msg = Arrays.toString(this.references);
			for (int i = 0; i < this.references.length; i++)
			{
				if(m.references[i] == 1)
				{
					refs = Arrays.copyOf(refs, refs.length + 1);
					refs[refs.length - 1] = i+1;
				}
			}
	
			msg = Arrays.toString(refs);
			log.info("This actor have reference to : " + msg );
		}

		else if (m.data.equals("START"))
		{
			
			if (sequenceNumber != m.sequenceNumber)
			{
				sequenceNumber = m.sequenceNumber;
				for(int i = 0; i < references.length; i++){
					if(references[i] == 1)
					{
						log.info("Received from "+ getSender().path().name() +". Sending message to actor " + (i+1) );
						
						String name = new String("actor" + (i+1) );
						ActorSelection selection = getContext().actorSelection("/user/" + name);
						Message msg1 = new Message("START");
						msg1.setSequenceNumber(m.sequenceNumber);
						selection.tell(msg1, getSelf());
						
					}
	
				}
			}
			else
			{
				log.info("Message received from " + getSender().path().name() + " dropped.");
			}

		}

	}
}


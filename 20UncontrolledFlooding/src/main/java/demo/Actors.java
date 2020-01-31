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
	

    public Actors() {
		log.info("Name: "+getSelf().path().name());
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
            .match(Message.class, this::receiveFunction)
            .matchEquals("done", m -> getContext().stop(getSelf()))
			.build();
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
			
			for(int i = 0; i < references.length; i++){
				if(references[i] == 1)
				{
					String name = new String("actor" + (i+1) );
					ActorSelection selection = getContext().actorSelection("/user/" + name);
					Message msg1 = new Message("START");
					selection.tell(msg1, ActorRef.noSender());
					log.info("Sending message to actor " + (i+1) );
				}

			}
			
		}

	}
}


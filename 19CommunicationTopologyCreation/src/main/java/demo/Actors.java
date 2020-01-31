package demo;

import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.event.Logging;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;

import java.util.Arrays;
import java.util.*;
import java.lang.*;

public class Actors extends AbstractActor{
	private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);
	public int [] references;
	

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
		
		if( (m.data).equals("REFERENCES"))
		{
			this.references = m.references.clone(); 
			String msg = Arrays.toString(this.references);
			int [] refs = {};
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
		

	}
}


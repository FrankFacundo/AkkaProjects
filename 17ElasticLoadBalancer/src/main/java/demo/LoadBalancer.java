package demo;

import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.event.Logging;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.*;


public class LoadBalancer extends AbstractActor{
	private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this);
	private ArrayList<ActorRef> subscribers = new ArrayList<>();
	private int max = 0;
	private int i = 0;
	private int maxNumberOfSessions = 0;
	private int numberCurrentSessions = 0;
	
	private ActorRef [] sessions  = new ActorRef [2];
	private int [] tasksBySession  = new int [2];

	public LoadBalancer(int maxNumberOfSessions) {
		this.maxNumberOfSessions = maxNumberOfSessions;
		log.info("The maximum number of sessions is "+ this.maxNumberOfSessions);
    }

	// Static function creating actor
	public static Props createActor(int maxNumberOfWorkers) {
		return Props.create(LoadBalancer.class, () -> {
			return new LoadBalancer(maxNumberOfWorkers);
		});
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(MessageType1.class, this::receiveFunctionMsg1)
			.match(MessageType3.class, this::receiveFunctionMsg3)
			.build();
	  }
	
	public void receiveFunctionMsg3(MessageType3 m){
		if (m.finished == true) {
			int i = 0;
			Boolean notfoundSession = true;
			while (i < sessions.length && notfoundSession) {
				if( (sessions[i].path().name()).equals(getSender().path().name()) )
				{
					tasksBySession[i] = tasksBySession[i] - 1;
					log.info("Number of tasks by session ["+ sessions[i].path().name() + "]:" + tasksBySession[i]);
					if(tasksBySession[i] == 0)
					{
						log.info(getSender().path().name() +" is stopping.");
						getSender().tell(akka.actor.Kill.getInstance(), ActorRef.noSender());
					}

					notfoundSession = false;
				}
				i++;
			}
		}

	}

	public void receiveFunctionMsg1(MessageType1 m){
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");

		if (numberCurrentSessions < maxNumberOfSessions){
			
			int i = 0;
			Boolean notfoundSession = true;
			while (i < sessions.length && notfoundSession) {
				if (sessions[i]==null){
					final ActorRef session = getContext().system().actorOf(Session.createActor(getSelf()), "Session" + (i+1));
					sessions[i] = session;
					numberCurrentSessions = numberCurrentSessions +1;
					sessions[i].tell(m, getSelf());
					tasksBySession[i] = tasksBySession[i] + 1;
					log.info("Number of tasks by ["+ sessions[i].path().name() + "]: "+tasksBySession[i]);

					notfoundSession = false;

				}
				i++;
			}

		}

		else {
			int i = 0;
			Boolean notfoundSession = true;
			
			while (i < sessions.length && notfoundSession) {
				if (sessions[i] != null){
					sessions[i].tell(m, getSelf());
					tasksBySession[i] = tasksBySession[i] + 1;
					log.info("Number of tasks by session ["+ sessions[i].path().name() + "]: "+tasksBySession[i]);
					notfoundSession = false;

				}
				i++;
			}
		}

		

	}

	
}

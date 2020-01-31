package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.*;

/**
 * @author Frank FACUNDO
 */

public class Multicast {

	public static void main(String[] args){
		System.out.println("Multicast");
		
		final ActorSystem system = ActorSystem.create("system");
		
		//Create childs
		
		final ActorRef rec1 = system.actorOf(ActorType2.createActor(), "rec1");
		final ActorRef rec2 = system.actorOf(ActorType2.createActor(), "rec2");
		final ActorRef rec3 = system.actorOf(ActorType2.createActor(), "rec3");

		HashMap<String, ActorRef> receivers = new HashMap<>();
		receivers.put(rec1.path().name(), rec1);
		receivers.put(rec2.path().name(), rec2);
		receivers.put(rec3.path().name(), rec3);

		final ActorRef M = system.actorOf(MergerActor.createActor(receivers), "Multicaster");
		
		final ActorRef sender = system.actorOf(ActorType1.createActor(M), "Sender");

		// Sending information about groups
		HashMap<String, ActorRef> group1 = new HashMap<>();
		group1.put(rec1.path().name(), rec1);
		group1.put(rec2.path().name(), rec2);
		
		HashMap<String, ActorRef> group2 = new HashMap<>();
		group2.put(rec2.path().name(), rec2);
		group2.put(rec3.path().name(), rec3);
		
		MessageType2 msgGroup1 = new MessageType2("group1", group1);
		MessageType2 msgGroup2 = new MessageType2("group2", group2);
		
		M.tell(msgGroup1, sender);
		M.tell(msgGroup2, sender);
		
		// Sending data

		
		MessageType3 hello_g1 = new MessageType3("group1", "hello");
		MessageType3 hello_g2 = new MessageType3("group2", "world");
		M.tell(hello_g1, sender);
		M.tell(hello_g2, sender);

		try {
            waitBeforeTerminate();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally{
            system.terminate();
        }
        System.out.println("********************");
	}
	
	public static void waitBeforeTerminate() throws InterruptedException{
		Thread.sleep(3000);
	}

	public static void wait(int ms) throws InterruptedException{
		Thread.sleep(ms);
	}

}

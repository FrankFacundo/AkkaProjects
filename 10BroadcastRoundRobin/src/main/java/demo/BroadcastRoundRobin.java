package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @author Frank FACUNDO
 */

public class BroadcastRoundRobin {

	public static void main(String[] args){
		System.out.println("Broadcast RoundRobin :");
		
		final ActorSystem system = ActorSystem.create("system");
		
		//Create childs
		final ActorRef T1 = system.actorOf(TransmitterActor.createActor(), "Broadcaster");
		final ActorRef A1 = system.actorOf(FirstActor.createActor(T1), "A1");
		final ActorRef B1 = system.actorOf(SecondActor.createActor(T1), "B1");
		final ActorRef C1 = system.actorOf(ThirdActor.createActor(T1), "C1");

		//Messages anonyme
		MyMessage msgBtoBroadcast = new MyMessage("join", B1);
		MyMessage msgCtoBroadcast = new MyMessage("join", C1);
		
		T1.tell(msgBtoBroadcast, B1);
		T1.tell(msgCtoBroadcast, C1);

		MyMessage start = new MyMessage("Start!", ActorRef.noSender());


		A1.tell(start, ActorRef.noSender());

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

}

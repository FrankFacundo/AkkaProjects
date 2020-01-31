package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @author Frank FACUNDO
 */

public class TellToAndForget {

	public static void main(String[] args){
		System.out.println("Tell To And Forget :");
		
		final ActorSystem system = ActorSystem.create("system");
		
		//Create childs
		final ActorRef T1 = system.actorOf(TransmitterActor.createActor(), "T1");
		final ActorRef A1 = system.actorOf(FirstActor.createActor(T1), "A1");
		final ActorRef B1 = system.actorOf(SecondActor.createActor(), "B1");

		//Messages anonyme
		MyMessage msg = new MyMessage("hello", B1);
        A1.tell(msg, ActorRef.noSender());

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

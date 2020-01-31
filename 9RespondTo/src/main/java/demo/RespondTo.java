package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @author Frank FACUNDO
 */

public class RespondTo {

	public static void main(String[] args){
		System.out.println("Respond To :");
		
		final ActorSystem system = ActorSystem.create("system");
		
		//Create childs
		final ActorRef C1 = system.actorOf(ThirdActor.createActor(), "C1");
		final ActorRef B1 = system.actorOf(SecondActor.createActor(), "B1");
		final ActorRef A1 = system.actorOf(FirstActor.createActor(B1,C1), "A1");

		//Messages anonyme
        A1.tell("Start!", ActorRef.noSender());

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
		Thread.sleep(7000);
	}

}

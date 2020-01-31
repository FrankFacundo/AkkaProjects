package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @author Frank FACUNDO
 */

public class RequestDontWaitForResponse {

	public static void main(String[] args){
		System.out.println("Request And Dont Wait For Response :");
		
		final ActorSystem system = ActorSystem.create("system");
		
		//Create childs
		final ActorRef B1 = system.actorOf(SecondActor.createActor(), "B1");
		final ActorRef A1 = system.actorOf(FirstActor.createActor(B1), "A1");

		//Messages anonyme
		MyMessage msg = new MyMessage("Start!");
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
		Thread.sleep(7000);
	}

}

package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
//import demo.MyBrokenMessage;
//import demo.MyActor.PrintBrokenMessage;
//import demo.MyActor.PrintCorrectMessage;

public class TellToAndForget {

	public static void main(String[] args){
		System.out.println("Test de Frank");
		
		final ActorSystem system = ActorSystem.create("system");
		
		try {
            waitBeforeTerminate();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally{
            //system.terminate();
        }
        System.out.println("********************");
	}

	public static void waitBeforeTerminate() throws InterruptedException{
		Thread.sleep(3000);
	}

}

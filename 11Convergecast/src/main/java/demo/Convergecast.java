package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @author Frank FACUNDO
 */

public class Convergecast {

	public static void main(String[] args){
		System.out.println("Convergecast");
		
		final ActorSystem system = ActorSystem.create("system");
		
		//Create childs
		final ActorRef D = system.actorOf(ActorType2.createActor(), "D");
		final ActorRef M = system.actorOf(MergerActor.createActor(D), "M");
		final ActorRef A = system.actorOf(ActorType1.createActor(M), "A");
		final ActorRef B = system.actorOf(ActorType1.createActor(M), "B");
		final ActorRef C = system.actorOf(ActorType1.createActor(M), "C");

		//Messages anonyme
		MyMessage msgMergerA = new MyMessage("join", A);
		MyMessage msgMergerB = new MyMessage("join", B);
		MyMessage msgMergerC = new MyMessage("join", C);
		M.tell(msgMergerA, A);
		M.tell(msgMergerB, B);
		M.tell(msgMergerC, C);

		MyMessage msg = new MyMessage("Message 1", ActorRef.noSender());
		A.tell(msg, ActorRef.noSender());
		B.tell(msg, ActorRef.noSender());
		C.tell(msg, ActorRef.noSender());

		try{wait(1000);
		}catch (InterruptedException e){e.printStackTrace();}

		MyMessage msgUnjoin = new MyMessage("unjoin", C);
		M.tell(msgUnjoin, C);

		MyMessage msg2 = new MyMessage("Message 2!", ActorRef.noSender());
		A.tell(msg2, ActorRef.noSender());
		B.tell(msg2, ActorRef.noSender());

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

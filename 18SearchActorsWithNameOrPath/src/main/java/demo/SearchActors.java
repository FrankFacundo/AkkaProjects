package demo;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.LoggingAdapter;
import akka.event.Logging;
import akka.actor.Props;

/**
 * @author Frank FACUNDO
 */

public class SearchActors {
	public static void main(String[] args){
		System.out.println("Search Actors :");
		final ActorSystem system = ActorSystem.create("system");
		
		final ActorRef A = system.actorOf(FirstActor.createActor(), "A");

		Message msg = new Message("CREATE");

		A.tell(msg, ActorRef.noSender());
		A.tell(msg, ActorRef.noSender());

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

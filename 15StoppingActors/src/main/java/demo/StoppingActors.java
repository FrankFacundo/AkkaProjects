package demo;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class StoppingActors {
	public static void main(String[] args){
		System.out.println("Stopping Actors :");
		final ActorSystem system = ActorSystem.create("system");
		
		final ActorRef A1 = system.actorOf(FirstActor.createActor(), "A1");

		Message msg = new Message("Hello");

		A1.tell(msg, ActorRef.noSender());

		//Second way to stop
		A1.tell(akka.actor.Kill.getInstance(), ActorRef.noSender());
		
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

package demo;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.LoggingAdapter;
import akka.event.Logging;
import akka.actor.Props;
import akka.actor.ActorIdentity;
import akka.actor.ActorSelection;
import akka.actor.Identify;

public class UncontrolledFlooding {
	public static void main(String[] args){
		System.out.println("Topology of Uncontrolled Flooding :");

		int numberOfActors = 5;
		int[][] d = { { 0, 1, 1, 0, 0 }, { 0, 0, 0, 1, 0 }, { 0, 0, 0, 1, 0}, { 0, 0, 0, 0, 1}, { 0, 0, 0, 0, 0} };
		//infinite cycle
		//int[][] d = { { 0, 1, 1, 0, 0 }, { 0, 0, 0, 1, 0 }, { 0, 0, 0, 1, 0}, { 0, 0, 0, 0, 1}, { 0, 1, 0, 0, 0} };
		Matrix matrix = new Matrix(d);
		matrix.show();
		
		final ActorSystem system = ActorSystem.create("system");
		final ActorRef A = system.actorOf(FirstActor.createActor(), "A");


		Message msg = new Message("CREATE");
		for(int i = 0; i < numberOfActors; i++){
			A.tell(msg, ActorRef.noSender());	
		}
		
		try {
			wait(1000);
		}catch (InterruptedException e){
			e.printStackTrace();
		}

		
		for(int i = 0; i < numberOfActors; i++){
			
			String name = new String("actor" + (i+1) );
			ActorSelection selection = system.actorSelection("/user/" + name);
			Message msg1 = new Message("REFERENCES");
			int [] row = matrix.getRow(i);
			msg1.setReferences(row);
			selection.tell(msg1, ActorRef.noSender());
		}
		
		try {
			wait(1000);
		}catch (InterruptedException e){
			e.printStackTrace();
		}

		ActorSelection selection = system.actorSelection("/user/actor1");
		selection.tell(new Message("START"), ActorRef.noSender());

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
		Thread.sleep(5000);
	}
	public static void wait(int ms) throws InterruptedException{
		Thread.sleep(ms);
	}
}

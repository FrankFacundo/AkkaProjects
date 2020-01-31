package demo;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class CreateRefConstruction {
	public static void main(String[] args){
		System.out.println("Test de Frank");
		final ActorSystem system = ActorSystem.create("system");
		final ActorSystem A2 = system.actorOf(SecondActor.createActor(), "A2");
		System.out.println("********************");
	}
}

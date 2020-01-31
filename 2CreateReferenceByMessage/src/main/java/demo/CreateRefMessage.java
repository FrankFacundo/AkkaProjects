package demo;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class CreateRefConstruction {
    public static void main(String[] args){
        System.out.println("Test de Frank");
        final ActorSystem system = ActorSystem.create("system");
        final ActorRef A2 = system.actorOf(SecondActor.createActor(), "A2");
        final ActorRef A1 = system.actorOf(FirstActor.createActor(), "A1");

        A1.tell(A2, ActorRef.noSender());

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

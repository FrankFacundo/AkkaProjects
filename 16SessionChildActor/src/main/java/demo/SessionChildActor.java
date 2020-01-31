package demo;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * @author Frank FACUNDO
 */

public class SessionChildActor {
	public static void main(String[] args){
		System.out.println("Session ChildActor");
		final ActorSystem system = ActorSystem.create("system");
		
		final ActorRef sessionManager = system.actorOf(SessionManager.createActor(), "Session_Manager");
		final ActorRef client1 = system.actorOf(Client.createActor(sessionManager), "Client1");

		MessageType1 msg = new MessageType1("create");
		sessionManager.tell(msg, client1);

		try {
            wait(1000);
        }catch (InterruptedException e){e.printStackTrace();}

		MessageType1 msg2 = new MessageType1("uncreate");
		sessionManager.tell(msg2, client1);

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

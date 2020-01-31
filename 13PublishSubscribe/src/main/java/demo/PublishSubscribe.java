package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.*;

public class PublishSubscribe {

	public static void main(String[] args){
		System.out.println("Publish Subscribe :");
		
		final ActorSystem system = ActorSystem.create("system");
		
		//Create childs
		
		final ActorRef topic1 = system.actorOf(Topic.createActor(), "Topic1");
		final ActorRef topic2 = system.actorOf(Topic.createActor(), "Topic2");
		
		HashMap<String, ActorRef> topics = new HashMap<>();
		topics.put(topic1.path().name(), topic1);
		topics.put(topic2.path().name(), topic2);

		final ActorRef a = system.actorOf(ActorType2.createActor(topics), "a");
		final ActorRef b = system.actorOf(ActorType2.createActor(topics), "b");
		final ActorRef c = system.actorOf(ActorType2.createActor(topics), "c");
		
		final ActorRef publisher1 = system.actorOf(Publisher.createActor(topics), "Publisher1");
		final ActorRef publisher2 = system.actorOf(Publisher.createActor(topics), "Publisher2");

		//MessageType3 is for subscription messages
		MessageType3 a_topic1 = new MessageType3(true);
		MessageType3 b_topic1 = new MessageType3(true);
		MessageType3 b_topic2 = new MessageType3(true);
		MessageType3 c_topic2 = new MessageType3(true);

		topic1.tell(a_topic1, a);
		topic1.tell(b_topic1, b);
		topic2.tell(b_topic2, b);
		topic2.tell(c_topic2, c);

		MessageType1 msg1 = new MessageType1("hello");
		topic1.tell(msg1,publisher1);

		MessageType1 msg2 = new MessageType1("world");
		topic2.tell(msg2,publisher2);

		try {
            wait(1000);
        }catch (InterruptedException e){
            e.printStackTrace();}
		
		a_topic1.subscribed = false;
		topic1.tell(a_topic1, a);

		MessageType1 msg3 = new MessageType1("hello2");
		topic1.tell(msg3,publisher1);


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

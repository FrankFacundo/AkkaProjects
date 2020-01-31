package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.*;

/**
 * @author Frank FACUNDO
 */

public class LoadBalancerRoundRobin {

	public static void main(String[] args){
		System.out.println("Load Balancer RoundRobin :");
		
		final ActorSystem system = ActorSystem.create("system");
		
		final ActorRef loadBalancer = system.actorOf(LoadBalancer.createActor(), "LoadBalancer");

		final ActorRef a = system.actorOf(Publisher.createActor(loadBalancer), "a");

		final ActorRef b = system.actorOf(ActorType2.createActor(loadBalancer), "b");
		final ActorRef c = system.actorOf(ActorType2.createActor(loadBalancer), "c");

		MessageType3 b_loadbalancer = new MessageType3(true);
		MessageType3 c_loadbalancer = new MessageType3(true);
		loadBalancer.tell(b_loadbalancer, b);
		loadBalancer.tell(c_loadbalancer, c);
		
		try {
            wait(1000);
        }catch (InterruptedException e){
			e.printStackTrace();}
			
		MessageType1 task1 = new MessageType1("Task 1");
		loadBalancer.tell(task1, a);
		MessageType1 task2 = new MessageType1("Task 2");
		loadBalancer.tell(task2, a);
		MessageType1 task3 = new MessageType1("Task 3");
		loadBalancer.tell(task3, a);

		MessageType3 c_loadbalancer2 = new MessageType3(false);
		loadBalancer.tell(c_loadbalancer2, c);

		MessageType1 task4 = new MessageType1("Task 4");
		loadBalancer.tell(task4, a);


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

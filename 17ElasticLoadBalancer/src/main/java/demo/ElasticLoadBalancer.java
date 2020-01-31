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

public class ElasticLoadBalancer {

	public static void main(String[] args){

		
		System.out.println("Elastic Load Balancer :");
				
		int maxNumberOfSessions = 2;
		final ActorSystem system = ActorSystem.create("system");
		
		final ActorRef loadBalancer = system.actorOf(LoadBalancer.createActor(maxNumberOfSessions), "LoadBalancer");

		final ActorRef a = system.actorOf(Publisher.createActor(loadBalancer), "a");

		MessageType1 task1 = new MessageType1("Task 1");
		loadBalancer.tell(task1, a);
		try {
			wait(100);
        }catch (InterruptedException e){
            e.printStackTrace();
		}	
		
		MessageType1 task2 = new MessageType1("Task 2");
		loadBalancer.tell(task2, a);
		try {
			wait(100);
        }catch (InterruptedException e){
            e.printStackTrace();
		}	
		
		MessageType1 task3 = new MessageType1("Task 3");
		loadBalancer.tell(task3, a);

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

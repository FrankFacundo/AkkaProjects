package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ActorSelection;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author Frank FACUNDO
 * @description Implementation of :
 * "Chord: A Scalable Peer-to-peer Lookup 
 * Protocol for Internet Applications"
 * Algorithm based on :
 * Slides of Amir H. Payberah professor at KTH
 * Source of slides : https://www.kth.se/social/upload/51647996f276545db53654c0/3-chord.pdf
 * Last visite : 31 january 2020
 * I used webpages as files to share.
 * I implemented the encryptation SHA1 as hash.
 * I use real webpages.
 * I implemented the topology of Chord.
 */

public class Chord {
	public static void main(String[] args){
		
		System.out.println("Implementation of :"+
		"\nChord: A Scalable Peer-to-peer Lookup "+
		"Protocol for Internet Applications"+
		"\nAlgorithm based on :"+
		"\nSlides of Amir H. Payberah professor at KTH"+
		"\nSource of slides : https://www.kth.se/social/upload/51647996f276545db53654c0/3-chord.pdf\n");
		
		int identifierSpace = 16;
		int numberOfActors = 5;

		
		DHT_Chord chord = new DHT_Chord(identifierSpace, numberOfActors);
		chord.printActorIdRingMap();
		//chord.printListSuccessors();

		//chord.printFileIdMap();
		chord.printListSuccessorsName();
		int[][] d = chord.getTopologyMatrix();
		Matrix matrix = new Matrix(d);
		System.out.println("\nTopology");
		matrix.show();
		System.out.println();

		final ActorSystem system = ActorSystem.create("system");
		final ActorRef A = system.actorOf(FirstActor.createActor(), "A");

		
		Message msg = new Message("CREATE");
		for(int i = 0; i < numberOfActors; i++){
			A.tell(msg, ActorRef.noSender());	
		}
		
		System.out.println();
		
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
		
		System.out.println();

		try {
			wait(1000);
		}catch (InterruptedException e){
			e.printStackTrace();
		}

		System.out.println("\n\n\n******* CONTROLLED FLOODING *******");
		ActorSelection selection = system.actorSelection("/user/actor1");
		Message messageStart = new Message("START");
		messageStart.setSequenceNumber(0);
		selection.tell(messageStart, ActorRef.noSender());

		try {
			wait(1000);
		}catch (InterruptedException e){
			e.printStackTrace();
		}

		Map<String, Integer> listWebPages = chord.fileIdMap();

		System.out.println("\n\n\n******* Distributed webpages : *******");
        Set<Entry<String, Integer>> setHm = listWebPages.entrySet();
        Iterator<Entry<String, Integer>> it = setHm.iterator();
        while(it.hasNext()){
		   Entry<String, Integer> e = it.next();
		   
		   ActorSelection selection2 = system.actorSelection("/user/actor"+ chord.getActorFromId(e.getValue()).toString());
		   Webpage WebPage = new Webpage(e.getKey());
		   selection2.tell(WebPage, ActorRef.noSender());

        }



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
		Thread.sleep(10000);
	}
	public static void wait(int ms) throws InterruptedException{
		Thread.sleep(ms);
	}
}

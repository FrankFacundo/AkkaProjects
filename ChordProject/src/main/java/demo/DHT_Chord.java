package demo;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import java.nio.file.*;

public class DHT_Chord {
	
	private int identifierSpace;
	private int numberOfActors;
	private ArrayList<Integer> listIdRingActors;
	private Map<Integer, Integer> identifierRingMap;
	private Map<Integer, Integer> listSuccessors;
	private Map<String, Integer> fileIdMap;
	private Map<Integer, Integer> listSuccessorsName;
	private int [][] matrix;
	
	public DHT_Chord (int identifierSpace, int numberOfActors)
	{
		this.identifierSpace = identifierSpace;
		this.numberOfActors = numberOfActors;
		this.listIdRingActors = IdRingActors();
		this.identifierRingMap = ActorIdRingMap();
		this.listSuccessors = listSuccessors();
		this.fileIdMap = fileIdMap();
		this.listSuccessorsName = listSuccessorsName();
		this.matrix = matrix();
	}
	
	public Integer getActorFromId(Integer id)
	{
		
		Map<Integer, Integer> ActorIdtoActor = new HashMap<>();;
		for(int i = 0; i < numberOfActors; i++)
		{
			ActorIdtoActor.put(listIdRingActors.get(i), i+1);
		}

		return ActorIdtoActor.get(id);
	}
	private ArrayList<Integer> IdRingActors()
	{
        ArrayList<Integer> listIdRingActors = new ArrayList<Integer>();
        for (int i=1; i< identifierSpace; i++) {
        	listIdRingActors.add(new Integer(i));
        }
        Collections.shuffle(listIdRingActors);
        return listIdRingActors;
	}
	
	//Integer : Number of actor (it could be an String too), Integer : ID between 0 and "identifierSpace"
	public Map<Integer, Integer> ActorIdRingMap()
	{
        Map<Integer, Integer> identifierRingMap = new HashMap<>();
        for(int i = 0; i < numberOfActors; i++)
        {
        	identifierRingMap.put(i+1,listIdRingActors.get(i));
        }
        return identifierRingMap;
	}
	
	private Map<Integer, Integer> listSuccessors()
	{
		Map<Integer, Integer> listSuccessors = new HashMap<>();
		for(int i = 0; i < numberOfActors; i++)
        {
        	listSuccessors.put(listIdRingActors.get(i),getSuccessor( listIdRingActors.get(i) + 1 ) );
        }
        return listSuccessors;
	}
	
	private Map<Integer, Integer> listSuccessorsName()
	{
		Map<Integer, Integer> listSuccessorsName = new HashMap<>();
		for(int i = 0; i < numberOfActors; i++)
        {
        	int actorName = i;
        	Integer successorActorName = null; 
			int actorId = this.listIdRingActors.get(i);
			int successorActorId = this.listSuccessors.get(actorId);
    		for(int j = 0; j < numberOfActors; j++)
            {
    			if(successorActorId == this.listIdRingActors.get(j))
    			{
    				successorActorName = j;
    			}
            }
    		listSuccessorsName.put(actorName+1,successorActorName+1);
        }
        return listSuccessorsName;
	}
	
    public int[][] matrix () {

    	int[][] data = new int[this.numberOfActors][this.numberOfActors];
        Set<Entry<Integer, Integer>> setHm = this.listSuccessorsName.entrySet();
        Iterator<Entry<Integer, Integer>> it = setHm.iterator();
        while(it.hasNext()){
           Entry<Integer, Integer> e = it.next();
           data[e.getKey()-1][e.getValue()-1] = 1;
        }
    	
    	return data;
    }
    
	
	public void printListSuccessorsName()
	{
        System.out.println("\nActors , Successor Actor : ");
        Set<Entry<Integer, Integer>> setHm = this.listSuccessorsName.entrySet();
        Iterator<Entry<Integer, Integer>> it = setHm.iterator();
        while(it.hasNext()){
           Entry<Integer, Integer> e = it.next();
           System.out.println(e.getKey() + " : " + e.getValue());
		}
		System.out.println();
	}
	
    public void showMatrix() {
		System.out.println("Topology matrix");
		for (int i = 0; i < this.numberOfActors; i++) {
            for (int j = 0; j < this.numberOfActors; j++) 
                System.out.print(this.matrix[i][j]+" ");
            System.out.println();
		}
		
    }
	
	
	public void printListIdRingActors()
	{
        for (int i=0; i< numberOfActors; i++) 
        {
            System.out.println(this.listIdRingActors.get(i));
        }
	}
	
	public void printActorIdRingMap()
	{
        System.out.println("Actors , ActorId : ");
        Set<Entry<Integer, Integer>> setHm = this.identifierRingMap.entrySet();
        Iterator<Entry<Integer, Integer>> it = setHm.iterator();
        while(it.hasNext()){
           Entry<Integer, Integer> e = it.next();
           System.out.println(e.getKey() + " : " + e.getValue());
        }
	}
	
	
	public Integer getSuccessor(int position)
	{
		if(position<=identifierSpace)
        {
			if (position == 16) {
				position = 0;
			}
			
        	int distance = identifierSpace;
        	int temp = identifierSpace;
        	Integer successor = null;
        	for (int i=0; i< numberOfActors; i++) {
                
        		if(position <= listIdRingActors.get(i)) 
        		{
        			temp = listIdRingActors.get(i) - position;
        		}
        		else 
        		{
        			temp = (16-position) + listIdRingActors.get(i);
        		}
        		if (temp < distance)
        		{
        			distance = temp;
        			successor = listIdRingActors.get(i);
        		}
            }
        	return successor;
        }
        
		else
		{
			return null;
		}
	}
	
	public void printListSuccessors()
	{
        System.out.println("\nActorId , ActorId Successor: ");
        Set<Entry<Integer, Integer>> setHm = this.listSuccessors.entrySet();
        Iterator<Entry<Integer, Integer>> it = setHm.iterator();
        while(it.hasNext()){
           Entry<Integer, Integer> e = it.next();
           System.out.println(e.getKey() + " : " + e.getValue());
		}
		System.out.println("\n");
	}
	
	public Map<String, Integer> fileIdMap()
	{
		Map<String, Integer> fileIdMap = new HashMap<>();
		SHA1 encryp = new SHA1();
		
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		//System.out.println("Current relative path is: " + s);
		
		//URL url = DHT_Chord.class.getResource("/src/main/java/demo/Files");
		URL url = DHT_Chord.class.getResource("Files");
		File folder = new File(url.getPath());
		String name = null;
		Integer id = null;
		
		for (final File fileEntry : folder.listFiles()) {
			name = fileEntry.getName();
			try {
				id = getSuccessor(encryp.encryptFile(fileEntry));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			fileIdMap.put(name,id );
			
	    }
		
		return fileIdMap;
	}
	
	public void printFileIdMap()
	{
        System.out.println("\nFile , FileId : ");
        Set<Entry<String, Integer>> setHm = this.fileIdMap.entrySet();
        Iterator<Entry<String, Integer>> it = setHm.iterator();
        while(it.hasNext()){
           Entry<String, Integer> e = it.next();
           System.out.println(e.getKey() + " : " + e.getValue());
        }
	}

	public int[][] getTopologyMatrix() {
		return matrix;
	}
	

	
}
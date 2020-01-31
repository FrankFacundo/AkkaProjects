package demo;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Files {
	
	private ArrayList<String> listFiles;
	private Map<String, Integer> fileIdMap;
	
	public Files()
	{
		generateListFiles();
	}
	
	private void generateListFiles()
	{
		listFiles = new ArrayList<String>();
		URL url = Files.class.getResource("/Files");
		File folder = new File(url.getPath());
		listFilesForFolder(folder);
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	            listFiles.add(fileEntry.getName());
	        }
	    }
		
	}
	
	
	public void listFilesForFolder(final File folder) {
	    
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	            System.out.println(fileEntry.getName());
	        }
	    }
	}
	
	public void listFilesCurrent() {
		URL url = Files.class.getResource("/Files");
		File folder = new File(url.getPath());
		listFilesForFolder(folder);
	}
	
	
	public void listFiles() {
		for(String filename : this.listFiles) {
			System.out.println(filename);
		}
	}
	
	public ArrayList<String> getListFiles(){
		return listFiles;
	}
	

}

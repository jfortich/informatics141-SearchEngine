import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.jsoup.Jsoup;


public class parseHTML {

	public static final String FILE_NAME = "processedHTMLPages.txt";
	static int docID = 0;
	
	public static void processHTMLPage(File page) {
		// Initialize variables 
		String content = "";
		
		try {
			// Reads file and collects content in a String
			// Closes buffered reader when done
	        BufferedReader in = new BufferedReader(new FileReader(page));
	        String str;
	        
	        while ((str = in.readLine()) != null) {
	            content += str;
	        }
	        in.close();
	        
	        // Parses HTML and grabs text from the page in between tags
	        // Tokenizes the text content found on page
	        docID++;
	        String parsedHTML 	= Jsoup.parse(content).text();
	        String docName 		= page.getName();
	        
			// Write the file that will contain the information of the page being processed
			// File writer solution: https://www.caveofprogramming.com/java/java-file-reading-and-writing-files-in-java.html
	        try {
	            // Assume default encoding
	            FileWriter fileWriter 		= new FileWriter(FILE_NAME, true);

	            // Always wrap FileWriter in BufferedWriter
	            BufferedWriter bufferedWriter 		= new BufferedWriter(fileWriter);
	            
	            // Note that write() does not automatically
	            // append a newline character
	            bufferedWriter.write(docID + "~`~" + docName + "~`~" + parsedHTML + "\n");
	            
	            // Always close files.
	            bufferedWriter.close();
	        }
	        
	        catch(IOException ex) {
	            System.out.println("Error writing to file");
	            ex.printStackTrace();
	        }
	        
		}
		
		catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 * Main function to run the indexer
	 */
	public static void main(String[] args) throws Exception {
//	    String file = "C:\\Users\\Jasmine\\Documents\\Search Engine Data\\Html\\access.ics.uci.educontact.html";
//	    String directory = "C:\\Users\\Jasmine\\Documents\\Search Engine Data\\Html";
//	    
//	    File dir = new File(directory);
//	    File[] directoryListing = dir.listFiles();
//	    
//	    // Solution help: https://stackoverflow.com/questions/4917326/how-to-iterate-over-the-files-of-a-certain-directory-in-java
//	    if (directoryListing != null) {
//	    	for (File child : directoryListing ) {
//	    		System.out.print("Processing page " + docID + " ... \n");
//	    		processHTMLPage(child);
//	    	}
//	    	
//	    	System.out.print("\nFinished processed all HTML files!");
//	    	
//	      } else {
//	        // Handle the case where dir is not really a directory.
//	    	System.out.print("else happened");
//	      }
		ArrayList<String> words = new ArrayList<String>();
		words.add("computer");
		words.add("computer");
		words.add("computer");
		words.add("computer");
		words.add("a");
		words.add("b");
		words.add("b");
		words.add("computer");
		
		System.out.print(Collections.frequency(words, "b"));

	}
}

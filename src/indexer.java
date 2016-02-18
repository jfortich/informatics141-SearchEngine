import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import org.jsoup.Jsoup;

import com.google.common.collect.ImmutableList;

/**
 * INF 141 Information Retrieval
 * Ricky Fong 			# 821869
 * Jasmine Fortich		# 46446130
 * Natalie Kassir		# 14591873 Hey!
 **/

public class indexer {
	
	// List of stop words
	public static final ImmutableList<String> STOP_WORDS = ImmutableList.of("a", "about", "above", "after", "again", 
	                                              "against", "all", "am", "an", "and", "any", "are",
	                                              "arent", "as", "at", "at", "be", "because", "been",
	                                              "before", "being", "below", "between", "both", "but",
	                                              "by", "cant", "cannot", "could", "couldnt", "did", "didnt",
	                                              "do", "does", "doesnt", "doing", "each", "few", "for",
	                                              "from", "further", "had", "hadnt", "has", "hasnt", "have",
	                                              "havent", "having", "he", "hed", "hell", "hes", "her", "here",
	                                              "heres", "hers", "herself", "him", "himself", "his", "how",
	                                              "hows", "i", "id", "ill", "im", "ive", "if", "in", "into",
	                                              "is", "isnt", "it", "its", "itself", "lets", "me", "more",
	                                              "most", "mustnt", "my", "myself", "no", "nor", "not", "of",
	                                              "off", "on", "once", "only", "or", "other", "ought", "our",
	                                              "ours", "ourselves", "out", "over", "own", "same", "shant",
	                                              "she", "shed", "shell", "shes", "shes", "shouldnt", "so", "some",
	                                              "such", "than", "that", "thats", "the", "their", "theres", 
	                                              "theirs", "them", "themselves", "then", "there", "theres",
	                                              "these", " they", "theyd", "theyll", "theyre", "theyve", "this",
	                                              "those", "through", "to", "too", "under", "until", "up", "very",
	                                              "was", "wasnt", "we", "wed", "well", "were", "weve", "werent",
	                                              "what", "whats", "when", "whens", "where", "wheres", "which",
	                                              "while", "whos", "who", "whom", "why", "whys", "with", "wont",
	                                              "would", "wouldnt", "you", "youd", "youll", "youre", "youve",
	                                              "your", "yours", "yourself", "yourselves");
	
	static HashMap<String, Integer> term2termid = new HashMap<String, Integer>();
	static HashMap<String, Integer> docID = new HashMap<String, Integer>();
	static HashMap<Integer, ArrayList> docid2termlist = new HashMap<Integer, ArrayList>();
	static HashMap<Integer, String> termid2term = new HashMap<Integer, String>();
	static int termID = 0;
	
	// Processes html file and gets content and tokens
	public static void processPage (File child) {
		String content = "";
		try {
	        BufferedReader in = new BufferedReader(new FileReader(child));
	        String str;
	        
	        while ((str = in.readLine()) != null) {
	            content += str;
	        }
	        in.close();
	        
	        String parsedHTML = Jsoup.parse(content).text();
	        ArrayList<String> tokens = Utilities.tokenizeFile(parsedHTML);
	        termID =  termID + 1;
	        
	        for (String token : tokens) {
	        	term2termid.put(token, termID);
	        }
	        
	        
		    System.out.print("Content: " + parsedHTML + "\n");
		    System.out.print("Tokens: " + Utilities.tokenizeFile(parsedHTML) + "\n");
		    System.out.print(term2termid);
		    
		  
		}
	    catch (IOException e) {
		e.printStackTrace();
	    }
	}
	
	
	public static void main(String[] args) throws Exception {
	    String file = "C:\\Users\\Jasmine\\Documents\\Search Engine Data\\Html\\access.ics.uci.educontact.html";
	    String directory = "C:\\Users\\Jasmine\\Documents\\Search Engine Data\\smallTest";
	    
	    File dir = new File(directory);
	    File[] directoryListing = dir.listFiles();
	    String list = Arrays.toString(directoryListing);
	    System.out.print(list + "\n\n");
	    
	    
	    // Solution: https://stackoverflow.com/questions/4917326/how-to-iterate-over-the-files-of-a-certain-directory-in-java
	    if (directoryListing != null) {
	    	for (File child : directoryListing ) {
	    		System.out.print(child + "\n\n");
	    		processPage(child);
	    	}
	    	
	      } else {
	        // Handle the case where dir is not really a directory.
	        // Checking dir.isDirectory() above would not be sufficient
	        // to avoid race conditions with another process that deletes
	        // directories.
	    	System.out.print("else happened");
	      }	      

	}
}


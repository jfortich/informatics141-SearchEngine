import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;

import com.google.common.collect.ImmutableList;

/**
 * INF 141 Information Retrieval
 * Ricky Fong 			# 821869
 * Jasmine Fortich		# 46446130
 * Natalie Kassir		# 14591873
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
	
	// Initialization of hashmaps and other variables
	static HashMap<String, Integer> term2termid = new HashMap<String, Integer>();
	static HashMap<String, Integer> docID = new HashMap<String, Integer>();
	static HashMap<Integer, ArrayList> docid2termlist = new HashMap<Integer, ArrayList>();
	static HashMap<Integer, String> termid2term = new HashMap<Integer, String>();
	static int termID = 0;
	static int uniqueDocID  = 0;
	
	
	/**
	 * Takes an HTML file and parses the content to retrieve the file's
	 * text between HTML tags.
	 * 
	 * Content found is then tokenized and stored in the 'term2termid' 
	 * dictionary with each entry organized as <term, termID>
	 * 
	 * @param page The HTML page that needs to be processed and parsed
	 */
	public static void processPage (File page) {
		HashSet<String> check = new HashSet<String>();
		String content = "";
		try {
	        BufferedReader in = new BufferedReader(new FileReader(page));
	        String str;
	        
	        while ((str = in.readLine()) != null) {
	            content += str;
	        }
	        in.close();
	        
	        String parsedHTML = Jsoup.parse(content).text();
	        ArrayList<String> tokens = Utilities.tokenizeFile(parsedHTML);
	        
	        for (String token : tokens) {
	        	if (!STOP_WORDS.contains(token) && !check.contains(token)){
	        		termID = termID + 1;
		        	term2termid.put(token, termID);
		        	check.add(token);
	        	}
	        }
	        
			
//		    System.out.print("Content: " + parsedHTML + "\n");
//		    System.out.print("Tokens: " + Utilities.tokenizeFile(parsedHTML) + "\n");
//		    System.out.print(term2termid+"\n\n");	
		    
		    
		    // Sorts and prints the term2termid dictionary
		    System.out.print(term2termid + "");
		    System.out.println("SORTED term2termid DICTIONARY:");
		    Iterator it = sortHashMapByValues(term2termid).entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        System.out.printf("%-20s %d %n",pair.getKey(), pair.getValue());
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		    
		}
	    catch (IOException e) {
		e.printStackTrace();
	    }
	}
	
	
	/**
	 * Solution: https://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
	 * 
	 * Takes a HashMap {@passedMap} and sorts the the hashmap by it's values which
	 * are Integers
	 * 
	 * @param passedMap A HashMap whose keys are Strings and values are Integers
	 * @return A sorted version of the hashmap ordered by it's values in ascending order
	 */
	public static LinkedHashMap<String, Integer> sortHashMapByValues(HashMap<String, Integer> passedMap) {
		   List<String> mapKeys = new ArrayList<String>(passedMap.keySet());
		   List<Integer> mapValues = new ArrayList<Integer>(passedMap.values());
		   Collections.sort(mapValues);
		   Collections.sort(mapKeys);

		   LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();

		   Iterator<Integer> valueIt = mapValues.iterator();
		   while (valueIt.hasNext()) {
		       Object val = valueIt.next();
		       Iterator<String> keyIt = mapKeys.iterator();

		       while (keyIt.hasNext()) {
		           Object key = keyIt.next();
		           String comp1 = passedMap.get(key).toString();
		           String comp2 = val.toString();

		           if (comp1.equals(comp2)){
		               passedMap.remove(key);
		               mapKeys.remove(key);
		               sortedMap.put((String)key, (Integer)val);
		               break;
		           }

		       }

		   }
		   return sortedMap;
		}
	
	public static void main(String[] args) throws Exception {
	    String file = "C:\\Users\\Jasmine\\Documents\\Search Engine Data\\Html\\access.ics.uci.educontact.html";
	    String directory = "C:\\Users\\Jasmine\\Documents\\Search Engine Data\\smallTest";
	    
	    File dir = new File(directory);
	    File[] directoryListing = dir.listFiles();
	    String list = Arrays.toString(directoryListing);
//	    System.out.print(list + "\n\n");
	    
	    
	    // Solution: https://stackoverflow.com/questions/4917326/how-to-iterate-over-the-files-of-a-certain-directory-in-java
	    if (directoryListing != null) {
	    	for (File child : directoryListing ) {
	    		System.out.print(child + "\n\n");
	    		uniqueDocID = uniqueDocID + 1;
	    		docID.put(child.toString(), uniqueDocID);
	    		processPage(child);
	    	}
	    	
	      } else {
	        // Handle the case where dir is not really a directory.
	        // Checking dir.isDirectory() above would not be sufficient
	        // to avoid race conditions with another process that deletes
	        // directories.
	    	System.out.print("else happened");
	      }	      
	    
	    // Sorts and prints the term2termid dictionary
//	    System.out.print(term2termid + "");
//	    System.out.println("SORTED term2termid DICTIONARY:");
//	    Iterator it = sortHashMapByValues(term2termid).entrySet().iterator();
//	    while (it.hasNext()) {
//	        Map.Entry pair = (Map.Entry)it.next();
//	        System.out.printf("%-20s %d %n",pair.getKey(), pair.getValue());
//	        it.remove(); // avoids a ConcurrentModificationException
//	    }
	    
	    // Sorts and prints the docID dictionary
	    System.out.print("\n\n" + docID + "");
	    System.out.println("SORTED docID DICTIONARY:");
	    Iterator it = sortHashMapByValues(docID).entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.printf("%-100s %d %n",pair.getKey(), pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }



	}
}


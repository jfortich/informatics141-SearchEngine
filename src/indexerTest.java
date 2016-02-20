import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;

import com.google.common.collect.ImmutableList;

/**
 * INF 141 Information Retrieval
 * Ricky Fong 			# 821869
 * Jasmine Fortich		# 46446130
 * Natalie Kassir		# 14591873
 **/


public class indexerTest {
	
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
	static HashMap<Integer, ArrayList<Integer>> docid2termlist = new HashMap<Integer, ArrayList<Integer>>();
	static HashMap<Integer, String> termid2term = new HashMap<Integer, String>();
	static int termID = 0;
	static int uniqueDocID  = 0;

	
	public static ArrayList<String> processPage (File page) {
		String content = "";
		ArrayList<String> tokens = new ArrayList<String>();
		
		try {
	        BufferedReader in = new BufferedReader(new FileReader(page));
	        String str;
	        
	        while ((str = in.readLine()) != null) {
	            content += str;
	        }
	        in.close();
	        
	        String parsedHTML = Jsoup.parse(content).text();
	        tokens = Utilities.tokenizeFile(parsedHTML);
		}
		
		catch (IOException e) {
		    e.printStackTrace();
		}
		    
		return tokens;
	}
	
	

	/**
	 * Takes an HTML file and parses the content to retrieve the file's
	 * text between HTML tags.
	 * 
	 * Content found is then tokenized and stored in the 'term2termid' 
	 * dictionary with each entry organized as <term, termID>
	 * 
	 * @param page The HTML page that needs to be processed and parsed
	 * @return 
	 */
	public static HashMap<String, Integer> createTerm2Term (ArrayList<String> tokens) {
		HashMap<String, Integer> words = new HashMap<String, Integer>();
	        
	        for (String token : tokens) {
	        	if (!STOP_WORDS.contains(token) && !term2termid.containsKey(token)) {
	        		termID = termID + 1;
	        		term2termid.put(token, termID);
  	
	        	}
	        }

		return words;
	}
	
	
	
	public static HashMap<Integer, ArrayList<Integer>> createDocID2TermList (HashMap<String,Integer> term2termIdList, HashMap<String,Integer> docIDList) {
		ArrayList<Integer> termIDs = new ArrayList<Integer>();

		
		for (Map.Entry <String, Integer> docID : docIDList.entrySet()) {
			File dir = new File(docID.getKey());
			ArrayList tokens = processPage(dir);
		}
		        
		for (String t : tokens) {
			for (Map.Entry<String, Integer> term : term2termIdList.entrySet()) {
				if (t.compareTo(term.getKey()) == 0) {
					termIDs.add(term.getValue());
				}
			}
		}
		
		docid2termlist.put(doc.getValue(), termIDs);

	}

		return docid2termlist;
		
	}
	
	
	/**
	 * Sorting method: https://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
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
	
	
	
	
	/**
	 * Main function to run the indexer
	 */
	public static void main(String[] args) throws Exception {
//	    String file = "C:\\Users\\Jasmine\\Documents\\Search Engine Data\\Html\\access.ics.uci.educontact.html";
	    String directory = "C:\\Users\\Jasmine\\Documents\\Search Engine Data\\smallTest";
	    
	    File dir = new File(directory);
	    File[] directoryListing = dir.listFiles();
//	    String list = Arrays.toString(directoryListing);
	    
	    
	    // Solution: https://stackoverflow.com/questions/4917326/how-to-iterate-over-the-files-of-a-certain-directory-in-java
	    if (directoryListing != null) {
	    	for (File child : directoryListing ) {
	    		uniqueDocID = uniqueDocID + 1;
	    		docID.put(child.toString(), uniqueDocID);
	    		ArrayList<String> t = processPage(child);
	    		createTerm2Term(t);
	    	}
	    	
	    	
	      } else {
	        // Handle the case where dir is not really a directory.
	    	System.out.print("else happened");
	      }	      
	    
	    System.out.println(createDocID2TermList(term2termid, docID));
	    
	    
	    
	    // Sorts and prints the term2termid dictionary 
	    // BUG: Starts printing at id "17" instead of "1"
	    System.out.print("\n\n UNSORTED: " + term2termid + "\n");
	    System.out.println("SORTED term2termid DICTIONARY:");
	    Iterator<Map.Entry<String, Integer>> term2termidIt = sortHashMapByValues(term2termid).entrySet().iterator();
	    while (term2termidIt.hasNext()) {
	        Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>)term2termidIt.next();
	        System.out.printf("%-20s %d %n",pair.getKey(), pair.getValue());
	        term2termidIt.remove(); // avoids a ConcurrentModificationException
	    }
	    
	    
//	    // Sorts and prints the docID dictionary
	    System.out.print("\n\n" + docID + "");
	    System.out.println("SORTED docID DICTIONARY:");
	    Iterator<Map.Entry<String, Integer>> docIDIt = sortHashMapByValues(docID).entrySet().iterator();
	    while (docIDIt.hasNext()) {
	        Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>)docIDIt.next();
	        System.out.printf("%-100s %d %n",pair.getKey(), pair.getValue());
	        docIDIt.remove(); // avoids a ConcurrentModificationException
	    }



	}

}
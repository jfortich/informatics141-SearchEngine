import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
	static HashMap<String, Integer> docIDdictionary = new HashMap<String, Integer>();
	static HashMap<Integer, String> termid2term = new HashMap<Integer, String>();
	static HashMap<Integer, ArrayList<Integer>> docid2termlist = new HashMap<Integer, ArrayList<Integer>>();
	static HashMap<Integer, ArrayList<HashMap<Integer, Integer>>> term2DocIDWordFreq = new HashMap<Integer, ArrayList<HashMap<Integer, Integer>>>();
	static int termID = 0;
	static int uniqueDocID  = 0;
	static long startingTime = System.currentTimeMillis();

	
	public static void buildIndex (File processedPages) {
		ArrayList<String> tokens = new ArrayList<String>();
		
		// Read the file line by line to grab the docID, url, and content of each HTML page crawled
		try (BufferedReader br = new BufferedReader(new FileReader(processedPages))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    
		       String info[] = line.split("~`~");
		       
		       if (info.length == 3) {
//			       System.out.print(Arrays.toString(info));
			       
			       int docID 		= Integer.parseInt(info[0]);
			       String url		= info[1];
			       String content 	= info[2];
			       
			       tokens = Utilities.tokenizeFile(content);
			       
			       docIDdictionary.put(url, docID);
			       createTerm2TermID(tokens);
			       createTermID2Term(term2termid);
			       createDocID2TermList(tokens, docID, term2termid);
//			       createTerm2DocIDWordFreq(tokens, term2termid, docIDdictionary);
		       }
		       
		    }
		    br.close();
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

	/**
	 * Takes a list of tokenized words {@tokens} from the document's page 
	 * and stores each unique token in the 'term2termid' dictionary with 
	 * each entry organized as <term, termID>
	 * 
	 * @param tokens An arraylist of tokens found on a document's page
	 * @return Returns a hashmap of terms and their corresponding term ID
	 */
	public static HashMap<String, Integer> createTerm2TermID (ArrayList<String> tokens) {
        // Goes through all tokens and adds those that are not a Stop Word to term2termid
        for (String token : tokens) {
        	if (!STOP_WORDS.contains(token) && !term2termid.containsKey(token)) {
        		termID = termID + 1;
        		term2termid.put(token, termID);
        	}
        }
		return term2termid;
	}
	
	
	/**
	 * Creates an inverse of the term2termID dictionary where the key is 
	 * the term's termID and the corresponding value is the term's string
	 * 
	 * @param term2termID A HashMap<String,Integer> of the original dictionary
	 * which contains the term's string as the key and the term's termID as the 
	 * value
	 * @return Returns a HashMap<Integer,String> which is the inverse of the 
	 * original dictionary 
	 */
	public static HashMap<Integer, String> createTermID2Term (HashMap<String, Integer> term2termID) {
		for (Map.Entry<String, Integer> termItem : term2termID.entrySet()) {
			termid2term.put(termItem.getValue(), termItem.getKey());
		}
		return termid2term;
	}
	
	
	/**
	 * Creates the docID2termList which maps out a document's ID to the 
	 * corresponding term IDs that are found in the document's content
	 * 
	 * @param term2termIdList Hashmap<String, Integer> of terms and their
	 * corresponding termIDs
	 * @param docIDList HashMap<String, Integer> of documents and their
	 * corresponding docIDs
	 * @return Returns aHashMap<Integer, ArrayList<Integer>>  that contains 
	 * the docID and corresponding list of termIDs found on the doc
	 */
	public static HashMap<Integer, ArrayList<Integer>> createDocID2TermList (ArrayList<String> tokens, Integer docID, HashMap<String,Integer> term2termIdList) {
		// Initialize variable
		ArrayList<Integer> termIDs = new ArrayList<Integer>();

		// Goes through each token and retrieves their corresponding termID;
		for (String token : tokens) {
			for (Map.Entry<String, Integer> term : term2termIdList.entrySet()) {
				// Adds each termID to the termIDs ArrayList<Integer> 
				if (token.compareTo(term.getKey()) == 0) {
					termIDs.add(term.getValue());
				}
			}
		}
		
		docid2termlist.put(docID, termIDs);
		
		return docid2termlist;
		
	}
	
	/**
	 * Creates a term2DocIDWordFreq dictionary which contains the word's 
	 * termID and a correspond list of docIDs and the frequency of the word
	 * on that document
	 *  
	 * @param term2termIdList Hashmap<String, Integer> of terms and their
	 * corresponding termIDs
	 * @param docIDList HashMap<String, Integer> of documents and their
	 * corresponding docIDs
	 * @return term2docIDWordFreq HashMap<Integer, ArrayList<HashMap<Integer, Integer>>>
	 */
	public static HashMap<Integer, ArrayList<HashMap<Integer, Integer>>> createTerm2DocIDWordFreq(ArrayList<String> tokens, HashMap<String,Integer> term2termIdList, HashMap<String,Integer> docIDList) {		
		// Goes through each term from the term2termID dictionary
		for (Map.Entry<String, Integer> term : term2termIdList.entrySet()) {
			
			// Temporary HashMap to store docID and term frequencies
			ArrayList<HashMap<Integer, Integer>> docIDFrequencies = new ArrayList<HashMap<Integer, Integer>>();
			
			// Goes through each document and counts the frequency of the term in each doc
			for (Map.Entry<String, Integer> doc : docIDList.entrySet()) {
				int frequency = 0;
				HashMap<Integer, Integer> docIDFrequency = new HashMap<Integer, Integer>();

				for (String token : tokens) {
					if (token.compareTo(term.getKey()) == 0) {
						frequency++;
					}
				}
				docIDFrequency.put(doc.getValue(), frequency);	
				docIDFrequencies.add(docIDFrequency);
			}
		
			// Adds the term ID and the list of doc IDs and the term's frequency to ther term2DocIDWordFrequency dictionary
			term2DocIDWordFreq.put(term.getValue(), docIDFrequencies);
			
		}
		return term2DocIDWordFreq;
	}
	
	
	
	/**
	 * Main function to run the indexer
	 */
	public static void main(String[] args) throws Exception {
		String dir 	= "C:\\Users\\Jasmine\\Documents\\ICS45J-Jfortich\\INF141RJBNwebCrawler\\processedPagesSmall.txt";
	    String dir2 = "C:\\Users\\Jasmine\\Documents\\ICS45J-Jfortich\\informatics141-SearchEngine\\processedHTMLPages.txt";
		File processed = new File(dir2);
		buildIndex(processed);
		
	    System.out.print("Term2Termid DICTIONARY: \n	" + term2termid + "\n\n");
	    System.out.println("DocID2TermList DICTIONARY: \n	" + docid2termlist + "\n\n");
	    System.out.println("TermID2Term DICTIONARY: \n	" + termid2term + "\n\n");
	    System.out.print("docID DICTIONARY: \n	" + docIDdictionary + "\n\n");
//	    System.out.print("Term2DocIDWordFrequency DICTIONARY: \n	" + term2DocIDWordFreq + "\n\n");

	}

}

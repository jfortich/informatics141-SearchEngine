/**
 * INF 141 Information Retrieval
 * Ricky Fong 			# 821869
 * Jasmine Fortich		# 46446130
 * Natalie Kassir		# 14591873
 */

package searchEngine;

import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import searchEngine.DBConnectionManager;
import searchEngine.indexerDB;

import com.google.common.collect.ImmutableList;

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
	// Paramaters for connecting to MySQL DB
    public static final String URL  = "jdbc:mysql://localhost:3306/inf141index?autoReconnect=true&useSSL=false";
    public static final String USER = "root";
    public static final String PW 	= "Buster2000";
    
    // Initialize variables to creat term2termid
	static HashMap<String, Integer> term2termid = new HashMap<String, Integer>();
	static HashMap<String, Integer> docIDdictionary = new HashMap<String, Integer>();
	static int termID = 0;
	static int uniqueDocID  = 0;


	
	public static void buildIndex (File processedPages) throws PropertyVetoException {
		ArrayList<String> tokens = new ArrayList<String>();
		
		// Read the file line by line to grab the docID, url, and content of each HTML page crawled
		try (BufferedReader br = new BufferedReader(new FileReader(processedPages))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    
		       String info[] = line.split("~`~");
//			   String info[] = line.split(";");

		        DBConnectionManager dbConnectionManager = new DBConnectionManager();

		        if (info.length == 3) {			       
			       int docID 		= Integer.parseInt(info[0]);
			       String url		= info[1];
			       String content 	= info[2];
			       
			       tokens = Utilities.tokenizeFile(content);
			       
			       // Updates doc2docid table 
			       docIDdictionary.put(url, docID);
			       indexerDB.updateDoc2DocID(url, docID);
			       
			       // Updates term2termid table
			       createTerm2TermID(tokens);
			       
			       // Updates termid2term table
			       indexerDB.createTermID2Term();
			       
			       // Updates docid2termlist table
			       createDocID2TermList(tokens, docID);
			       
			       // Updates termfrequency table
			       createTerm2DocIDWordFreq();
			        
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
	 * and stores each unique token in the 'term2termid' table in the 
	 * inf141index DB
	 * @throws PropertyVetoException 
	 * 
	 */
	public static void createTerm2TermID (ArrayList<String> tokens) throws PropertyVetoException {
        // Goes through all tokens and adds those that are not a Stop Word to term2termid
        for (String token : tokens) {
        	if (!STOP_WORDS.contains(token) && !term2termid.containsKey(token)) {
        		termID = termID + 1;
                indexerDB.updateTerm2TermID(token, termID);
        	}
        }    
	}
	
	
	/**
	 * Creates an inverse of the term2termID dictionary in the inf141 DB
	 *  where the key is the term's termID and the corresponding value is 
	 *  the term's string
	 */
	public static void createTermID2Term () {
		indexerDB.createTermID2Term();
	}
	
	
	/**
	 * Creates the docID2termList dictionary in the inf141index DB
	 * @throws PropertyVetoException 
	 * 
	 */
	public static void createDocID2TermList (ArrayList<String> tokens, Integer docID) throws PropertyVetoException {
		// Initialize variable
		StringBuilder termIDs = new StringBuilder();

		// Goes through each token and retrieves their corresponding termID;
		for (String token : tokens) {
			int termID = indexerDB.retrieveTermID(token);
			if (termID != 0 ){
				termIDs.append(termID + ",");
			}
		}
		
		String termList = termIDs.toString();
		indexerDB.updateDocID2TermList(docID, termList);
		
	}
	
	/**
	 * Creates a term2DocIDWordFreq dictionary in the inf141index DB
	 */
	public static void createTerm2DocIDWordFreq() {		
		indexerDB.createTermFrequency();
	}
	
	
	
	/**
	 * Main function to run the indexer
	 */
	public static void main(String[] args) throws Exception {
//		String dir 	= "C:\\Users\\Jasmine\\Documents\\ICS45J-Jfortich\\INF141RJBNwebCrawler\\processedPagesSmall.txt";
//		String dir2 = "C:\\Users\\Jasmine\\Documents\\ICS45J-Jfortich\\INF141RJBNwebCrawler\\ten-processedPages.txt";
	    String dir3 = "C:\\Users\\Jasmine\\Documents\\ICS45J-Jfortich\\informatics141-SearchEngine\\processedHTMLPages.txt";
	    File processed = new File(dir3);
		buildIndex(processed);
		System.out.print("Finished building index.");

	}

}

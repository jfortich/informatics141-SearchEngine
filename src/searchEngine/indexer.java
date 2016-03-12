package searchEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import searchEngine.indexerDB;

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
		    
//		       String info[] = line.split("~`~");
			   String info[] = line.split(";");

		       
		       if (info.length == 3) {
//			       System.out.print(Arrays.toString(info));
			       
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
	 * and stores each unique token in the 'term2termid' dictionary with 
	 * each entry organized as <term, termID>
	 * 
	 * @param tokens An arraylist of tokens found on a document's page
	 * @return Returns a hashmap of terms and their corresponding term ID
	 */
	public static void createTerm2TermID (ArrayList<String> tokens) {
        // Goes through all tokens and adds those that are not a Stop Word to term2termid
        for (String token : tokens) {
        	if (!STOP_WORDS.contains(token) && !term2termid.containsKey(token)) {
        		termID = termID + 1;
                indexerDB.updateTerm2TermID(token, termID);
        	}
        }    
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
	public static void createTermID2Term () {
		indexerDB.createTermID2Term();
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
	public static void createDocID2TermList (ArrayList<String> tokens, Integer docID) {
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
	public static void createTerm2DocIDWordFreq() {		
		indexerDB.createTermFrequency();
	}
	
	
	
	/**
	 * Main function to run the indexer
	 */
	public static void main(String[] args) throws Exception {
		String dir 	= "C:\\Users\\Jasmine\\Documents\\ICS45J-Jfortich\\INF141RJBNwebCrawler\\processedPagesSmall.txt";
		String dir2 = "C:\\Users\\Jasmine\\Documents\\ICS45J-Jfortich\\INF141RJBNwebCrawler\\ten-processedPages.txt";
	    String dir3 = "C:\\Users\\Jasmine\\Documents\\ICS45J-Jfortich\\informatics141-SearchEngine\\processedHTMLPages.txt";
	    File processed = new File(dir2);
		buildIndex(processed);
		
        // Serialization: http://www.tutorialspoint.com/java/java_serialization.htm
        // Serializes the dictionaries for use later
//        try
//        {
//        	// Serializes the term2TermID dictionary
//           FileOutputStream term2TermFileOut = new FileOutputStream("term2TermID.ser");
//           ObjectOutputStream term2TermOut 	 = new ObjectOutputStream(term2TermFileOut);
//           term2TermOut.writeObject(term2termid);
//           term2TermOut.close();
//           term2TermFileOut.close();
//           System.out.printf("Serialized data is saved in term2TermID.ser \n");
//           
//           
//           // Serializes the docID dictionary
//           FileOutputStream docIDDictionaryFileOut = new FileOutputStream("docIDDictionary.ser");
//           ObjectOutputStream docIDDictionaryOut   = new ObjectOutputStream(docIDDictionaryFileOut);
//           docIDDictionaryOut.writeObject(docIDdictionary);
//           docIDDictionaryOut.close();
//           docIDDictionaryFileOut.close();
//           System.out.printf("Serialized data is saved in docID2TermList.ser \n");
//           
//           
//           // Serializes the termID2Term dictionary
//           FileOutputStream termID2TermFileOut  = new FileOutputStream("termID2Term.ser");
//           ObjectOutputStream termID2TermOut 	= new ObjectOutputStream(termID2TermFileOut);
//           termID2TermOut.writeObject(termid2term);
//           termID2TermOut.close();
//           termID2TermFileOut.close();
//           System.out.printf("Serialized data is saved in termID2Term.ser \n");
//           
//           
//           // Serializes the docID2TermList dictionary
//           FileOutputStream docID2TermListFileOut = new FileOutputStream("docID2TermList.ser");
//           ObjectOutputStream docID2TermListOut   = new ObjectOutputStream(docID2TermListFileOut);
//           docID2TermListOut.writeObject(docid2termlist);
//           docID2TermListOut.close();
//           docID2TermListFileOut.close();
//           System.out.printf("Serialized data is saved in docID2TermList.ser \n");
//           
//           
//           // Serializes the term2DocIDWordFreq dictionary
//           FileOutputStream term2DocIDWordFreqFileOut = new FileOutputStream("term2DocIDWordFreq.ser");
//           ObjectOutputStream term2DocIDWordFreqOut 	= new ObjectOutputStream(term2DocIDWordFreqFileOut);
//           term2DocIDWordFreqOut.writeObject(term2DocIDWordFreq);
//           term2DocIDWordFreqOut.close();
//           term2DocIDWordFreqFileOut.close();
//           System.out.printf("Serialized data is saved in term2DocIDWordFreq.ser \n");
//        }
//        
//        catch(IOException i) {
//            i.printStackTrace();
//        }
		
//	    System.out.print("Term2Termid DICTIONARY: \n	" + term2termid + "\n\n");
//	    System.out.println("DocID2TermList DICTIONARY: \n	" + docid2termlist + "\n\n");
//	    System.out.println("TermID2Term DICTIONARY: \n	" + termid2term + "\n\n");
	    System.out.print("docID DICTIONARY: \n	" + docIDdictionary + "\n\n");
//	    System.out.print("Term2DocIDWordFrequency DICTIONARY: \n	" + term2DocIDWordFreq + "\n\n");

	}

}

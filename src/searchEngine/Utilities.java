package searchEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A collection of utility methods for text processing.
 */
public class Utilities {
	/**
	 * Reads the input text file and splits it into alphanumeric tokens.
	 * Returns an ArrayList of these tokens, ordered according to their
	 * occurrence in the original text file.
	 * 
	 * Non-alphanumeric characters delineate tokens, and are discarded.
	 *
	 * Words are also normalized to lower case. 
	 * 
	 * Example:
	 * 
	 * Given this input string
	 * "An input string, this is! (or is it?)"
	 * 
	 * The output list of strings should be
	 * ["an", "input", "string", "this", "is", "or", "is", "it"]
	 * 
	 * @param input The file to read in and tokenize.
	 * @return The list of tokens (words) from the input file, ordered by occurrence.
	 */
	public static ArrayList<String> tokenizeFile(String input) {
		// Normalizes the string "tokens" to all lowercase
		input = input.toLowerCase();

		// creates a new ArrayList using the String tokens,
		// splits the string into alphanumeric characters only
		ArrayList<String> tokenList = new ArrayList<String>(Arrays.asList(input.split("\\W+")));
		return tokenList;
	}
	
	/**
	 * Takes a list of {@link Frequency}s and prints it to standard out. It also
	 * prints out the total number of items, and the total number of unique items.
	 * 
	 * Example one:
	 * 
	 * Given the input list of word frequencies
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total item count: 6
	 * Unique item count: 5
	 * 
	 * sentence	2
	 * the		1
	 * this		1
	 * repeats	1
	 * word		1
	 * 
	 * 
	 * Example two:
	 * 
	 * Given the input list of 2-gram frequencies
	 * ["you think:2", "how you:1", "know how:1", "think you:1", "you know:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total 2-gram count: 6
	 * Unique 2-gram count: 5
	 * 
	 * you think	2
	 * how you		1
	 * know how		1
	 * think you	1
	 * you know		1
	 * 
	 * @param frequencies A list of frequencies.
	 */
	public static void printFrequencies(List<Frequency> frequencies) {
		// Calculates total frequency count and unique count
		int totalCount  = 0;
		int uniqueCount = 0;
		for (Frequency f : frequencies) {
			totalCount = totalCount + f.getFrequency();
			uniqueCount++;
		}
		
		// Gets first item in list to check for 2-gram frequencies:
		// 	Splits the text at the space; if the length of the resulting
		//  array is 2, the text is a 2-gram frequency; Otherwise
		//  the text is a regular frequency
		String text = frequencies.get(0).getText();
		// Prints correct format if list contains 2-gram frequencies
		if ((text.split("\\s+")).length == 2) {
			System.out.print("Total 2-gram count: " + Integer.toString(totalCount) + "\n");
			System.out.print("Unique 2-gram count: " + Integer.toString(uniqueCount) + "\n\n");
			for (Frequency freq : frequencies) {
				System.out.printf("%-15s %d %n",freq.getText(),freq.getFrequency());
			}
		}
		// Else prints format for regular frequencies
		else {
			System.out.print("Total item count: " + Integer.toString(totalCount) + "\n");
			System.out.print("Unique item count: " + Integer.toString(uniqueCount) + "\n\n");
			for (Frequency freq : frequencies) {
				System.out.printf("%-20s %d %n",freq.getText(),freq.getFrequency());
			}
		}
	}

}

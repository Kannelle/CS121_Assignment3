/*
Kristina Wong, 76513468
Haoming Li, 20426226
Shengjie Xu, 10616769
Yirui Jiang, 64137163
*/

import java.io.File;
import java.util.ArrayList;
import java.util.*;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;

/**
 * A collection of utility methods for text processing.
 */
public class Modified_again_Utilities {
   
	/**
	 * Reads the input text file and splits it into alphanumeric tokens.
	 * Returns an ArrayList of these tokens, ordered according to their
	 * occurrence in the original text file.
	 * 
	 * Non-alphanumeric characters delineate tokens, and are discarded.
	 *
	 * Words are also normalized to lower case. 
	 * 
	 * @param input The file to read in and tokenize.
	 * @return The list of tokens (words) from the input file, ordered by occurrence.
	 */
	public static ArrayList<String> tokenizeFile(String text, boolean useStopWords, List<String> stopWords) {
      //createStopWordsList();
      ArrayList<String> words = new ArrayList<String>();// create an arraylist for separated word
      String delimiter = new String("[^a-zA-Z'&]+"); // create a delimiter
      String[] parts = text.split(delimiter);// split lines according to the delimiter
      for(String part: parts){
         if(!part.equals("") && part.length()>1 && (part.charAt(0)!='&')){
            if(useStopWords && !stopWords.contains(part.toLowerCase())){//check if to use stop words
               words.add(part.toLowerCase());} // put the split words into an arraylist(words),
                                                 // delete all the empty strings,
                                                 // and convert all the upper cases to lower cases
            else if(!useStopWords){
               words.add(part.toLowerCase());
            }
         }
      }
		return words;
	}
	
}

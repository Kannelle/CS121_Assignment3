import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class Index {
	public static void main(String[] args) throws Exception {
		
	}
	
	// Takes as a parameter the location (directory) of all the files 
	// and produces basic index as output.
	// The index maps words to documents (pages).
	public static Multimap<String, Frequency> getIndex(String filePath) {
		Multimap<String, Frequency> multimap = ArrayListMultimap.create();
		
		//TODO
		
		// Find the number of documents
		// Count the number of values per key in the multimap and find the sum
		int valueCounter = 0;
		for(Frequency value : multimap.values()) {
		   valueCounter++;
		  }
		System.out.println("Documents: "+valueCounter);
		
		// Find the number of [unique] words 
		// Count the number of keys in the multimap
		int size = multimap.size();
		System.out.println("Keys: "+size); 
		
		// Create a sample index: you can simply provide a text version of the key value pairs 
		// you have in the indexes.
		
		// Find the total size (in KB) of your index on disk
		
		// Find the time taken to create your index
		
		return multimap;
	}
}

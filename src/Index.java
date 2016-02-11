import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class Index {
	public static void main(String[] args) throws Exception {
		
	}
	
	// Takes as a parameter the location (directory) of all the files 
	// and produces basic index as output.
	public static Multimap<String, String> getIndex(String filePath) {
		Multimap<String, String> multimap = ArrayListMultimap.create();
		//TODO
		return multimap;
	}
}

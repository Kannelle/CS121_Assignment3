/*
Kristina Wong, 76513468
Haoming Li, 20426226
Shengjie Xu, 10616769
Yirui Jiang, 64137163
*/

import java.net.*;
import java.io.*;
import java.util.*;

// using Jsoup(in the 'lib' folder) for parsing htmls, so please incude it in the compile path
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;


class Index
{
   //using the base 10 logarithm
   private static HashMap<Integer, HashMap<Integer, Double>> indexTfIdf = new HashMap<Integer, HashMap<Integer, Double>>(); // contains termID -> docID, tf-idfs
   private static HashMap<Integer, HashMap<Integer, Double>> docID_tfs = new HashMap<Integer, HashMap<Integer, Double>>(); // contains docID -> termID , tfs
   private static List<String> stopWords = new ArrayList<String>(); // contains stop words
   private static HashMap<String,Integer> term_ID = new HashMap<String,Integer>(); // contains term -> termID
   private static HashMap<Integer,String> ID_term = new HashMap<Integer,String>(); // contains termID -> term
   private static HashMap<Integer,String> ID_doc = new HashMap<Integer,String>(); // contains docID -> doc
   private static HashMap<Integer,Double> term_numPages= new HashMap<Integer, Double>(); // contains termID -> number of pages which the has appeared 
   /**
   *  my html parser, not a very good one, used to handle the situation when Jsoup does not work
   */
   private static String myHtmlParser(File file){
      try{
         String content = new Scanner(file).useDelimiter("\\Z").next();
         content = content.replaceAll("\\<.*\\>", "");
         return content;
      }catch(Exception e){
         String content = "";
         return content;
      }
   }
   
   
   /**
   *  Create the index of docs
   *  The input will be the path of the folder which contains docs
   *  The function will generate several indices:
   *  1. term -> termID    
   *  2. termID -> term (will be written into id_term_index.txt)
   *  3. docID -> doc   (will be written into id_doc_index.txt)
   *  4. termID -> number of pages which the has appeared 
   *  5. docID -> termID , tfs
   *     And the most important:
   *  6. termID -> docID, tf-idfs (will be written into Index.txt in anothe function)
   */   
   private static void createIndexForFolder(final File folder, final String folderName) throws Exception {
      int docCount = 0; // doc counter, also used for docID
      int termIDcounter = 0; // used for termID
      int cannotParse = 0; // page that reports errors using Jsoup
      BufferedWriter id_doc_indexWriter = new BufferedWriter(new FileWriter("id_doc_index.txt"));
      BufferedWriter id_term_indexWriter = new BufferedWriter(new FileWriter("id_term_index.txt"));
      
      for (final File file : folder.listFiles()){
         if (file.isDirectory()){
            createIndexForFolder(file,folderName + "/" + file.getName());
         }
         else{
            docCount += 1;
            String fileName = file.getName();
            ID_doc.put(docCount,fileName); // generating docID -> doc
            
            // write docID -> doc to txt
            id_doc_indexWriter.write(Integer.toString(docCount) + " : " + fileName);
            id_doc_indexWriter.newLine();
            File input = new File(folderName+"/"+fileName);
            // using Jsoup to parse html into text
            String text = "";
            try{
               Document doc = Jsoup.parse(input,"UTF-8");
               text += doc.body().text();
            
            }catch(Exception e){
               text += myHtmlParser(input); // if Jsoup does not work, use my own html parser function
               cannotParse += 1;
            }
            ArrayList<String> words = Modified_again_Utilities.tokenizeFile(text,true,stopWords); //tokenize the page into words
            HashMap<Integer, Double> tf= new HashMap<Integer, Double>(); // contains termID -> tf in this page
            int numberOfWordInThePage = words.size(); // number of words in this page(NOT including stop words)
            
            for(int i=0; i < numberOfWordInThePage; i++ ){
               String wordStr = words.get(i);
               if(!tf.containsKey(term_ID.get(wordStr))){
                  if(!term_ID.containsKey(wordStr)){
                     // if term_ID doesn't contain the term and we haven't seen the term in this page before,
                     // put new term -> termID and termID -> term to hashmaps
                     termIDcounter += 1;
                     term_ID.put(words.get(i),termIDcounter);
                     ID_term.put(termIDcounter,wordStr);
                     
                     // write termID -> term to txt
                     id_term_indexWriter.write(Integer.toString(termIDcounter) + " : " + wordStr);
                     id_term_indexWriter.newLine();
                     // number of pages that the word has appeared, initialize it to 0, will increment to 1 latter
                     term_numPages.put(termIDcounter,0.0); 
                  }
                  // if term_ID contains the term but we haven't seen the term in this page before
                  // put term -> 1 to tf, where 1 means that now the term has appeared once in the page
                  tf.put(term_ID.get(wordStr),(double)(1));

               }
               else{
                  // add 1 to the number of time that the term has appeared(in the page)
                  tf.put(term_ID.get(wordStr),tf.get(term_ID.get(wordStr)) + 1);
               }
            }
            for (int key : tf.keySet()) { // for each word in the page
               // if a word has appeared once in the page, plus one to the total number of pages that the word has appeared
               // (will be used to compute idf latter)
               term_numPages.put(key,term_numPages.get(key)+1); 
               
               // compute tfs for each page
               // (will be used to compute tf-idf latter)
               tf.put(key,tf.get(key)/numberOfWordInThePage);
            }

            docID_tfs.put(docCount,tf);

         }
      }

      // initialize indexTfIdf
      for (int i = 0; i < ID_term.size(); i++){
         indexTfIdf.put(i+1,new HashMap<Integer, Double>());
      }
      
      // compute tf_idf for each word and page
      for (int docID : docID_tfs.keySet()){
         for (int termID : docID_tfs.get(docID).keySet()){
            // generate tf_idf, using the base 10 logarithm 
            double tf_idf = Math.round(docID_tfs.get(docID).get(termID)*Math.log10((double)docCount/term_numPages.get(termID))*10000.0)/10000.0;
            indexTfIdf.get(termID).put(docID,tf_idf);
         }
      }
      System.out.println("Number of documents : "+Integer.toString(docCount));
      System.out.println("Number of [unique] words( NOT including stop words) : "+Integer.toString(term_ID.size()));
      System.out.println("Pages that cannot parse using Jsoup : "+ Integer.toString(cannotParse));
      id_doc_indexWriter.close();
      id_term_indexWriter.close();
   }
   
   /**
   *  This function will write (termID -> docID, tf-idfs) to index.txt with some format
   */
   private static void writeIndexTfIdftoFile() throws Exception{
      BufferedWriter indexWriter = new BufferedWriter(new FileWriter("Index.txt"));
      for (int i : indexTfIdf.keySet()){
         indexWriter.write(Integer.toString(i) + " : " + indexTfIdf.get(i));
         indexWriter.newLine();
      }
      indexWriter.close();
   }

   /**
   *  This function is used to get stop words from stopWords.txt
   */ 
   private static void createStopWordsList() throws Exception{
      String delimiter = new String("[^a-zA-Z\']+");
      //get stop words from file stopWords
      File stopW = new File("stopWords.txt");
      Scanner scan = new Scanner(stopW); // scan the input file
      while(scan.hasNextLine()){
         String line = scan.nextLine();
         String[] parts = line.split(delimiter);
         for(String part: parts)
         {
            if(!part.equals(""))
            {
               stopWords.add(part);
            }
         }
      } 
   }
   

   public static void main(String args[])throws Exception{
      long tStart = System.currentTimeMillis();
      
      createStopWordsList();
      String folderName = "Html";
      final File folder = new File(folderName);     // folder that contains docs
      createIndexForFolder(folder,folderName);  // create index
      writeIndexTfIdftoFile();                  // write index to txt file
           
      long tEnd = System.currentTimeMillis();
      long tDelta = tEnd - tStart;
      double elapsedSeconds = tDelta / 1000.0;
      System.out.print("Time used(second):  "); 
      // print out the time used to create index, including the time spend on writting the index to Index.txt
      System.out.println(elapsedSeconds); 
       
  } 
}
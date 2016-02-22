Several notifications:

1. I used Jsoup(it's in the 'lib') to parse html into text, so please include it in your compile path
2. I used the base 10 logarithm for idf
3. Please put Html(which contains the documents that we want to index) under the 'src' folder
4. The program will generate 3 txt files
	1. Index.txt : 		contains termID -> docID, tf-idfs
	2. id_term_index.txt :  contains termID -> term 
	3. id_doc_index.txt :   contains docID -> doc
import java.io.*;
import java.util.*;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * 
 * @author Group 15
 * 
 * This class is used to do pre-processing of the third extra credit. It uses Stanford POS Tagger and 
 * only selects nouns from Amazon review. 
 *
 */
public class posTest {
	public static void main(String[] args) throws IOException, ClassNotFoundException{
	
		MaxentTagger tagger = new MaxentTagger("left3words-wsj-0-18.tagger");
		//read lemmatized review file
		BufferedReader reader = new BufferedReader(new FileReader(new File("/home/o/class/cs129a/assignment4/all.txt")));
		//BufferedReader reader = new BufferedReader(new FileReader(new File("/Users/Yahui/Desktop/all.txt")));

		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("pa4_review_pos.txt")));
		int counter = 0;
		String currentLine = "";
		while((currentLine = reader.readLine()) != null) {
				String[] tmp = currentLine.split(":");
			if (tmp[0].trim().equalsIgnoreCase("review/text")) {
				//System.out.println(tmp[1].trim());
				
				++counter;
				
				String tagged = tagger.tagString(tmp[1].trim());
				String[] split = tagged.split(" ");
				for(String s : split) {
					//split the word with its tag
					int index = s.indexOf("/");
					//if the tag starts with "N", it should be a noun
					if(s.substring(index + 1).startsWith("N")) {
						//write the word into file
						writer.write(s.substring(0,index) + " ");
					}
				}
				writer.newLine();
			}
			
		}
		System.out.println(counter);
		reader.close();
		writer.close();
	}
}

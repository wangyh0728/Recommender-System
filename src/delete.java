import java.io.*;
import java.util.*;

/**
 * 
 * @author Group 15
 * 
 * This class is used for pre-processing extra credit, part three. It can combine reviews for the same product 
 * into one line so that we can use that for clustering. 
 *
 */
public class delete {

	public static void main(String[] args) throws java.io.IOException{
		BufferedReader reader1 = new BufferedReader(new FileReader(new File("pa4_review_pos.txt")));	
		BufferedReader reader2 = new BufferedReader(new FileReader(new File("pa4_item_all.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("pa4_review_clustering_input.txt")));
		BufferedWriter writer2 = new BufferedWriter(new FileWriter(new File("pa4_item_clustering.txt")));
		String item = "";
		//String curr = "";
		String prev = "";
		while((item = reader2.readLine().trim()) != null) {
			String review = reader1.readLine().trim();
			//if the new item is as same as the previous one, then we combine corresponding 
			//review into one group
			if(item.equals(prev)) {
				writer.append(review + " ");
			}else { //if not, we start a new line
				writer.newLine();
				writer.write(review + " ");
				writer2.write(item);
				writer2.newLine();
				prev = item;
			}
		}
		
		
		
		reader1.close();
		reader2.close();
		writer.close();
		writer2.close();

	}
}

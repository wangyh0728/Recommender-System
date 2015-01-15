import java.io.*;
import java.util.*;

/**
 * 
 * @author Group 15
 * 
 * This class is for pre-processing of item-based recommendation. It selects product/productId, 
 * review/userId and review/score from every individual user review. For both productId and userId,
 * it can convert IDs from alphanumeric into numeric. The output file can be directly used by
 * Mahout commands and our Java files. 
 *
 */

public class Reduce {

	public static void main(String args[]) throws java.io.IOException {

		// file
		File amazonFile = new File("/home/o/class/cs129a/assignment4/all.txt");

		//create buffered reader and writer
		BufferedReader reader = new BufferedReader(new FileReader(amazonFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("output_int")));
	
				
		//build hashmap to match IDs with assigned integer
		Map<String, Integer> pMap = new HashMap<String, Integer>();
		Map<String, Integer> uMap = new HashMap<String, Integer>();
		// read every review in all.txt
		String line = "";
		int pNum = 0; //assigned integer to products
		int uNum = 0; //assigned integer to users
		String result_item = "";
		String result_user = "";
		while ((line = reader.readLine()) != null) {
			// split everyline
			String[] tmp = line.split(":");
			if (tmp[0].equalsIgnoreCase("product/productId")) { //extract productIds
				//check if this product has already been assigned an integer
				//if it is, then search in map and find the integer
				//if not, then assign a new integer to this product and save into map
				if (pMap.containsKey(tmp[1].trim())) {
					result_item = pMap.get(tmp[1].trim()) + ",";
				}else {
					++pNum;
					result_item = pNum + ",";
					pMap.put(tmp[1].trim(), pNum);					
				}				
			} else if (tmp[0].equalsIgnoreCase("review/userId")) { //extract userId
				//check if this user has already been assigned an integer
				//if it is, then search in map and find the integer
				//if not, then assign a new integer to this product and save into map
				if (uMap.containsKey(tmp[1].trim())) {
					result_user = uMap.get(tmp[1].trim()) + ",";
				}else {
					++uNum;
					result_user = uNum + ",";
					uMap.put(tmp[1].trim(), uNum);					
				}
			}else if(tmp[0].equalsIgnoreCase("review/score")) { //extract review scores
					writer.write(result_user + result_item + tmp[1].trim());
					writer.newLine();
					result_user = "";
					result_item = "";
			}
		}


		//store hashmap
		BufferedWriter writer2 = new BufferedWriter(new FileWriter(new File("pOutput.txt")));
		BufferedWriter writer3 = new BufferedWriter(new FileWriter(new File("uOutput.txt")));
		
		//save users' Ids and assinged integer into storage
		for(String key:uMap.keySet()) {
			writer3.write(key + "\t" + uMap.get(key));
			writer3.newLine();
		}
		//save products' Ids and assinged integer into storage
		for(String key:pMap.keySet()) {
			writer2.write(key + "\t" + pMap.get(key));
			writer2.newLine();
		}


		reader.close();
		writer.close();
		writer2.close();
		writer3.close();

	}
	

}

import java.io.*;
import java.util.*;

/**
 * 
 * @author Group 15
 * 
 * This class can help to improve the accuracy of our results. Since some items have less than ten recommendations, 
 * this class can find the most possible recommendations that have the highest similarities with given items.
 * If the given item has no recommendation, then we search through the whole results and see if it is a recommendation 
 * for some other items. If the given item has less than ten recommendations, then we start with its first recommended item
 * and check its recommendation. If the recommendation of the first recommended item is still not enough, we continue searching
 * on the second recommended item of that given item and we stop until we find enough recommendations or we have gone over 
 * all recommendations of the given item. 
 * 
 * This class cannot make sure that we can find ten recommendations for every given item, but it can find as many as possible.
 *
 */
public class recommendToTen {
	public static void main(String[] args) throws IOException{
		//read the whole results and given item lists
		BufferedReader reader2 = new BufferedReader(new FileReader(new File("/Users/Yahui/Desktop/BD-PA4/Cosine/Cosine_15.txt")));		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("/Users/Yahui/Desktop/BD-PA4/Cosine/Cosine_Ten_15.txt")));
		
		String curr2 = ""; 
		while((curr2 = reader2.readLine())!= null) {

			String[] split = curr2.split(",");
			String itemTarget = split[0].trim();
			//no recommended items, then search the whole results file
			if(split.length < 2) {		
				String curr1 = "";
				//keep similarities into arraylist in order to sort descendingly
				List<Double> similarity = new ArrayList<Double>();
				Map<String, Double> itemSimilarityMap = new HashMap<String, Double>();
				BufferedReader reader1 = new BufferedReader(new FileReader(new File("/Users/Yahui/Desktop/BD-PA4/Cosine/Cosine")));
				while((curr1 = reader1.readLine()) != null) {
					//if we find the given item is recommended to other items, then these two items 
					if(curr1.substring(curr1.indexOf("\t") + 1, curr1.lastIndexOf("\t")).trim().equals(itemTarget)){
						//add this item into map and arraylist
						String[] tmp = curr1.split("\t");
						similarity.add(Double.parseDouble(tmp[2].trim()));
						itemSimilarityMap.put(tmp[0].trim(), Double.parseDouble(tmp[2].trim()));
					}
				}
				reader1.close();
				//sort
				Collections.sort(similarity);
				Collections.reverse(similarity);				
				//select from items with the highest similarities
				writer.write(itemTarget + ",");
				for(Double d : similarity) {
					for (String s:itemSimilarityMap.keySet()) {
						if(itemSimilarityMap.get(s).equals(d)) {
							writer.write(s + ",");
							itemSimilarityMap.remove(s);
							break;
						}
					}
				}
				writer.newLine();
				
			}else if(split.length < 11) { //if less than 10 recommended items, then go through the recommendations of its recommended items
				//find how many recommendation we need
				int recommendNeeded = 11 - split.length;
				writer.write(curr2);
				int i = 1;
				List<String> similarity = new ArrayList<String>(); 
				//do while we still want more recommendations and there still exists some recommendation of given items
				while(similarity.size() < recommendNeeded && i < split.length) {
					//find in the list where has all items with corresponding recommendations
					BufferedReader reader4 = new BufferedReader(new FileReader(new File("/Users/Yahui/Desktop/BD-PA4/Cosine/Cosine_all_item.txt")));
					String itemToSearch = split[i].trim();
					
					String line = "";
					while((line = reader4.readLine()) != null) {
						//while find records
						if(line.substring(0, line.indexOf(",")).equals(itemToSearch)) {
							String[] tmp = line.split(",");
							for(int j = 1; j < tmp.length; j++) {
								//add all recommendations into array list
								similarity.add(tmp[j].trim().substring(0, tmp[j].length() - 1));
							}
							break;
						}
					}
					++i;
					reader4.close();
				}
				
				//write new recommended items
				int limit = Math.min(recommendNeeded, similarity.size());
				//chose recommendations according to recommendation we need
				//or we print all new recommendations we find if new recommendations is less than the number we need
				for(int k = 0; k < limit; k++) {
					writer.write(similarity.get(k) + ",");
				}
				writer.newLine();
			}
			else {
				writer.write(curr2);
				writer.newLine();
			}
		}
		reader2.close();
		writer.close();
	}
	
	
}

import java.io.*;
import java.util.*;
/**
 * 
 * @author Group 15
 * 
 * This class is used to process item-based recommendation results got from Mahout (cluster). 
 * It can select at most ten recommendations for given items, and output in descending orders.
 *
 */
public class clusterToResult {
	public static void main(String args[]) throws IOException {
		for (int i = 1; i < 19; i++) {
			
			//read the whole results we got from cluster. Here, e.g, we use results with Cosine Similarity
			//read the items we tried to find, that is given input file for all 19 hadoop groups
			BufferedReader reader = new BufferedReader(new FileReader(new File("/Users/Yahui/Desktop/BD-PA4/Cosine/Cosine")));
			BufferedReader reader3 = new BufferedReader(new FileReader(new File("/Users/Yahui/Desktop/BD-PA4/Data/Group" + i + "_items.txt")));
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("/Users/Yahui/Desktop/BD-PA4/Cosine/Cosine_"+ i +".txt")));

			
			// read target 100 items into list
			List<String> target = new ArrayList<String>();
			String currentLine3 = "";
			while ((currentLine3 = reader3.readLine()) != null) {
				target.add(currentLine3.trim());
			}

			//for every different item, keep a record of its recommended items with similarities
			//keep a record of the output such that they would print as given order
			Map<String, Double> mapRS = new HashMap<String, Double>();
			List<Double> similarity = new ArrayList<Double>();
			Map<String, List<String>> result = new HashMap<String, List<String>>();
			String prev = target.get(0);
			String curr = "";

			//start reading whole results
			String currentLine = "";
			while ((currentLine = reader.readLine()) != null) {
				String[] token = currentLine.split("\t");
				curr = token[0].trim();
				
				//when the current line we read in is the recommendation for a new item
				//we write most ten recommendations for the previous item according to similarities
				if (!curr.equals(prev) && !similarity.isEmpty()) {
					// sort similarities from high to low
					Collections.sort(similarity);
					Collections.reverse(similarity);
					// select the first ten and store to result
					List<String> subResult = new ArrayList<String>();
					List<Double> tmp = new ArrayList<Double>();
					if (similarity.size() > 10) {
						tmp = similarity.subList(0, 10);
					} else {
						tmp = similarity;

					}

					// find corresponding recommended item Id in map
					for (Double d : tmp) {
						for (String s : mapRS.keySet()) {
							if (mapRS.get(s).equals(d)) {
								subResult.add(s);
								mapRS.remove(s);
								break;
							}
						}
					}

					result.put(prev, subResult);
					//clear both hash maps, getting prepared for the new item
					mapRS.clear();
					similarity.clear();
					prev = curr;
					// map<itemRecommend, similarity>
					//if the new item we read is included in our given item lists, we read in and put into map
					if (target.contains(curr)) {
						mapRS.put(token[1].trim(),
								Double.parseDouble(token[2].trim()));
						similarity.add(Double.parseDouble(token[2].trim()));

					}
				} else if (target.contains(curr)) { //similarly, if the item we read in is included in given item lists, read in and put into map
					double simil = Double.parseDouble(token[2].trim());
					mapRS.put(token[1].trim(), simil);
					similarity.add(simil);
					prev = curr;
				} else { //if it is not in the given list, we skip this item
					prev = curr;
				}
			}

			// write results
			for (String s : target) {
				writer.write(s + ",");
				if (result.containsKey(s)) {
					for (String ss : result.get(s)) {
						writer.write(ss + ",");
					}
				}
				writer.newLine();
			}

			reader.close();
			reader3.close();
			writer.close();
		}

	}
}

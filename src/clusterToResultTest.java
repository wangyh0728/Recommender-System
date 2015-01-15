import java.io.*;
import java.util.*;

public class clusterToResultTest {
	public static void main(String args[]) throws IOException{
		for(int i =1; i < 2; i++) {
			BufferedReader reader = new BufferedReader(new FileReader(new File("/Users/Yahui/Desktop/BD-PA4/Cosine/Cosine")));

			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("/Users/Yahui/Desktop/BD-PA4/Cosine/Cosine_15_inter_output.txt")));
			
			//	BufferedReader reader2 = new BufferedReader(new FileReader(new File("/Users/Yahui/Desktop/BD-PA4/pOutput.txt")));
				
				BufferedReader reader3 = new BufferedReader(new FileReader(new File("/Users/Yahui/Desktop/BD-PA4/Cosine/Cosine_25_inter_input.txt")));

				//read target 100 items into list
				List<String> target = new ArrayList<String>();
				String currentLine3 = "";
				while((currentLine3 = reader3.readLine())!=null) {
					target.add(currentLine3.trim());
				}
				
				//
				String prev = target.get(0);
				String curr = "";
				Map<String, Double> mapRS = new HashMap<String, Double>();
				List<Double> similarity = new ArrayList<Double>();
				Map<String, List<String>> result = new HashMap<String, List<String>>();
				int test = 0;
				
				String currentLine = "";
				while((currentLine = reader.readLine())!=null) {
					String[] token = currentLine.split("\t");
					curr = token[0].trim();	

				
					if(!curr.equals(prev) && !similarity.isEmpty()) {
						//sort 
						Collections.sort(similarity);
						Collections.reverse(similarity);
						//select the first ten and store to result
						List<String> subResult = new ArrayList<String>();
						List<Double> tmp = new ArrayList<Double>();
						if(similarity.size() > 10) {
							tmp = similarity.subList(0, 10);
						}else {
							tmp = similarity;
						
						}
						
						//find corresponding recommended item Id in map
						for(Double d : tmp) {
							for(String s : mapRS.keySet()) {
								if(mapRS.get(s).equals(d)) {	
									subResult.add(s);
									mapRS.remove(s);
									break;
								}
							}
						}
						
						result.put(prev, subResult);			
						//
						mapRS.clear();
						similarity.clear();
						prev = curr;
						//map<itemRecommend, similarity>
						if(target.contains(curr)) {
							mapRS.put(token[1].trim(), Double.parseDouble(token[2].trim()));
							similarity.add(Double.parseDouble(token[2].trim()));					
							
						}
					}else if(target.contains(curr)){
						double simil = Double.parseDouble(token[2].trim());
						mapRS.put(token[1].trim(), simil);
						similarity.add(simil);
						prev = curr;
					}else {
						prev = curr;
					}
				}
				
				//write results
				for(String s: target) {
				//	System.out.println(result.toString() + " " );//+ result.get(s).size());
					writer.write(s + " ");
					if(result.containsKey(s)) {
						for(String ss: result.get(s)) {
							writer.write(ss + ", ");
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

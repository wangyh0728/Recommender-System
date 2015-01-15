import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class ConvertBackItems {
	public static Map<String, String> productMap = new HashMap<String, String>();
	public static void main(String[] args) throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader(new File ("/Users/Yahui/Desktop/BD-PA4/pOutput.txt")));
		String currentLine = "";
		while((currentLine = reader.readLine())!=null) {
			int split = currentLine.indexOf("\t");
			productMap.put(currentLine.substring(split + 1).trim(), currentLine.substring(0,split).trim());
		}
		reader.close();
		convertBack();
	}

	public static void convertBack() throws FileNotFoundException {
		for (int i = 1; i < 20; i ++) {
			Scanner input = new Scanner(new File("/Users/Yahui/Desktop/BD-PA4/Pearson/Pearson_" + i + ".txt"));
			PrintStream pf = new PrintStream(new File("/Users/Yahui/Desktop/BD-PA4/Pearson/Final" + i + "_items.txt"));
			while(input.hasNextLine()) {
				String line = input.nextLine();
				String[] splits = line.split(" ");
				pf.print(productMap.get(splits[0]) + " "); 
				if (splits.length >= 2) {
					for (int j=1; j< splits.length; j++) {
						String item = splits[j].substring(0, splits[j].length()-1);
						if (productMap.containsKey(item)) {
							pf.print(productMap.get(item) + ", ");
						}
						else {
							System.out.println("Does not contain " + item);
						}
					}		
				}
				pf.println();
			}
			pf.close();
			input.close();
		}
	}

}

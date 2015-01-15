import java.io.*;
import java.util.*;

public class deleteSpace {
	public static void main(String[] args) throws IOException {
		
	//	for(int i = 1; i < 20; i++) {
			Scanner s = new Scanner(new File("/Users/Yahui/Desktop/BD_PA4_Result-1/User_Based_Result/Final_UserBased_LogLikelihood.txt"));
			BufferedWriter w = new BufferedWriter(new FileWriter(new File("/Users/Yahui/Desktop/BD_PA4_Result-1/User_Based_Result/LogLikelihood_UserBased_Final.txt")));
		
			while(s.hasNextLine()) {
				String line = s.nextLine();
				String[] split = line.split(" ");
				w.write(split[0].trim() + ",");
				for(int j = 1; j < split.length - 1; j++) {
					if(!split[j].equals("")) {
						w.write(split[j].trim());
					}
				}
				w.write(split[split.length -1].substring(0,split[split.length-1].length() - 1));
				w.newLine();
			}
			w.close();
	//	}
		
		
	}
}

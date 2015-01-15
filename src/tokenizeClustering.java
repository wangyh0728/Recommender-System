//package code.lemma;


/*
 * Consider other ways of removing stop words 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
/**
 * 
 * @author Group 15
 * 
 * This class is to tokenize the given Amazon Review file
 *
 */
public class tokenizeClustering {
	// build a hash set of stop words
	final static Set<String> stopWords = new HashSet<String>();
	
	// high frequency words specified for this corpus
    final static String[] wordsList = new String[] {"product", "good", "great", "price", "order", "bought", "buy", "thing", "nice", "make", "made", "amazon", "excellent", "purchas", "recommend", "recommendation", "quality", "take", "adequate"};


	public static void main(String[] args) throws IOException {
		
		for(String highWord: wordsList) {
			stopWords.add(highWord);
		}
		
		File f = new File("stopword.txt");
		BufferedReader reader1 = new BufferedReader(new FileReader(f));
		String currentLine = null;
		while ((currentLine = reader1.readLine()) != null) {
			stopWords.add(currentLine);
		}
		reader1.close();
		
		File f2 = new File ("pa4_review_tokenized_all.txt");
		BufferedReader reader2 = new BufferedReader(new FileReader(f2));
		BufferedWriter writer  = new BufferedWriter(new FileWriter(new File("pa4_review_tokenized_all_2.txt")));
		
		BufferedWriter writer2 = new BufferedWriter(new FileWriter(new File("pa4_item_all_2.txt")));
		String current = null;
		while ((current = reader2.readLine())!= null) {
			String[] array = current.split(":");
			
			if(array[0].trim().equals("product/productId")) { //keep a record of corresponding reivew
				writer2.write(array[1].trim());
				writer2.newLine();
			}else if(array[0].trim().equals("review/text")) {
				//tokenize and lemmatize give reviews
				List<String> tmpR = tokenize(array[1].trim());
				for(String tmp:tmpR) {
					writer.append(tmp + " ");
				}
				writer.newLine();
			}
			
			
		}

		
		reader2.close();
		writer.close();
		writer2.close();
	}
	 /**
     * Tokenizing sentence into a list of words
     * @param sentence string
     * @return an ArrayList of lemmas
     * @throws FileNotFoundException
     */
	public static List<String> tokenize(String sentence) throws FileNotFoundException {
		List<String> result = new ArrayList<String>();

		// separate the words into tokens and take out unnecessary symbols
		StringTokenizer st = new StringTokenizer(sentence," ,.:;?!+=_`\n\t\r-|<>@#$%^&*()[]{}\"\\/1234567890");
		// ...I removed it below using the lemmatizer.
		while (st.hasMoreTokens()) {
			String token = st.nextToken().toLowerCase();
			// take out the stop words
			if (!stopWords.contains(token)) {
				// lemmatizing input token
				String word = lemmatizing(token);
				if (word.length()>1) {
					if (!word.substring(0,1).matches("[a-z]+"))
						word = word.substring(1);
					if (!word.substring(word.length()-1, word.length()).matches("[a-z]+"))
						word = word.substring(0, word.length()-1);
				}
				result.add(word);
			}
		}
		return result;
	}

    /**
     * This function determines if the input character is a vowel letter
     * @param a single character
     * @return boolean value if it is a vowel
     */
	private static boolean isVowel(char c) {
		return (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u');
	}

	/**
	 * This function determines if the input character is a stop or nasal
	 * consonant
     * @param a single character
     * @return boolean value if it is a certain consonant
	 */
	private static boolean isConsonant(char c) {
		return (c == 'p' || c == 't' || c == 'k' || c == 'b' || c == 'd'
				|| c == 'g' || c == 'm' || c == 'n');
	}

	/**
	 * This function lemmatizes the input token
     * @param a string token
     * @return its lemmatized form
	 */
	private static String lemmatizing(String token) {
		int len = token.length();
		//remove apostrophe('s)
		if(len > 2) {
			if(token.substring(len-3).equals("ful") || token.substring(len-2).equals("ly")) {
				token = "";
			}
		}
		if (len > 2 && token.substring(len - 2, len).equals("'s"))
			token = token.substring(0, len - 2);
		
		//remove apostrophe(')
		else if (token.substring(len - 1).equals("'"))
			token = token.substring(0, len - 1);

		//plurality and present tense removal
		else if (len > 2 && token.charAt(len - 1) == 's') {
			// eg. cherries -> cherry
			if (len > 3 && token.substring(len - 3, len - 1).equals("ie"))
			{
				token = token.substring(0, len - 3) + "y";
			} else if (len > 3
					&& token.substring(len - 3, len - 1).equals("ve")) {
				// eg. leaves -> leaf
				if (len > 5 && isVowel(token.charAt(len - 4))
						&& isVowel(token.charAt(len - 5)))
				{
					token = token.substring(0, len - 3) + "f";
				}
				// eg. lives -> life
				else if (len > 3) {
					token = token.substring(0, len - 3) + "fe";
				}
			}
			// eg. boxes -> box, passes -> pass
			else if (len > 4
					&& token.charAt(len - 2) == 'e'
					&& token.charAt(len - 1) == 's'
					&& !(isVowel(token.charAt(len - 3)) && (token
							.charAt(len - 3) == 'x'
							|| token.charAt(len - 3) == 'z'
							|| token.charAt(len - 3) == 'o'
							|| token.substring(len - 4, len - 2).equals("sh")
							|| token.substring(len - 4, len - 2).equals("ss") || token
							.substring(len - 4, len - 2).equals("ch")))) 
			{
				token = token.substring(0, len - 2);
			}
			// eg. dogs -> dog, gets -> get
			else if (len > 2
					&& !(token.charAt(len - 2) == 's' || (token.charAt(len - 2) == 'i'))) {
				token = token.substring(0, len - 1);
			}

			// capturing small words
			else if (len < 4)
			{
				token = token.substring(0, len - 1);
			}

		}
		//past participle removal
		else if (len > 5 && token.charAt(len - 1) == 'd'
				&& token.charAt(len - 2) == 'e') {
			// eg. tanned -> tan
			if (token.charAt(len - 3) == token.charAt(len - 4)
					&& isConsonant(token.charAt(len - 3))
					&& isConsonant(token.charAt(len - 4)))
				token = token.substring(0, len - 3);
			// eg. carried -> carry
			else if (token.charAt(len - 3) == 'i')
				token = token.substring(0, len - 3) + "y";
			// eg. fired--> fire
			else if (isConsonant(token.charAt(len - 3))
					&& isVowel(token.charAt(len - 4))
					&& !(isVowel(token.charAt(len - 5)) || (len > 5 && token
							.substring(len - 5, len - 2).equals("ang")))
					|| (len > 5 && token.substring(len - 5, len - 2).equals(
							"ais")))
				token = token.substring(0, len - 1);
			// eg. showed -> show
			else
				token = token.substring(0, len - 2);
		}
		//gerund form removal
		else if (len > 5 && token.charAt(len - 3) == 'i'
				&& token.charAt(len - 2) == 'n' && token.charAt(len - 1) == 'g') {
			// eg. getting -> get
			if (len > 6 && token.charAt(len - 4) == token.charAt(len - 5)
					&& isConsonant(token.charAt(len - 4))
					&& isConsonant(token.charAt(len - 5)))
				token = token.substring(0, len - 4);
			// eg. leaving -> leave
			else if ((!(isVowel(token.charAt(len - 4)))
					&& isVowel(token.charAt(len - 5)) && !isVowel(token
						.charAt(len - 6)))
					|| token.equals("leaving")
					|| token.equals("housing")
					|| (len > 6 && token.substring(len - 6, len - 3).equals(
							"ang"))
					|| (len > 6 && token.substring(len - 6, len - 3).equals(
							"ais")))// ng is for words like changing --> change
				token = token.substring(0, len - 3) + "e";
			// eg. going -> go
			else
				token = token.substring(0, len - 3);
		}
		return token;
	}
    
}

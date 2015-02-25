package counterror;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CleanRunError {
	public static void main(String[] args) {

		String[] postSplits = { "aa", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar", "as",
				"at", "au", "av", "aw", "ax", "ay", "az", "ba", "bb", "bc", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bk", "bl", "bm", "bn", "bo",
				"bp", "bq", "br", "bs", "bt", "bu", "bv", "bw", "bx", "by", "bz", "ca", "cb", "cc", "cd", "ce", "cf", "cg", "ch", "ci", "cj", "ck",
				"cl", "cm", "cn", "co", "cp", "cq", "cr", "cs", "ct", "cu", "cv", "cw", "cx", "cy", "cz", "da", "db", "dc", "dd", "de", "df", "dg",
				"dh", "di", "dj", "dk", "dl", "dm", "dn", "do", "dp", "dq", "dr", "ds", "dt", "du", "dv" };

		String inFile = "";
		String outFile = "";
		
		for (String post : postSplits) {
			System.out.println("processing post" + post + "......");
			inFile = "C:\\StackOverflow\\JS\\runError\\post" + post + "_error.txt";
			outFile = "C:\\StackOverflow\\JS\\runErrorClean\\post" + post+ "_error.txt";

			BufferedReader br;
			BufferedWriter writer;
			try {
				br = new BufferedReader(new FileReader(new File(inFile)));
				writer = new BufferedWriter(new FileWriter(new File(outFile)));
			
				String temp;
				while ((temp = br.readLine()) != null) {
					if(temp.contains("Error: ")){
						writer.write(temp + '\n');
					}
				}
				writer.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			

		}

		
	

	}

}

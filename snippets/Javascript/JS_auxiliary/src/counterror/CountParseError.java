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

public class CountParseError {
	public static void main(String[] args) {

		String[] postSplits = { "aa", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar", "as",
				"at", "au", "av", "aw", "ax", "ay", "az", "ba", "bb", "bc", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bk", "bl", "bm", "bn", "bo",
				"bp", "bq", "br", "bs", "bt", "bu", "bv", "bw", "bx", "by", "bz", "ca", "cb", "cc", "cd", "ce", "cf", "cg", "ch", "ci", "cj", "ck",
				"cl", "cm", "cn", "co", "cp", "cq", "cr", "cs", "ct", "cu", "cv", "cw", "cx", "cy", "cz", "da", "db", "dc", "dd", "de", "df", "dg",
				"dh", "di", "dj", "dk", "dl", "dm", "dn", "do", "dp", "dq", "dr", "ds", "dt", "du", "dv" };

		String inFile = "";
		int totalCount=0;
		Map<String, Integer> errorMap = new HashMap<String, Integer>();

		for (String post : postSplits) {
			System.out.println("processing post" + post + "......");
			inFile = "C:\\StackOverflow\\JS\\parseError\\post" + post + "_error.txt";

			BufferedReader br;
			
			try {
				br = new BufferedReader(new FileReader(new File(inFile)));
				
				String temp;
				while ((temp = br.readLine()) != null) {
					
					String errMsg = temp;

					if (errorMap.containsKey(errMsg)) {
						errorMap.put(errMsg, errorMap.get(errMsg) + 1);
					} else {
						errorMap.put(errMsg, 1);
					}
					
					totalCount +=1;
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		ArrayList<Map.Entry<String, Integer>> errorList = new ArrayList<Map.Entry<String, Integer>>(errorMap.entrySet());
		Collections.sort(errorList, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> arg0, Entry<String, Integer> arg1) {
				if (arg0.getValue() - arg1.getValue() > 0)
					return -1;
				else if (arg0.getValue() - arg1.getValue() == 0) {
					return 0;
				} else
					return 1;
			}
		});

		BufferedWriter writer1;
		try {
			writer1 = new BufferedWriter(new FileWriter(new File("C:\\StackOverflow\\JS\\parseError\\errorCount.txt")));
			for (Map.Entry<String, Integer> entry : errorList) {
				writer1.write(entry.getKey() + "Di2015UniqueSeparator" + entry.getValue() + "\n");
			}
			writer1.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(totalCount);
	}

}

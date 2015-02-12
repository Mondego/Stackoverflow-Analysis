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



public class CommonParseError {

	public static void main(String[] args) {

		 String[] postSplits = { "aa", "ab", "ac", "ad", "ae", "af", "ag",
		 "ah",
		 "ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar",
		 "as", "at", "au", "av", "aw", "ax", "ay", "az", "ba", "bb",
		 "bc", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bk", "bl",
		 "bm", "bn", "bo", "bp", "bq", "br", "bs", "bt", "bu", "bv",
		 "bw", "bx", "by", "bz", "ca", "cb", "cc", "cd", "ce", "cf",
		 "cg", "ch", "ci", "cj", "ck", "cl", "cm", "cn", "co", "cp",
		 "cq", "cr", "cs", "ct", "cu", "cv", "cw", "cx", "cy", "cz",
		 "da", "db", "dc", "dd", "de", "df", "dg", "dh", "di", "dj",
		 "dk", "dl", "dm", "dn", "do", "dp", "dq", "dr", "ds", "dt",
		 "du", "dv" };
		
		String inFile = "";

		Map<String, Integer> errorMap = new HashMap<String, Integer>();
		Map<String, String> msgMap = new HashMap<String, String>();

		for (String post : postSplits) {
			System.out.println("processing post" + post + "......");
			//inFile = "C:\\StackOverflow\\C#\\parseErrorInitial\\post"+ post + "_error.txt";
			inFile = "C:\\StackOverflow\\C#\\parseErrorAfterRemoves\\post"+ post + "_error.txt";
			//inFile = "C:\\StackOverflow\\C#\\parseErrorAfterFix2\\post"+ post + "_error.txt";

			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(new File(inFile)));
				String[] nextLine;
				String temp;
				while ((temp = br.readLine()) != null) {
					nextLine = temp.split(":");
					String errMsg = "";
					String errNo = "";
					if (nextLine.length > 2) {
						errNo = nextLine[1];
						for (int i = 2; i < nextLine.length; i++) {
							errMsg += nextLine[i];
						}
						
						msgMap.put(errNo, errMsg);
						
						if (errorMap.containsKey(errNo)) {
							errorMap.put(errNo, errorMap.get(errNo) + 1);
						} else {
							errorMap.put(errNo, 1);
						}
					}

				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		ArrayList<Map.Entry<String, Integer>> errorList = new ArrayList<Map.Entry<String, Integer>>(
				errorMap.entrySet());
		Collections.sort(errorList,
				new Comparator<Map.Entry<String, Integer>>() {
					public int compare(Entry<String, Integer> arg0,
							Entry<String, Integer> arg1) {
						if (arg0.getValue() - arg1.getValue() > 0)
							return -1;
						else if (arg0.getValue() - arg1.getValue() == 0) {
							return 0;
						} else
							return 1;
					}
				});

		BufferedWriter writer1;
		BufferedWriter writer2;
		try {
			//writer1 = new BufferedWriter(new FileWriter(new File("C:\\StackOverflow\\C#\\parseErrorInitial\\errorCount.txt")));
			writer1 = new BufferedWriter(new FileWriter(new File("C:\\StackOverflow\\C#\\parseErrorAfterRemoves\\errorCount.txt")));
			//writer1 = new BufferedWriter(new FileWriter(new File("C:\\StackOverflow\\C#\\parseErrorAfterFix2\\errorCount.txt")));
			for (Map.Entry<String, Integer> entry : errorList) {
				writer1.write(entry.getKey() + "Di2015UniqueSeparator"+ entry.getValue() + "\n");
			}
			writer1.close();
			
			//writer2 = new BufferedWriter(new FileWriter(new File("C:\\StackOverflow\\C#\\parseErrorInitial\\errMsg.txt")));
			writer2 = new BufferedWriter(new FileWriter(new File("C:\\StackOverflow\\C#\\parseErrorAfterRemoves\\errMsg.txt")));
			//writer2 = new BufferedWriter(new FileWriter(new File("C:\\StackOverflow\\C#\\parseErrorAfterFix2\\errMsg.txt")));
			for (Map.Entry<String, String> entry : msgMap.entrySet()) {
				writer2.write(entry.getKey() + "Di2015UniqueSeparator"+ entry.getValue() + "\n");
			}
			writer2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
}



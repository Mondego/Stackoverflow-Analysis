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

public class CountTypeErrors {
	public static void main(String[] args) {
		Map<String, Integer> errorMap = new HashMap<String, Integer>();

		String[] types = { "not a function", "is undefined", "is null", "is not a constructor", "missing argument", "invalid", "requires more than", "redeclaration of", "not an object",
				"null has no properties", "undefined has no properties", "can't convert", "properties may not be accessed on strict mode functions", 
				"should have array-like objects", "must not specify a value", "called on incompatible undefined", "not a non-null object", "Must pass start and end arguments", "can't redefine"};
		for (String s : types) {
			errorMap.put(s, 0);
		}

		String[] postSplits = { "aa", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar", "as", "at", "au", "av", "aw", "ax", "ay", "az", "ba", "bb",
				"bc", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bk", "bl", "bm", "bn", "bo", "bp", "bq", "br", "bs", "bt", "bu", "bv", "bw", "bx", "by", "bz", "ca", "cb", "cc", "cd", "ce", "cf",
				"cg", "ch", "ci", "cj", "ck", "cl", "cm", "cn", "co", "cp", "cq", "cr", "cs", "ct", "cu", "cv", "cw", "cx", "cy", "cz", "da", "db", "dc", "dd", "de", "df", "dg", "dh", "di", "dj",
				"dk", "dl", "dm", "dn", "do", "dp", "dq", "dr", "ds", "dt", "du", "dv" };

		String inFile = "";

		int totalCount = 0;
		int typeCount = 0;

		for (String post : postSplits) {
			System.out.println("processing post" + post + "......");
			inFile = "C:\\StackOverflow\\JS\\runErrorClean\\post" + post + "_error.txt";
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(new File(inFile)));

				String temp;
				while ((temp = br.readLine()) != null) {
					String errMsg = temp;
					totalCount++;

					if(errMsg.contains("TypeError: ")){
						typeCount++;
						for (String s : types) {
							if (errMsg.contains(s)) {
								errorMap.put(s, errorMap.get(s) + 1);
								break;
							}
						}
					
					}
					

				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		int maptotal = 0;
		for (Map.Entry<String, Integer> entry : errorMap.entrySet()) {
			System.out.println("TypeError: " + entry.getKey() + "\t" + entry.getValue());
			maptotal += entry.getValue();
		}
		
		System.out.println(totalCount);
		System.out.println(typeCount);
		System.out.println(maptotal);
		

	}

}

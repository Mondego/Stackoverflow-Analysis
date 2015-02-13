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

public class CountReferenceErrors {
	public static void main(String[] args) {

		String[] postSplits = { "aa", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar", "as", "at", "au", "av", "aw", "ax", "ay", "az", "ba", "bb",
				"bc", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bk", "bl", "bm", "bn", "bo", "bp", "bq", "br", "bs", "bt", "bu", "bv", "bw", "bx", "by", "bz", "ca", "cb", "cc", "cd", "ce", "cf",
				"cg", "ch", "ci", "cj", "ck", "cl", "cm", "cn", "co", "cp", "cq", "cr", "cs", "ct", "cu", "cv", "cw", "cx", "cy", "cz", "da", "db", "dc", "dd", "de", "df", "dg", "dh", "di", "dj",
				"dk", "dl", "dm", "dn", "do", "dp", "dq", "dr", "ds", "dt", "du", "dv" };

		String inFile = "";
		int notDefined = 0;
		int invalidAssignment = 0;
		int undeclared = 0;
		int totalCount = 0;
		int totalReferenceError = 0;

		Map<String, Integer> errorMap = new HashMap<String, Integer>();

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

					if (errMsg.contains("ReferenceError: ")) {
						totalReferenceError++;
						if(errMsg.contains("is not defined")){
							notDefined++;
						}else if(errMsg.contains("invalid assignment left-hand side")){
							invalidAssignment++;	
						}else if(errMsg.contains("assignment to undeclared variable")){
							undeclared++;
						}else{
							System.out.println(errMsg);
						}
						
					}

				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		System.out.println("ReferenceError: xxx is not defined\t" + notDefined);
		System.out.println("ReferenceError: invalid assignment left-hand side\t" + invalidAssignment);
		System.out.println("ReferenceError: assignment to undeclared variable xxx\t" + undeclared);
		System.out.println("ReferenceError: [other]\t" + (totalReferenceError-notDefined-invalidAssignment-undeclared));
		System.out.println("total error counts: "+totalCount);
		System.out.println("total reference error counts: "+totalReferenceError);

	}

}

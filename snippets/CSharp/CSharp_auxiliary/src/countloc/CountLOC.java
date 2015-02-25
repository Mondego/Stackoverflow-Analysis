package countloc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CountLOC {

	public static void main(String[] args) {

		String[] postSplits = { "aa", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar", "as", "at", "au", "av", "aw", "ax", "ay", "az", "ba", "bb",
				"bc", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bk", "bl", "bm", "bn", "bo", "bp", "bq", "br", "bs", "bt", "bu", "bv", "bw", "bx", "by", "bz", "ca", "cb", "cc", "cd", "ce", "cf",
				"cg", "ch", "ci", "cj", "ck", "cl", "cm", "cn", "co", "cp", "cq", "cr", "cs", "ct", "cu", "cv", "cw", "cx", "cy", "cz", "da", "db", "dc", "dd", "de", "df", "dg", "dh", "di", "dj",
				"dk", "dl", "dm", "dn", "do", "dp", "dq", "dr", "ds", "dt", "du", "dv" };

		String inFile = "";

		int totalCount = 0;
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(new File("C:\\StackOverflow\\C#\\countCSharpLOC.txt")));

			for (String post : postSplits) {
				System.out.println("processing post" + post + "......");

				inFile = "C:\\StackOverflow\\C#\\snippets\\post" + post + "_snippet.txt";

				BufferedReader br;

				br = new BufferedReader(new FileReader(new File(inFile)));
				String[] nextLine;
				String temp;
				while ((temp = br.readLine()) != null) {
					nextLine = temp.split("Di2015NewLine");
					totalCount++;
					int loc = nextLine.length;
					writer.write(loc + "\t");
					if (totalCount % 10 == 0) {
						writer.write("\n");
					}
				}
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(totalCount);

	}

}

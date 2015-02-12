package remove;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class RemoveSingleWord {

	/*
	 * Remove single word code blocks
	 */
	public static String remove(String inFile, String outFile) {
		int line = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(inFile)));

			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outFile)));
			String[] nextLine;
			String temp;
			while ((temp = br.readLine()) != null) {
				nextLine = temp.split("Di2015UniqueSeparator");
				// nextLine[] is an array of values from the line
				if (nextLine.length > 1) {
					String find = "[^\\w]";
					Pattern pattern = Pattern.compile(find);

					Matcher matcher = pattern.matcher(nextLine[1]);
					if (matcher.find()) {
						// System.out.println(nextLine[0] + "\t" + nextLine[1]);
						writer.write(nextLine[0] + "Di2015UniqueSeparator" + nextLine[1] + "\n");
						line++;
					}
				}

			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return String.valueOf(line);
	}

	public static void main(String[] args) throws IOException {

		String[] postSplits = { "aa", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar", "as",
				"at", "au", "av", "aw", "ax", "ay", "az", "ba", "bb", "bc", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bk", "bl", "bm", "bn", "bo",
				"bp", "bq", "br", "bs", "bt", "bu", "bv", "bw", "bx", "by", "bz", "ca", "cb", "cc", "cd", "ce", "cf", "cg", "ch", "ci", "cj", "ck",
				"cl", "cm", "cn", "co", "cp", "cq", "cr", "cs", "ct", "cu", "cv", "cw", "cx", "cy", "cz", "da", "db", "dc", "dd", "de", "df", "dg",
				"dh", "di", "dj", "dk", "dl", "dm", "dn", "do", "dp", "dq", "dr", "ds", "dt", "du", "dv" };

		try {
			String inFile = "";
			String outFile = "";

			BufferedWriter writer = new BufferedWriter(new FileWriter(
					new File("C:\\StackOverflow\\C#\\removeSingleWord\\snippetCountAfterRemove.txt")));

			for (String post : postSplits) {
				System.out.println("processing post" + post + "......");
				inFile = "C:\\StackOverflow\\C#\\snippets\\post" + post + "_snippet.txt";
				outFile = "C:\\StackOverflow\\C#\\removeSingleWord\\post" + post + "_remove.txt";

				String[] entry = new String[2];
				entry[0] = "post" + post;
				entry[1] = remove(inFile, outFile);

				writer.write(entry[0] + "Di2015UniqueSeparator" + entry[1] + "\n");

			}
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

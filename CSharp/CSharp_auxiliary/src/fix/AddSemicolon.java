package fix;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AddSemicolon {
	/*
	 * Fix 2: Adds ";"'s
	 */
	public static String Fix2(String inFile, String outFile) {
		int fixCount = 0;
		Boolean flag = false;

		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(inFile)));
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outFile)));
			String temp;

			while ((temp = br.readLine()) != null) {
				// nextLine[] is an array of values from the line

				String[] nextLine = temp.split("Di2015UniqueSeparator");

				if (nextLine.length > 1) {
					flag = false;
					String snippet = nextLine[1];

					String codeAlt = "";
					String[] codeLines = snippet.split("(Di2015NewLine)+");

					for (String line : codeLines) {
						if (line.contains("{") == false 
								&& line.contains("(") == false 
								&& line.contains("if") == false
								&& line.contains("else") == false 
								&& line.contains("while") == false 
								&& line.contains("for") == false
								&& line.contains("foreach") == false 
								&& line.contains("try") == false 
								&& line.contains("catch") == false
								&& line.contains("class") == false
								&& line.contains("namespace") == false 
								&& line.contains(";") == false
								&& line.endsWith("do") == false 
								&& line.endsWith("}") == false 
								&& line.endsWith("} ") == false
								&& line.endsWith("=") == false 
								&& line.endsWith("= ") == false) {
							// THE FIX
							line += "; Di2015NewLine";
							codeAlt += line;
							flag = true;
						} else {
							line += "Di2015NewLine";
							codeAlt += line;
						}

					}
					writer.write(nextLine[0] + "Di2015UniqueSeparator" + codeAlt + "\n");
					if (flag == true) {
						fixCount++;
					}
				}
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return String.valueOf(fixCount);
	}

	public static void main(String[] args) {

		String[] postSplits = { "aa", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar", "as",
				"at", "au", "av", "aw", "ax", "ay", "az", "ba", "bb", "bc", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bk", "bl", "bm", "bn", "bo",
				"bp", "bq", "br", "bs", "bt", "bu", "bv", "bw", "bx", "by", "bz", "ca", "cb", "cc", "cd", "ce", "cf", "cg", "ch", "ci", "cj", "ck",
				"cl", "cm", "cn", "co", "cp", "cq", "cr", "cs", "ct", "cu", "cv", "cw", "cx", "cy", "cz", "da", "db", "dc", "dd", "de", "df", "dg",
				"dh", "di", "dj", "dk", "dl", "dm", "dn", "do", "dp", "dq", "dr", "ds", "dt", "du", "dv" };
		try {
			String inFile = "";
			String outFile = "";
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\StackOverflow\\C#\\fix2\\fixCountForFix2.txt")));

			for (String post : postSplits) {
				System.out.println("processing post" + post + "......");
				inFile = "C:\\StackOverflow\\C#\\unparsableAfterRemoves\\post" + post + "_unparsable.txt";
				outFile = "C:\\StackOverflow\\C#\\fix2\\post" + post + "_fix2.txt";

				String[] entry = new String[2];
				entry[0] = "post" + post;
				entry[1] = Fix2(inFile, outFile);
				writer.write(entry[0] + "Di2015UniqueSeparator" + entry[1] + "\n");

			}
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * String snippet =
		 * "class Program{&#xA; if(i==0 &#xA; && j==0 &#xA; && k==0){double trans = trackBar1.Value / 5000.0  &#xA;}double trans = trackBar1.Value / 5000d  &#xA;&#xA;}"
		 * ;
		 * 
		 * String codeAlt = ""; String[] codeLines = snippet.split("(&#xA;)+");
		 * 
		 * for (String line : codeLines) { if (line.contains("{") == false &&
		 * line.contains("(") == false && line.contains("if") == false &&
		 * line.contains("else") == false && line.contains("while") == false &&
		 * line.contains("for") == false && line.contains("foreach") == false &&
		 * line.contains("try") == false && line.contains("catch") == false &&
		 * line.contains("class") == false && line.contains("namespace") ==
		 * false && line.contains(";") == false && line.endsWith(" do ") ==
		 * false && line.endsWith("}") == false && line.endsWith("=") == false )
		 * { // THE FIX line += "; \n"; codeAlt += line; } else { line += "\n";
		 * codeAlt += line; } } System.out.println(codeAlt);
		 */
	}

}

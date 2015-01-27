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


public class AddClassHeader {

	/*
	 * Fix 1: Adds "class Program { }"
	 */
	public static String Fix1(String inFile, String outFile) {
		int fixCount = 0;
		try {
			CSVReader reader = new CSVReader(new FileReader(inFile));

   		 	CSVWriter writer = new CSVWriter(new FileWriter(outFile));
			String [] nextLine;
		    while ((nextLine = reader.readNext()) != null) {
		        // nextLine[] is an array of values from the line
		    
		    	String snippet = nextLine[1];
		    	if (snippet.contains(" class ") == true
						| snippet.startsWith("class ") == true
						| snippet.contains("\nclass ") == true
						| snippet.startsWith("using ") == true
						| snippet.startsWith(" using ") == true
						| snippet.contains("\nusing ") == true
						| snippet.contains("namespace ") == true
						| snippet.contains(" namespace ") == true
						| snippet.contains("\nnamespace ") == true) {
					
				} else {
					// THE FIX
					snippet = "class Program { " + snippet + "}";	
					fixCount++;
				}
		    	String[] entries = new String[2];
		    	entries[0] = nextLine[0];
		    	entries[1] = snippet;
		    	writer.writeNext(entries);	
			}
		    writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return String.valueOf(fixCount);
			
	}
	
	public static void main(String[] args){

		String[] postSplits = {"aa", "ab", "ac", "ad", "ae", "af", "ag", "ah",
				"ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar",
				"as", "at", "au", "av", "aw", "ax", "ay", "az"};
		
		try {
			String inFile = "";
			String outFile = "";
			CSVWriter writer = new CSVWriter(new FileWriter("C:\\StackOverflow\\C#_addClassHeader\\addClassHeaderCount.csv"));
			
			for(String post : postSplits){
				System.out.println("processing post" + post + "......");
				inFile = "C:\\StackOverflow\\C#_unparsableSnippet\\post" + post + "_snippet.csv";
				outFile = "C:\\StackOverflow\\C#_addClassHeader\\post" + post + "_addClassHeader.csv";
				
				String[] entry = new String[2];
				entry[0] = "post"+post;
				entry[1] = Fix1(inFile, outFile);
				writer.writeNext(entry);
	   		 	
			}
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	
}

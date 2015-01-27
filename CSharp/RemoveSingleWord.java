import java.io.BufferedReader;
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
		String csvSplitBy = ",";
		try {
			CSVReader reader = new CSVReader(new FileReader(inFile));
//			BufferedReader br = new BufferedReader(new FileReader(new File(inFile)));
  		 	CSVWriter writer = new CSVWriter(new FileWriter(outFile));
			
   		 	String [] nextLine;
   			String temp;
//   		 	while( (temp = br.readLine()) != null){
//   		 		nextLine = temp.split(csvSplitBy);
		    while ((nextLine = reader.readNext()) != null) {
		        // nextLine[] is an array of values from the line
		    	String find = "[^\\w]";
		    	Pattern pattern = Pattern.compile(find);
//		    	nextLine[0] = nextLine[0].substring(1, nextLine[0].length()-1);
//		    	nextLine[1] = nextLine[1].substring(1, nextLine[1].length()-1);
		    	
		    	Matcher matcher = pattern.matcher(nextLine[1]);
		    	if(matcher.find()){
		    	    writer.writeNext(nextLine);	   
		    	    line++;
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

		String[] postSplits = { "aa", "ab", "ac", "ad", "ae", "af", "ag", "ah",
				"ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar",
				"as", "at", "au", "av", "aw", "ax", "ay", "az"};
		
		try {
			String inFile = "";
			String outFile = "";
			CSVWriter writer = new CSVWriter(new FileWriter("C:\\StackOverflow\\C#_removeSingleWord\\snippetCountAfterRemoveSingleWord.csv"));
			
			for(String post : postSplits){
				System.out.println("processing post" + post + "......");
				inFile = "C:\\StackOverflow\\C#_unparsableSnippet\\post" + post + "_unparsable.csv";
				outFile = "C:\\StackOverflow\\C#_removeSingleWord\\post" + post + "_remove.csv";
				
				String[] entry = new String[2];
				entry[0] = "post"+post;
				entry[1] = remove(inFile, outFile);
				writer.writeNext(entry);
	   		 	
			}
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}

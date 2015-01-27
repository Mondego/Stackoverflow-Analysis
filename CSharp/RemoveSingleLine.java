import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


public class RemoveSingleLine {
	/*
	 * Remove single line code
	 */
	public static String remove(String inFile, String outFile) {
		int line = 0;
		try {
			CSVReader reader = new CSVReader(new FileReader(inFile));
  		 	CSVWriter writer = new CSVWriter(new FileWriter(outFile));
			
   		 	String [] nextLine;
		    while ((nextLine = reader.readNext()) != null) {
		        // nextLine[] is an array of values from the line
		    	String snippet = nextLine[1];
		    	if(snippet.matches(".*&#xA.*")){
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
			CSVWriter writer = new CSVWriter(new FileWriter("C:\\StackOverflow\\C#_removeSingleLine\\snippetCountAfterRemoveSingleLine.csv"));
			
			for(String post : postSplits){
				System.out.println("processing post" + post + "......");
				inFile = "C:\\StackOverflow\\C#_unparsableSnippet\\post" + post + "_unparsable.csv";
				outFile = "C:\\StackOverflow\\C#_removeSingleLine\\post" + post + "_remove.csv";
				
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

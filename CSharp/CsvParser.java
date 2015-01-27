
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;

import au.com.bytecode.opencsv.*;

public class CsvParser {
	public static String parseCSV(String inFile, String outFile){
		int line = 0;
		String csvSplitBy = ",";
		try {
			CSVReader reader = new CSVReader(new FileReader(inFile));
//			BufferedReader br = new BufferedReader(new FileReader(new File(inFile)));
   		 	CSVWriter writer = new CSVWriter(new FileWriter(outFile));
			
   		 	String [] nextLine;
 //  			String temp;
 //  		 	while( (temp = br.readLine()) != null){
 //  		 		nextLine = temp.split(csvSplitBy);
   		 		
		    while ((nextLine = reader.readNext()) != null) {
		        // nextLine[] is an array of values from the line
		    	String find = "(<code>).*?(</code>)";
		    	Pattern pattern = Pattern.compile(find);
		    	Matcher matcher = pattern.matcher(nextLine[1]);
		    	while(matcher.find()){
		    		String rawSnippet = matcher.group();
		    		String snippet = rawSnippet.substring(6,  rawSnippet.length()-7);
		    		snippet = snippet.replace("&gt;", ">").replace("&lt;", "<");
		    		if(snippet!="" && snippet!=null){
		    			 // feed in your array (or convert your data to an array)
			    	    String[] entries = new String[2];
			    	    entries[0] = nextLine[0];
			    	    entries[1] = snippet;
			    	    writer.writeNext(entries);	   
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
	
	
	public static void main(String[] args){

		String[] postSplits = { "aa", "ab", "ac", "ad", "ae", "af", "ag", "ah",
				"ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar",
				"as", "at", "au", "av", "aw", "ax", "ay", "az" };
		
		try {
			String inFile = "";
			String outFile = "";
			CSVWriter writer = new CSVWriter(new FileWriter("C:\\StackOverflow\\C#_snippet\\snippetCount.csv"));
			
			for(String post : postSplits){
				System.out.println("processing post" + post + "......");
				inFile = "C:\\StackOverflow\\C#_body\\post" + post + "_body.csv";
				outFile = "C:\\StackOverflow\\C#_snippet\\post" + post + "_snippet.csv";
				
				String[] entry = new String[2];
				entry[0] = "post"+post;
				entry[1] = parseCSV(inFile, outFile);
				writer.writeNext(entry);
	   		 	
			}
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}


}

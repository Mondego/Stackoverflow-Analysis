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

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


public class CountDifferentError {
	
	public static void count(String inFile, String outFile){
		Map<String, Integer> errorMap = new HashMap<String, Integer>();
		int line = 0;
		try {
			CSVReader reader = new CSVReader(new FileReader(inFile));
   		 	CSVWriter writer = new CSVWriter(new FileWriter(outFile));
			String [] nextLine;
		    while ((nextLine = reader.readNext()) != null) {
		    	String errorNo = nextLine[1].substring(9, 13);
		    	if (errorMap.containsKey(errorNo)){
		    		errorMap.put(errorNo, errorMap.get(errorNo)+1);
		    	}else{
		    		errorMap.put(errorNo, 1);
		    	}
		    }
		    
		    ArrayList<Map.Entry<String, Integer>> errorList = new ArrayList<Map.Entry<String, Integer>>(errorMap.entrySet());
			Collections.sort(errorList,new Comparator<Map.Entry<String, Integer>>(){
				public int compare(Entry<String, Integer> arg0,Entry<String, Integer> arg1) {
					if (arg0.getValue()-arg1.getValue() > 0) return -1;
					else if(arg0.getValue()-arg1.getValue() == 0){
						return 0;
					} else return 1;
				}
			});
			
			for(Map.Entry<String, Integer> entry : errorList){
				String[] newError = new String[2];
		    	newError[0] = entry.getKey();
		    	newError[1] = String.valueOf(entry.getValue());
		    	writer.writeNext(newError);
			}		
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	
	public static void main(String[] args){

		String[] postSplits = { "aa", "ab", "ac", "ad", "ae", "af", "ag", "ah",
				"ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar",
				"as", "at", "au", "av", "aw", "ax", "ay", "az" };
		
		String inFile = "";
		String outFile = "";
		
		for(String post : postSplits){
			System.out.println("processing post" + post + "......");
			inFile = "C:\\StackOverflow\\C#_compileError\\post" + post + "_error.csv";
			outFile = "C:\\StackOverflow\\C#_compileError\\post" + post + "_errorCount.csv";
			count(inFile, outFile);
		
		}
	
	}
}

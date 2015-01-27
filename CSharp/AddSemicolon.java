import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


public class AddSemicolon {
	/*
	 * Fix 2: Adds ";"'s
	 */
	public static String Fix2(String inFile, String outFile) {
		int fixCount = 0;
		Boolean flag = false;
		
		try {
			CSVReader reader = new CSVReader(new FileReader(inFile));

   		 	CSVWriter writer = new CSVWriter(new FileWriter(outFile));
			String [] nextLine;
		    while ((nextLine = reader.readNext()) != null) {
		        // nextLine[] is an array of values from the line
		    	flag = false;
		    	String snippet = nextLine[1];

		    	String codeAlt = "";
		    	String[] codeLines = snippet.split("(&#xA;)+");

				for (String line : codeLines) {
					if (line.contains("{") == false & line.contains("(") == false
							& line.contains("if") == false
							& line.contains("else") == false
							& line.contains("while") == false
							& line.contains("for") == false
							& line.contains("try") == false
							& line.contains("catch") == false
							& line.contains("class") == false
							& line.contains("using") == false
							& line.contains("namespace") == false
							& line.contains("}") == false
							& line.contains(";") == false
							& line.startsWith("//")==false) {
						// THE FIX
						line += "; &#xA;";
						codeAlt += line;
						flag = true;
					}else{
						line += "&#xA;";
						codeAlt += line;
					}
					
				}
				String[] entries = new String[2];
		    	entries[0] = nextLine[0];
		    	entries[1] = codeAlt;
		    	writer.writeNext(entries);
		    	if(flag == true){
		    		fixCount++;
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
	
	public static void main(String[] args){

		String[] postSplits = {"aa", "ab", "ac", "ad", "ae", "af", "ag", "ah",
				"ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar",
				"as", "at", "au", "av", "aw", "ax", "ay", "az"};
		
		try {
			String inFile = "";
			String outFile = "";
			CSVWriter writer = new CSVWriter(new FileWriter("C:\\StackOverflow\\C#_addSemicolon\\addSemicolonCount.csv"));
			
			for(String post : postSplits){
				System.out.println("processing post" + post + "......");
				inFile = "C:\\StackOverflow\\C#_unparsableSnippet\\post" + post + "_snippet.csv";
				outFile = "C:\\StackOverflow\\C#_addSemiColon\\post" + post + "_addSemicolon.csv";
				
				String[] entry = new String[2];
				entry[0] = "post"+post;
				entry[1] = Fix2(inFile, outFile);
				writer.writeNext(entry);
	   		 	
			}
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

}



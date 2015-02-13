package snippet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;


public class ExtractSnippets {
	public static String extractSnippet(String inFile, String outFile){
		int line = 0;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(inFile)));
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outFile)));
			
   		 	String [] nextLine;
   			String temp;
   		 	while( (temp = br.readLine()) != null){
   		 		
   		 		nextLine = temp.split("\t");
   		 		
   		 		String find = "(<code>).*?(</code>)";
		    	Pattern pattern = Pattern.compile(find);
		    	
		    	String body="";
		    	for(int i=1; i<nextLine.length; i++){
		    		body = body + nextLine[i];
		    	} //nextLine[0] is the id, all the others are the body
		    	
		    	Matcher matcher = pattern.matcher(body);
		    	
		    	while(matcher.find()){
		    		String rawSnippet = matcher.group();
		    		String snippet = rawSnippet.substring(6,  rawSnippet.length()-7);	
		    		
		    		if(snippet!="" && snippet!=null){
		    			snippet= snippet.replaceAll("&(amp;)*#xA;", "Di2015NewLine");
		    			snippet= snippet.replaceAll("&(amp;)*#xD;", "Di2015NewLine");
		    			snippet= snippet.replaceAll("&(amp;)*#xa;", "Di2015NewLine");
		    			snippet= snippet.replaceAll("&(amp;)*#xd;", "Di2015NewLine");
		    			snippet= snippet.replaceAll("&(amp;)*#13;", "Di2015NewLine");
		    			snippet= snippet.replaceAll("&(amp;)*#10;", "Di2015NewLine");
		    			snippet= snippet.replaceAll("&(amp;)*#x0A;", "Di2015NewLine");
		    			snippet= snippet.replaceAll("&(amp;)*#x0D;", "Di2015NewLine");
		    			snippet= snippet.replaceAll("&(amp;)*#x0a;", "Di2015NewLine");
		    			snippet= snippet.replaceAll("&(amp;)*#x0d;", "Di2015NewLine");
		    
		    			snippet = StringEscapeUtils.unescapeHtml4(snippet);
		    			snippet = StringEscapeUtils.unescapeHtml3(snippet);
		    			snippet = StringEscapeUtils.unescapeXml(snippet);
//			    	    System.out.println(nextLine[0] + "-----------"+snippet);
			    		writer.write(nextLine[0]+"Di2015UniqueSeparator"+snippet+"\n");
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
				"as", "at", "au", "av", "aw", "ax", "ay", "az", "ba", "bb",
				"bc", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bk", "bl",
				"bm", "bn", "bo", "bp", "bq", "br", "bs", "bt", "bu", "bv",
				"bw", "bx", "by", "bz", "ca", "cb", "cc", "cd", "ce", "cf",
				"cg", "ch", "ci", "cj", "ck", "cl", "cm", "cn", "co", "cp",
				"cq", "cr", "cs", "ct", "cu", "cv", "cw", "cx", "cy", "cz",
				"da", "db", "dc", "dd", "de", "df", "dg", "dh", "di", "dj",
				"dk", "dl", "dm", "dn", "do", "dp", "dq", "dr", "ds", "dt",
				"du", "dv" };
		
/*		String[] postSplits = { "aa", "ab", "ac", "ad", "ae", "af", "ag", "ah",
				"ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar",
				"as", "at", "au", "av", "aw", "ax", "ay", "az" };
		*/
		try {
			String inFile = "";
			String outFile = "";
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\StackOverflow\\C#\\snippets\\initialSnippetCount.txt")));
			
			for(String post : postSplits){
				System.out.println("processing post" + post + "......");
				inFile = "C:\\StackOverflow\\C#\\body\\post" + post + "_body.txt";
				outFile = "C:\\StackOverflow\\C#\\snippets\\post" + post + "_snippet.txt";
				
				String[] entry = new String[2];
				entry[0] = "post"+post;
				entry[1] = extractSnippet(inFile, outFile);
				writer.write(entry[0]+"Di2015UniqueSeparator"+entry[1]+"\n");
	   		 	
			}
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}


}

//http://www.accordess.com/wpblog/an-overview-of-java-compilation-api-jsr-199/
package javaParserCompiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

public class GetFile {
	public String sourceCode = "";
	public LinkedHashMap<String,String> hm=new LinkedHashMap<String,String>();
	public LinkedHashMap<String,String> preparefile(String iFile) throws IOException {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = "splitHere46\",\"";

		try {
			br = new BufferedReader(new FileReader(iFile));
			while ((line = br.readLine()) != null) {
				line=line.replace("<newline46mondego>", "\n");
				line=line.replace("<doubleQuotes46>", "\"");
				String body[] = line.substring(1, line.length() - 1).split(
						cvsSplitBy);
				hm.put(body[0]+"columnMarker46"+body[1], body[2]);
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
					br.close();
			}
		}
		return hm;
	}
}


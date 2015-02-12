package javaParserCompiler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import au.com.bytecode.opencsv.CSVWriter;

public class SnippetInfo {
	ArrayList<Double> loc_set = new ArrayList<Double>();
	ArrayList<Double> charcount_set = new ArrayList<Double>();

	public void count_params(LinkedHashMap<String, String> codeMap) {

		Iterator it = codeMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			String code = (String) pairs.getValue();
			if (code.length() == 0)
				System.out.println("GotIT");
			String[] lines = code.split("\n");

			// new line code
			if (lines.length == 0) {
				loc_set.add((double) 1);
			} else {
				loc_set.add((double) lines.length);
			}

			// NOT USING
			int charcount = 0;
			for (String line : lines) {
				charcount = charcount + line.length();
			}
			charcount_set.add((double) charcount);
		}
	}

	public ArrayList<Double> getLocSet() {
		return loc_set;
	}

	public ArrayList<Double> getCharCountSet() {
		return charcount_set;
	}

	public void save_locs(ArrayList<Double> al) throws IOException {
		CSVWriter writer = new CSVWriter(new FileWriter(
				"E:/StackOverflow/stackexchange/stackoverflow.com-Posts/"
						+ "JavaLOC" + ".csv"));

		for (double val : al) {
			String[] record = { String.valueOf(val) };
			writer.writeNext(record);
		}
		writer.close();
	}

}

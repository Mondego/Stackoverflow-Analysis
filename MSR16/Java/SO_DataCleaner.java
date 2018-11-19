import java.io.BufferedReader;
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

public class SO_DataCleaner {
	public static LinkedHashMap<String, String> hm = new LinkedHashMap<String, String>();
	public static LinkedHashMap<String, String> hm_php = new LinkedHashMap<String, String>();

	public static void main(String[] args) throws IOException {

		String[] posts = { "ab", "ac", "ad", "ae", "af", "ag", "ah",
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

		/*
		 * {"aa", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak",
		 * "al", "am", "an", "ao", "ap", "aq", "ar", "as", "at", "au", "av",
		 * "aw", "ax", "ay", "az", "ba", "bb", "bc", "bd", "be", "bf", "bg",
		 * "bh", "bi", "bj", "bk", "bl", "bm", "bn", "bo", "bp", "bq", "br",
		 * "bs", "bt", "bu", "bv", "bw", "bx", "by", "bz", "ca", "cb", "cc",
		 * "cd", "ce", "cf", "cg", "ch", "ci", "cj", "ck", "cl", "cm", "cn",
		 * "co", "cp", "cq", "cr", "cs", "ct", "cu", "cv", "cw", "cx", "cy",
		 * "cz", "da", "db", "dc", "dd", "de", "df", "dg", "dh", "di", "dj",
		 * "dk", "dl", "dm", "dn", "do", "dp", "dq", "dr", "ds", "dt", "du",
		 * "dv" };
		 */
		for (String post : posts) {

			/*
			 * @params: input file, output file, language
			 */
			System.out.println(post);
			 generateCsvFile(
			 "E:\\StackOverflow\\stackexchange\\stackoverflow.com-Posts\\Php-UnprocessedAccptAsCSV\\posts"+post+"_php_as_parentid_body.csv",
			 "E:\\StackOverflow\\stackexchange\\stackoverflow.com-Posts\\Php-ProcessedAccptAsCSV\\posts"+post+"_php_processed.csv",
			 "php");
		}
	}

	private static void generateCsvFile(String iFile, String oFile, String lang)
			throws IOException {

		String row = "";
		BufferedReader br = null;

		int code_id = 0;
		// Reading File
		br = new BufferedReader(new FileReader(iFile));
		br.readLine();// Skipping first row of csv file
		hm.clear();
		hm_php.clear();

		// Reading rows into "hm"
		while ((row = br.readLine()) != null) {
			int splitPt = row.indexOf(',');
			String parentId = row.substring(0, splitPt);
			String body = row.substring(splitPt + 1, row.length());
			Pattern p = Pattern.compile("\\<code>(.*?)\\</code>");
			Matcher m = p.matcher(body);
			while (m.find()) {
				String code = m.group(1).trim();
				code = code.replace("&#xA;", "<newline46mondego>")
						.replace("&gt;", ">").replace("&lt;", "<")
						.replace("&apos;", "'")
						.replace("&quot;", "<doubleQuotes46>")
						.replace("&amp;", "&")
						.replace("<br>", "<newline46mondego>")
						.replace("nbsp;", " ")
						.replace("\"\"", "<doubleQuotes46>");// converts 2 "'s to 1 "
				// PRINT IT!
				// System.out.println(parentId + "#" + code);
				if (code.compareTo("") != 0) {
					code_id++;
					hm.put(code_id + "-" + parentId, code);
				}
			}
		}

		if (lang.compareTo("php") == 0) {
			php_code_extraction();
		}

		// Writing File
		CSVWriter writer = new CSVWriter(new FileWriter(oFile));
		Iterator it;
		if (lang.compareTo("php") == 0) {
			it = hm_php.entrySet().iterator();
		} else {
			it = hm.entrySet().iterator();
		}
		int i = 0;
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			String key = (String) pairs.getKey();
			String codeId = key.substring(0, key.indexOf('-'));
			String parentId = key.substring(key.indexOf('-') + 1, key.length());
			String entry = codeId + "splitHere46" + "#splitMondego_UCI#"
					+ parentId + "splitHere46" + "#splitMondego_UCI#"
					+ pairs.getValue();
			String[] record = entry.split("#splitMondego_UCI#");
			writer.writeNext(record);
		}
		writer.close();
	}

	/*
	 * Extracting php codes from a code post. E.g. <code><php code><non php
	 * code><php code></code> (code ID remodified here)
	 */
	private static void php_code_extraction() {

		Iterator it0 = hm.entrySet().iterator();
		int i = 0, j1 = 0;
		int new_code_id = 1;
		while (it0.hasNext()) {
			Map.Entry pairs = (Map.Entry) it0.next();
			String php_code = (String) pairs.getValue();
			String key_php = (String) pairs.getKey();
			// String codeId_php = key_php.substring(0, key_php.indexOf('-'));
			String parentId_php = key_php.substring(key_php.indexOf('-') + 1,
					key_php.length());
			if (php_code.contains("?php") == true) {
				Pattern p1 = Pattern.compile("\\?php(.*?)\\?");
				Matcher m1 = p1.matcher(php_code);

				while (m1.find()) {
					String php_code_extract = "<?php " + m1.group(1) + " ?>";
					hm_php.put(new_code_id + "-" + parentId_php,
							php_code_extract);
					i++;
					new_code_id++;
				}
			} else {
				String php_code_refined="<?php " + php_code + " ?>";
				hm_php.put(new_code_id + "-" + parentId_php, php_code_refined);
				j1++;
				new_code_id++;
			}
		}
		System.out.println(i + j1);
	}
}

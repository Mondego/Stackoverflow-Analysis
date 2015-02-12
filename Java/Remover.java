package javaParserCompiler;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Remover {
	public String rmvTracker = "";
	public LinkedHashMap<String, String> rmv1code_accept = new LinkedHashMap<String, String>();
	public int rmv1count = 0;
	public LinkedHashMap<String, String> rmv2code_accept = new LinkedHashMap<String, String>();
	public LinkedHashMap<String, String> rmv2code_reject = new LinkedHashMap<String, String>();
	public int rmv2count = 0;
	public LinkedHashMap<String, String> rmv3code_accept = new LinkedHashMap<String, String>();
	public LinkedHashMap<String, String> rmv3code_reject = new LinkedHashMap<String, String>();
	public int rmv3count = 0;

	/*
	 * Remove single word code blocks
	 */
	public void rmv1(LinkedHashMap<String, String> codeMap) {
		rmvTracker = rmvTracker + "<rmv1>";

		Iterator it = codeMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			String codeEntry = (String) pairs.getValue();

			Pattern p = Pattern.compile("[^\\w]");
			Matcher m = p.matcher(codeEntry);

			if (m.find()) {
				rmv1code_accept.put(String.valueOf(pairs.getKey()), codeEntry);
			} else {
				rmv1count++;
			}
		}
	}

	/*
	 * Remove single lines code blocks that begin with // or /*, assuming they
	 * are entirely comments //NOT USING THIS
	 */
	public void rmv2(LinkedHashMap<String, String> codeMap) {
		rmvTracker = rmvTracker + "<rmv2>";

		Iterator it = codeMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			String codeEntry = (String) pairs.getValue();

			Pattern p = Pattern.compile("^(/\\*).*(\\*\\/)$");
			Matcher m = p.matcher(codeEntry);
			while (m.find()) {
				
			}
			
			if (codeEntry.contains("\n") == true) {
				rmv2code_accept.put(String.valueOf(pairs.getKey()), codeEntry);
			} else {
				if (codeEntry.startsWith("//") == true
						| codeEntry.startsWith("/*") == true) {
					rmv2code_reject.put(String.valueOf(pairs.getKey()),
							codeEntry);
					rmv2count++;
				} else {
					rmv2code_accept.put(String.valueOf(pairs.getKey()),
							codeEntry);
				}
			}
		}
	}
	
	/*
	 * Remove single lines code blocks (takes care of rmv1)
	 */
	public void rmv3(LinkedHashMap<String, String> codeMap) {
		rmvTracker = rmvTracker + "<rmv3>";

		Iterator it = codeMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			String codeEntry = (String) pairs.getValue();

			if (codeEntry.contains("\n") == true) {
				rmv3code_accept.put(String.valueOf(pairs.getKey()), codeEntry);
			} else {
				rmv3code_reject.put(String.valueOf(pairs.getKey()), codeEntry);
				rmv3count++;
			}
		}
	}


}

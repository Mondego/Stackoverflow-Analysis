package javaParserCompiler;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Fix {

	/*
	 * The code on which no fix was applied 
	 */
	public LinkedHashMap<String, String> fix0code = new LinkedHashMap<String, String>();
	public LinkedHashMap<String, String> fix0compPass = new LinkedHashMap<String, String>();
	public LinkedHashMap<String, String> fix0compFail = new LinkedHashMap<String, String>();
	public LinkedHashMap<String, String> fix0parsPass = new LinkedHashMap<String, String>();
	public LinkedHashMap<String, String> fix0parsFail = new LinkedHashMap<String, String>();

	/*
	 * The code on which fix1 was last applied
	 */
	public LinkedHashMap<String, String> fix1code = new LinkedHashMap<String, String>();
	public LinkedHashMap<String, String> fix1compPass = new LinkedHashMap<String, String>();
	public LinkedHashMap<String, String> fix1compFail = new LinkedHashMap<String, String>();
	public LinkedHashMap<String, String> fix1parsPass = new LinkedHashMap<String, String>();
	public LinkedHashMap<String, String> fix1parsFail = new LinkedHashMap<String, String>();

	int fix1count = 0;

	/*
	 * The code on which fix2 was last applied
	 */
	public LinkedHashMap<String, String> fix2code = new LinkedHashMap<String, String>();
	public LinkedHashMap<String, String> fix2compPass = new LinkedHashMap<String, String>();
	public LinkedHashMap<String, String> fix2compFail = new LinkedHashMap<String, String>();
	public LinkedHashMap<String, String> fix2parsPass = new LinkedHashMap<String, String>();
	public LinkedHashMap<String, String> fix2parsFail = new LinkedHashMap<String, String>();

	int fix2count = 0;

	/*
	 * The code on which fix3 was last applied
	 */
	public LinkedHashMap<String, String> fix3code = new LinkedHashMap<String, String>();
	public LinkedHashMap<String, String> fix3compPass = new LinkedHashMap<String, String>();
	public LinkedHashMap<String, String> fix3compFail = new LinkedHashMap<String, String>();
	public LinkedHashMap<String, String> fix3parsPass = new LinkedHashMap<String, String>();
	public LinkedHashMap<String, String> fix3parsFail = new LinkedHashMap<String, String>();

	int fix3count = 0;

	public String fixTracker = "";

	/*
	 * Default Fix : removes class, interface, enum modifiers - public,
	 * abstract, final
	 */
	/*public void fix0(LinkedHashMap<String, String> codeMap) {
		fixTracker = fixTracker + "<fix0>";
		Iterator it = codeMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			String codeAlt = (String) pairs.getValue();
			System.out.println("Before: "+codeAlt);
			codeAlt = codeAlt.replace("public class", "class")
					.replace("public static class", "class")
					.replace("abstract class", "class")
					.replace("final class", "class")
					.replace("public interface", "interface")
					.replace("abstract interface", "interface")
					.replace("final interface", "interface")
					.replace("public enum", "enum");
			fix0code.put((String) pairs.getKey(), codeAlt);
			System.out.println("After: "+codeAlt);
		}
	}*/

	/*
	 * Fix 1: Adds "class Program { }"
	 */
	public void fix1(LinkedHashMap<String, String> codeMap) {
		//System.out.println("FIX");
		fixTracker = fixTracker + "<fix1>";
		Iterator it = codeMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			String codeAlt = "";
			if (((String) pairs.getValue()).contains(" class ") == true
					| ((String) pairs.getValue()).startsWith("class ") == true
					| ((String) pairs.getValue()).contains("\nclass ") == true
					| ((String) pairs.getValue()).startsWith("import ") == true
					| ((String) pairs.getValue()).contains("\nimport ") == true
					| ((String) pairs.getValue()).contains("package ") == true
					| ((String) pairs.getValue()).contains(" package ") == true
					| ((String) pairs.getValue()).contains("\npackage ") == true) {
				codeAlt = (String) pairs.getValue();
			} else {
				// THE FIX
				codeAlt = "class Program {"+ "\n" + pairs.getValue() + "\n"+"}";
				fix1count++;
				//System.out.println(pairs.getKey());
			}
			fix1code.put((String) pairs.getKey(), codeAlt);
		}
//		System.out.println("CODE: after fx1 "+fix1code.get("917columnMarker464337462"));
	}

	/*
	 * Fix 2: Adds ";"'s
	 */
	public void fix2(LinkedHashMap<String, String> codeMap) {
		fixTracker = fixTracker + "<fix2>";
		Iterator it = codeMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			String code = (String) pairs.getValue();
			String codeAlt = "";
			String[] codeLines = code.split("\n");

			
			for (String line : codeLines) {
				if (line.contains("{") == false & line.contains("(") == false
						& line.contains("class") == false
						& line.contains("if") == false
						& line.contains("else") == false
						& line.contains("while") == false
						& line.contains("for") == false
						& line.contains("try") == false
						& line.contains("catch") == false
						& line.contains(";") == false
						& line.endsWith("=")==false
						& line.endsWith("= ")==false
						& line.endsWith("}")==false
						& line.endsWith("} ")==false
						& line.endsWith(" do")==false){
					// THE FIX
					fix2count++;
					line = line + ";";
					codeAlt = codeAlt + line+"\n";
					continue;
				}
				codeAlt = codeAlt + line+"\n";
			}
			fix2code.put((String) pairs.getKey(), codeAlt);
		}
//		System.out.println("CODE: after fx2 "+fix2code.get("917columnMarker464337462"));
	}

	/*
	 * Add method body on single line code blocks.
	 */
	public void fix3(LinkedHashMap<String, String> codeMap) {
		fixTracker = fixTracker + "<fix3>";
		Iterator it = codeMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			String codeAlt = (String) pairs.getValue();
			if (codeAlt.contains("\n")) {
				fix3code.put((String) pairs.getKey(), codeAlt);
			} else {
				if (codeAlt.contains(" class") == false) {
					// THE FIX
					codeAlt = "public Method(){" + pairs.getValue() + "}";
					fix3code.put((String) pairs.getKey(), codeAlt);
					fix3count++;
				}
			}

		}
	}
}

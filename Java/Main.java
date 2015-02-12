package javaParserCompiler;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.com.bytecode.opencsv.CSVWriter;

public class Main {
	static LinkedHashMap<String, String> dataOP = new LinkedHashMap<String, String>();;

	public static void main(String[] args) throws IOException {

		LinkedHashMap<String, String> mainSrc = new LinkedHashMap<String, String>();
		GetFile gf = new GetFile();
		Fix fx = new Fix();
		Remover rmv = new Remover();
		
		/*
		 * Post Info 
		 */
		double total_snippets = 0;
		ArrayList<Double> total_loc_set = new ArrayList<Double>();
		ArrayList<Double> total_charcount_set = new ArrayList<Double>();

		String[] posts = { "aa", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak",
				  "al", "am", "an", "ao", "ap", "aq", "ar", "as", "at", "au", "av",
				  "aw", "ax", "ay", "az", "ba", "bb", "bc", "bd", "be", "bf", "bg",
				  "bh", "bi", "bj", "bk", "bl", "bm", "bn", "bo", "bp", "bq", "br",
				  "bs", "bt", "bu", "bv", "bw", "bx", "by", "bz", "ca", "cb", "cc",
				  "cd", "ce", "cf", "cg", "ch", "ci", "cj", "ck", "cl", "cm", "cn",
				  "co", "cp", "cq", "cr", "cs", "ct", "cu", "cv", "cw", "cx", "cy",
				  "cz", "da", "db", "dc", "dd", "de", "df", "dg", "dh", "di", "dj",
				  "dk", "dl", "dm", "dn", "do", "dp", "dq", "dr", "ds", "dt", "du",
				  "dv"};
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

			String ipFile = "E:/StackOverflow/stackexchange/stackoverflow.com-Posts/Java-ProcessedAccptAsCSV/posts"
					+ post + "_java_processed.csv";

			dataOP.put("File", post);
			mainSrc = gf.preparefile(ipFile);

			// comp.doCompilation(null);

			String fixes = "";// adds to error messages file name
			String operation = "";// adds to error messages file name

			// Removes
			String rmvs = "";// adds to error messages file name

			rmv.rmv1(mainSrc);// Rmv1
			dataOP.put("Rmv 1 count", String.valueOf(rmv.rmv1count));

			//Get aggregated post info
			SnippetInfo post_inf = new SnippetInfo();
			post_inf.count_params(mainSrc);
			total_loc_set.addAll(post_inf.getLocSet());
			total_charcount_set.addAll(post_inf.getCharCountSet());

			// rmv.rmv2(rmv.rmv1code_accept);// Rmv2
			// dataOP.put("Rmv 2 count", String.valueOf(rmv.rmv2count));

			//
			// rmv.rmv3(mainSrc);// Rmv3
			// dataOP.put("Rmv 3 count", String.valueOf(rmv.rmv3count));

			dataOP.put("No. of Snippets",
					String.valueOf(rmv.rmv1code_accept.size()));

			// Raw Parse
			fixes = "nofix";
			operation = "parse";
			Parser pars = new Parser();
			pars.doParse(rmv.rmv1code_accept);// P
			fx.fix0parsPass = pars.codePass;
			fx.fix0parsFail = pars.codeFail;
			dataOP.put("Parse Pass", String.valueOf(fx.fix0parsPass.size()));
			dataOP.put("Parse Fail", String.valueOf(fx.fix0parsFail.size()));
			pars.save_error_messages(operation, rmvs, fixes, post);

			// Compile only the ones that passed raw parsing
			fixes = "nofix";
			operation = "compile";
			Compiler comp = new Compiler();
			comp.doCompilation(fx.fix0parsPass);// C
			fx.fix0compPass = comp.codePass;
			fx.fix0compFail = comp.codeFail;
			dataOP.put("Compile Pass (of Parse Pass Snippets)",
					String.valueOf(fx.fix0compPass.size()));
			dataOP.put("Compile Fail (of Parse Pass Snippets)",
					String.valueOf(fx.fix0compFail.size()));
			comp.save_error_messages(operation, rmvs, fixes, post);

			// fix0fail + fix 1 -> Parse
			fixes = "fix1";
			operation = "parse";
			fx.fix1(fx.fix0parsFail);// Fix1
			dataOP.put("No. of Fix 1 fixes", String.valueOf(fx.fix1count));
			Parser pars2 = new Parser();
			pars2.doParse(fx.fix1code);// P
			fx.fix1parsPass = pars2.codePass;
			fx.fix1parsFail = pars2.codeFail;
			dataOP.put("Parse Fix 1 Pass",
					String.valueOf(fx.fix1parsPass.size()));
			dataOP.put("Parse Fix 1 Fail",
					String.valueOf(fx.fix1parsFail.size()));
			pars2.save_error_messages(operation, rmvs, fixes, post);

			// Compile only the ones that passed fix0fail + fix 1 -> Parse
			fixes = "fix1";
			operation = "compile";
			Compiler comp2 = new Compiler();
			comp2.doCompilation(fx.fix1parsPass);// C
			fx.fix1compPass = comp2.codePass;
			fx.fix1compFail = comp2.codeFail;
			dataOP.put("Compile Pass (of fix1 Parse Pass Snippets)",
					String.valueOf(fx.fix1compPass.size()));
			dataOP.put("Compile Fail (of fix1 Parse Pass Snippets)",
					String.valueOf(fx.fix1compFail.size()));
			comp2.save_error_messages(operation, rmvs, fixes, post);

			// fix0fail + fix 2 -> Parse
			fixes = "fix2";
			operation = "parse";
			fx.fix2(fx.fix0parsFail);// Fix2
			dataOP.put("No. of Fix 2 fixes", String.valueOf(fx.fix2count));
			Parser pars3 = new Parser();
			pars3.doParse(fx.fix2code);// P
			fx.fix2parsPass = pars3.codePass;
			fx.fix2parsFail = pars3.codeFail;
			dataOP.put("Parse Fix 2 Pass",
					String.valueOf(fx.fix2parsPass.size()));
			dataOP.put("Parse Fix 2 Fail",
					String.valueOf(fx.fix2parsFail.size()));
			pars3.save_error_messages(operation, rmvs, fixes, post);

			// Compile only the ones that passed fix0fail + fix 2 -> Parse
			fixes = "fix2";
			operation = "compile";
			Compiler comp3 = new Compiler();
			comp3.doCompilation(fx.fix2parsPass);// C
			fx.fix2compPass = comp3.codePass;
			fx.fix2compFail = comp3.codeFail;
			dataOP.put("Compile Pass (of fix2 Parse Pass Snippets)",
					String.valueOf(fx.fix2compPass.size()));
			dataOP.put("Compile Fail (of fix2 Parse Pass Snippets)",
					String.valueOf(fx.fix2compFail.size()));
			comp3.save_error_messages(operation, rmvs, fixes, post);

			Fix fx_double = new Fix();

			// fix1fail + fix 2 -> Parse
			fixes = "fix1fix2";
			operation = "parse";
			fx_double.fix2(fx.fix1parsFail);// Fix2
			dataOP.put("No. of Fix 2 fixes after Fix 1",
					String.valueOf(fx_double.fix2count));
			Parser pars4 = new Parser();
			pars4.doParse(fx_double.fix2code);// P
			fx_double.fix2parsPass = pars4.codePass;
			fx_double.fix2parsFail = pars4.codeFail;
			dataOP.put("Parse Fix 1, Fix 2 (Sequential) Pass",
					String.valueOf(fx_double.fix2parsPass.size()));
			dataOP.put("Parse Fix 1, Fix 2 (Sequential) Fail",
					String.valueOf(fx_double.fix2parsFail.size()));
			pars4.save_error_messages(operation, rmvs, fixes, post);

			// Compile only the ones that passed fix1fail + fix 2 -> Parse
			fixes = "fix1fix2";
			operation = "compile";
			Compiler comp4 = new Compiler();
			comp4.doCompilation(fx_double.fix2parsPass);// C
			fx_double.fix2compPass = comp4.codePass;
			fx_double.fix2compFail = comp4.codeFail;
			dataOP.put("Compile Pass (of fix1fix2 Parse Pass Snippets)",
					String.valueOf(fx_double.fix2compPass.size()));
			dataOP.put("Compile Fail (of fix1fix2 Parse Pass Snippets)",
					String.valueOf(fx_double.fix2compFail.size()));
			comp4.save_error_messages(operation, rmvs, fixes, post);

			// fix2fail + fix1 -> Parse
			fixes = "fix2fix1";
			operation = "parse";
			fx_double.fix1(fx.fix2parsFail);// Fix1
			dataOP.put("No. of Fix 1 fixes after Fix 2",
					String.valueOf(fx_double.fix1count));
			Parser pars5 = new Parser();
			pars5.doParse(fx_double.fix1code);// P
			fx_double.fix1parsPass = pars5.codePass;
			fx_double.fix1parsFail = pars5.codeFail;
			dataOP.put("Parse Fix 2, Fix 1 (Sequential) Pass",
					String.valueOf(fx_double.fix1parsPass.size()));
			dataOP.put("Parse Fix 2, Fix 1 (Sequential) Fail",
					String.valueOf(fx_double.fix1parsFail.size()));
			pars5.save_error_messages(operation, rmvs, fixes, post);

			// Compile only the ones that passed fix2fail + fix1 -> Parse
			fixes = "fix2fix1";
			operation = "compile";
			Compiler comp5 = new Compiler();
			comp5.doCompilation(fx_double.fix1parsPass);// C
			fx_double.fix1compPass = comp5.codePass;
			fx_double.fix1compFail = comp5.codeFail;
			dataOP.put("Compile Pass (of fix2fix1 Parse Pass Snippets)",
					String.valueOf(fx_double.fix1compPass.size()));
			dataOP.put("Compile Fail (of fix2fix1 Parse Pass Snippets)",
					String.valueOf(fx_double.fix1compFail.size()));
			comp5.save_error_messages(operation, rmvs, fixes, post);

			// System.out.println("Set 1");
			// prnt(fx.fix1parsPass);
			// prnt(fx_double.fix2parsPass);
			// System.out.println("Set 2");
			// prnt(fx.fix2parsPass);
			// prnt(fx_double.fix1parsPass);
			// fx_double.fix1parsFail
			// System.out.println("CODE: \n"+fx_double.fix1parsFail.get("1580columnMarker464357645"));
			// System.out.println("290columnMarker464318068");
			
			prnt(dataOP);

			fx = new Fix();
			gf = new GetFile();
			 comp = new Compiler();
			rmv = new Remover();
			mainSrc = new LinkedHashMap<String, String>();
		}
		
//		StatsCalculator stats=new StatsCalculator();
//		System.out.println("Total: "+total_loc_set.size());
//		System.out.println("Average LOC: "+stats.average(total_loc_set));
//		System.out.println("Std Dev LOC: "+stats.standard_dev(total_loc_set));
//		System.out.println("Average CharCount: "+stats.average(total_charcount_set));
//		System.out.println("Std Dev CharCount: "+stats.standard_dev(total_charcount_set));
	
		SnippetInfo post_inf = new SnippetInfo();
		post_inf.save_locs(total_loc_set);
	}

	public static void prnt(LinkedHashMap<String, String> mp) {
		System.out.println();
		Iterator it2 = mp.entrySet().iterator();
		while (it2.hasNext()) {
			Map.Entry pairs = (Map.Entry) it2.next();
//			 System.out.println(pairs.getKey());
			System.out.println(pairs.getValue());
		}

	}
}

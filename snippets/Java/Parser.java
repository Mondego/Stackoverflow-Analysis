package javaParserCompiler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import au.com.bytecode.opencsv.CSVWriter;

public class Parser {
	LinkedHashMap<String, String> codePass = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> codeFail = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> error_dump = new LinkedHashMap<String, String>();

	public void doParse(LinkedHashMap<String, String> mp) {
		Iterator it = mp.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			String sourceCode = (String) pairs.getValue();
			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setSource(sourceCode.toCharArray());

			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			
			
			// In order to parse 1.5 code, some compiler options need to be set to 1.5
			 Map options = JavaCore.getOptions();
			 JavaCore.setComplianceOptions(JavaCore.VERSION_1_7, options);
			 parser.setCompilerOptions(options);

			CompilationUnit ab = null;
			try {

				final CompilationUnit cu = (CompilationUnit) parser
						.createAST(null);
				ab = cu;
			} catch (Exception e) {
			}
			parser.setKind(ASTParser.K_COMPILATION_UNIT);

			if (ab == null) {
				codeFail.put((String) pairs.getKey(), sourceCode);
				continue;
			} else {
				IProblem[] problems = ab.getProblems();

				if (problems.length > 0) {
					codeFail.put((String) pairs.getKey(), sourceCode);
				} else {
					codePass.put((String) pairs.getKey(), sourceCode);
				}

				if (problems.length > 0) {
					int code_error_idx = 1;
					for (int i = 0; i < problems.length; i++) {
						error_dump.put((String) pairs.getKey()
								+ "columnMarker46" + code_error_idx++
								+ "columnMarker46" + sourceCode,
								problems[i].getMessage());
						if (problems[i].getMessage().contains("source level")){
							System.out.println("Here: "+sourceCode);
						}
					}
				}
			}

		}
	}

	public void save_error_messages(String operation, String rmvs,
			String fixes, String post) throws IOException {
		CSVWriter writer = new CSVWriter(new FileWriter(
				"E:/StackOverflow/stackexchange/stackoverflow.com-Posts/"
						+ operation + "." + fixes + "." + post + "."
						+ "ErrorMessages" + ".csv"));

		// File 2 - Only Messages
		CSVWriter writer2 = new CSVWriter(new FileWriter(
				"E:/StackOverflow/stackexchange/stackoverflow.com-Posts/"
						+ "OnlyMsgs." + operation + "." + fixes + "." + post
						+ "." + "ErrorMessages" + ".csv"));

		Iterator it = error_dump.entrySet().iterator();

		/*
		 * Writing "code_id" + "#" + "parent_id" + "#" + "error_id" + "#" +
		 * "snippet"+"#"+"error_msg"
		 */
		String entry = "code_id" + "#" + "parent_id" + "#" + "error_id" + "#"
				+ "snippet" + "#" + "error_msg";
		String[] record = entry.split("#");
		writer.writeNext(record);

		/*
		 * Writing "snippet" if error_msg is ';' expected
		 */
		// String entry = "snippet";
		// String[] record = entry.split("dummySplitMondego");
		// writer.writeNext(record);

		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			String key = (String) pairs.getKey();
			String[] key_whole_array = key.split("columnMarker46");

			String code_id = key_whole_array[0];
			String parent_id = key_whole_array[1];
			String error_id = key_whole_array[2];
			String snippet = key_whole_array[3].replace("\"",
					"<doublequoteMondego>").replace("\n", "<newlineMondego>");

			String value = (String) pairs.getValue();
			String[] value_whole_array = value.split("columnMarker46");
			String error_msg = value_whole_array[0];

			/*
			 * Writing "code_id" + "#" + "parent_id" + "#" + "error_id" + "#" +
			 * "snippet"+"#"+"error_msg"
			 */
			entry = code_id + "UniqueSplit" + parent_id + "UniqueSplit"
					+ error_id + "UniqueSplit" + snippet + "UniqueSplit"
					+ error_msg;
			record = entry.split("UniqueSplit");
			writer.writeNext(record);
			String[] record2 = new String[] { error_msg };
			writer2.writeNext(record2);

		}

		writer.close();
		writer2.close();
	}
}

package javaParserCompiler;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import au.com.bytecode.opencsv.CSVWriter;

public class Compiler {

	LinkedHashMap<String, String> codePass = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> codeFail = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> error_dump = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> NoSemiColn_Snpts = new LinkedHashMap<String, String>();

	/**
	 * Does the required object initialization and compilation.
	 */
	public void doCompilation(LinkedHashMap<String, String> mp)
			throws IOException {
		Iterator it = mp.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			String sourceCode = (String) pairs.getValue();

			// ***COMPILE PORTION BEGIN**
			// --------------------------

			/* Creating dynamic java source code file object */
			SimpleJavaFileObject fileObject = new DynamicJavaSourceCodeObject(
					"javaCompiler.CompiledFile", sourceCode);
			JavaFileObject javaFileObjects[] = new JavaFileObject[] { fileObject };

			/* Instantiating the java compiler */
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

			/**
			 * Retrieving the standard file manager from compiler object, which
			 * is used to provide basic building block for customizing how a
			 * compiler reads and writes to files.
			 * 
			 * The same file manager can be reopened for another compiler task.
			 * Thus we reduce the overhead of scanning through file system and
			 * jar files each time
			 */
			StandardJavaFileManager stdFileManager = compiler
					.getStandardFileManager(null, Locale.getDefault(), null);

			/*
			 * Prepare a list of compilation units (java source code file
			 * objects) to input to compilation task
			 */
			Iterable<? extends JavaFileObject> compilationUnits = Arrays
					.asList(javaFileObjects);

			/* Prepare any compilation options to be used during compilation */
			// In this example, we are asking the compiler to place the output
			// files
			// under bin folder.
			String[] compileOptions = new String[] { "-d", "bin" };
			Iterable<String> compilationOptionss = Arrays
					.asList(compileOptions);

			/*
			 * Create a diagnostic controller, which holds the compilation
			 * problems
			 */
			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

			/*
			 * Create a compilation task from compiler by passing in the
			 * required input objects prepared above
			 */
			CompilationTask compilerTask = compiler.getTask(null,
					stdFileManager, diagnostics, compilationOptionss, null,
					compilationUnits);

			// Perform the compilation by calling the call method on
			// compilerTask
			// object.
			boolean status = compilerTask.call();

			// --------------------------
			// ***COMPILE PORTION END**
			int i = 0;

			/*
			 * Do not count codes which only have non-errors and class file name
			 * errors
			 */
			/*
			 * if (!status) { boolean err = false; for (Diagnostic diagnostic :
			 * diagnostics.getDiagnostics()) { if
			 * (!(diagnostic.getKind().toString().contains("ERROR"))) { err =
			 * false; continue; } else if (diagnostic.getMessage(null).contains(
			 * "should be declared in a file named")) { err = false; } else if
			 * (!(diagnostic.getMessage(null)
			 * .contains("should be declared in a file named"))) { err = true;
			 * break; } } if (!err) { codePass.put((String) pairs.getKey(),
			 * sourceCode); // System.out.println(sourceCode); } else
			 * codeFail.put((String) pairs.getKey(), sourceCode); // /
			 * System.out.println(i++); } else { codePass.put((String)
			 * pairs.getKey(), sourceCode); }
			 */

			/*
			 * PROCESSING ERROR MESSAGES
			 */

			if (!status) {
				int code_error_idx = 1;
				boolean err = false;
				for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
					if (diagnostic.getKind().toString().contains("ERROR")
							& !(diagnostic.getMessage(null)
									.contains("should be declared in a file named"))) {
						error_dump.put((String) pairs.getKey()
								+ "columnMarker46" + code_error_idx++
								+ "columnMarker46" + sourceCode,
								diagnostic.getMessage(null) + "columnMarker46"
										+ diagnostic);
						err = true;
					}

				}
				if (err == true) {
					codeFail.put((String) pairs.getKey(), sourceCode);
				} else if (err == false) {
					codePass.put((String) pairs.getKey(), sourceCode);
				}
			} else {
				codePass.put((String) pairs.getKey(), sourceCode);
			}
			stdFileManager.close();// Close the file manager
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
		 * "snippet"+"#"+"error_msg"+"#"+"error_msg_full
		 */
		String entry = "code_id" + "#" + "parent_id" + "#" + "error_id" + "#"
				+ "snippet" + "#" + "error_msg" + "#" + "error_msg_full";
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

			// System.out.println(id_whole_array[3]);
			String value = (String) pairs.getValue();
			String[] value_whole_array = value.split("columnMarker46");
			String error_msg = value_whole_array[0];
			String error_msg_full = value_whole_array[1];

			/*
			 * Writing "code_id" + "#" + "parent_id" + "#" + "error_id" + "#" +
			 * "snippet"+"#"+"error_msg"+"#"+"error_msg_full
			 */
			entry = code_id + "UniqueSplit" + parent_id + "UniqueSplit"
					+ error_id + "UniqueSplit" + snippet + "UniqueSplit"
					+ error_msg + "UniqueSplit" + error_msg_full;
			record = entry.split("UniqueSplit");
			writer.writeNext(record);
			String[] record2 = new String[] { error_msg };
			writer2.writeNext(record2);

			/*
			 * Snippets with ';' expected
			 */
			// if (error_msg.contains("';' expected")){
			// NoSemiColn_Snpts.put(snippet, "");
			//
			// }

		}
		// Iterator it2 = NoSemiColn_Snpts.entrySet().iterator();
		// while (it2.hasNext()) {
		// Map.Entry pairs = (Map.Entry) it2.next();
		// String Snpt_noSemiColn=(String)pairs.getKey();
		// record=Snpt_noSemiColn.split("dummysplitMondego");
		// writer.writeNext(record);}

		writer.close();
		writer2.close();
	}

	/**
	 * Creates a dynamic source code file object
	 * 
	 * This is an example of how we can prepare a dynamic java source code for
	 * compilation. This class reads the java code from a string and prepares a
	 * JavaFileObject
	 * 
	 */
	class DynamicJavaSourceCodeObject extends SimpleJavaFileObject {
		private String qualifiedName;
		private String sourceCode;

		/**
		 * Converts the name to an URI, as that is the format expected by
		 * JavaFileObject
		 * 
		 * 
		 * @param fully
		 *            qualified name given to the class file
		 * @param code
		 *            the source code string
		 */
		protected DynamicJavaSourceCodeObject(String name, String code) {
			super(URI.create("string:///" + name.replaceAll(".", "/")
					+ Kind.SOURCE.extension), Kind.SOURCE);
			this.qualifiedName = name;
			this.sourceCode = code;
		}

		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors)
				throws IOException {
			return sourceCode;
		}

		public String getQualifiedName() {
			return qualifiedName;
		}

		public void setQualifiedName(String qualifiedName) {
			this.qualifiedName = qualifiedName;
		}

		public String getSourceCode() {
			return sourceCode;
		}

		public void setSourceCode(String sourceCode) {
			this.sourceCode = sourceCode;
		}
	}
}

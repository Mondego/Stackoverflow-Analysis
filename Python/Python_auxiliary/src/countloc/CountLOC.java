package countloc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import snippet.XmlWriter;

public class CountLOC {
	public static void main(String[] args) {

		String[] postSplits = { "aa", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar", "as", "at", "au", "av", "aw", "ax", "ay", "az", "ba", "bb",
				"bc", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bk", "bl", "bm", "bn", "bo", "bp", "bq", "br", "bs", "bt", "bu", "bv", "bw", "bx", "by", "bz", "ca", "cb", "cc", "cd", "ce", "cf",
				"cg", "ch", "ci", "cj", "ck", "cl", "cm", "cn", "co", "cp", "cq", "cr", "cs", "ct", "cu", "cv", "cw", "cx", "cy", "da", "db", "dc", "dd", "de", "df", "dg", "dh", "di", "dj",
				"dk", "dl", "dm", "dn", "do", "dp", "dq", "dr", "ds", "dt", "du", "dv" };

		int totalCount = 0;
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(new File("C:\\StackOverflow\\Python\\countPythonLOC.txt")));

			for (String post : postSplits) {
				System.out.println("processing post" + post + "......");

				File xmlFile = new File("C:\\StackOverflow\\Python\\body\\post" + post + "_body.xml");
				// Get the DOM Builder Factory
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

				// Get the DOM Builder
				DocumentBuilder builder;

				builder = factory.newDocumentBuilder();
				// Load and Parse the XML document
				// document contains the complete XML as a Tree.
				Document document = builder.parse(xmlFile);
				// Iterating through the nodes and extracting the data.
				NodeList nodeList = document.getDocumentElement().getChildNodes();

				XmlWriter xmlwriter = new XmlWriter(); // prepare to write
														// snippets into xml
														// file
				for (int i = 0; i < nodeList.getLength(); i++) {

					// We have encountered an <ROW> tag.
					Node node = nodeList.item(i);
					if (node instanceof Element) {
						Data row = new Data();
						NodeList childNodes = node.getChildNodes();

						for (int j = 0; j < childNodes.getLength(); j++) {

							Node cNode = childNodes.item(j);
							// Identifying the child tag of data encountered.
							if (cNode instanceof Element) {
								String content = cNode.getLastChild().getTextContent().trim();
								switch (cNode.getNodeName()) {
								case "Id":
									row.id = content;
									break;
								case "Body":
									row.body = content;
									String find = "(<code>).*?(</code>)";
									Pattern pattern = Pattern.compile(find);
									Matcher matcher = pattern.matcher(content);
									while (matcher.find()) {
										String rawSnippet = matcher.group();
										String snippet = rawSnippet.substring(6, rawSnippet.length() - 7);
										row.snippets.add(snippet);
									}
									if (!row.snippets.isEmpty()) {
										for (String s : row.snippets) {
											totalCount++;
											String[] nextLine = s.split("&#xA;");
											int loc = nextLine.length;
											writer.write(loc + "\t");
											if (totalCount % 10 == 0) {
												writer.write("\n");
											}
										}
									}
									break;
								}
							}
						}
					}
				}
			}
			writer.close();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(totalCount);
	}
}

class Data {

	String id;
	String body;
	ArrayList<String> snippets;

	public Data() {
		id = "";
		body = "";
		snippets = new ArrayList<String>();
	}

	@Override
	public String toString() {
		return id + snippets;
	}

}
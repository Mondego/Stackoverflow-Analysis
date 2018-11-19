package snippet;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;



public class XmlWriter {
	
	Document doc;
	Element rootElement;
	

	public XmlWriter(){
		try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				// root elements
				doc = docBuilder.newDocument();
				rootElement = doc.createElement("DATA");
				doc.appendChild(rootElement);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}		
	}
			
	public void writeToXML(String filename){
		try{
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filename));
	 
			// Output to console for testing
			//StreamResult result = new StreamResult(System.out);
			System.out.println(result.toString()); 
			
			transformer.transform(source, result);
			System.out.println("File saved!");
	 
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
	}
	
	public void addElement(String idNum, String snippetText){
		//escape xml and html notes
		
		
		// row elements
		Element row = doc.createElement("ROW");
		rootElement.appendChild(row);

		// id elements
		Element id = doc.createElement("id");
		id.appendChild(doc.createTextNode(idNum));
		row.appendChild(id);
 
		// snippet elements
		Element snippet = doc.createElement("snippet");
		snippet.appendChild(doc.createTextNode(snippetText));
		row.appendChild(snippet);		
	}
}



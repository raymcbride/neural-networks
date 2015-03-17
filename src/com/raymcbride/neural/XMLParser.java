import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;

/**
 * The XMLParser class is used to parse XML documents and create W3C DOM objects
 *
 *
 * @author Raymond McBride
 */
public class XMLParser{

	private DocumentBuilderFactory factory;
	private DocumentBuilder documentBuilder;
	private Document document;

	/**
	 * This constructor for the <code>XMLParser</code>builds a new <code>Document</code> from
	 * an XML file
	 *
	 * @param location The location of the XML file
	 */
	public XMLParser(String location){

		try{
			factory = DocumentBuilderFactory.newInstance();
			documentBuilder = factory.newDocumentBuilder();
			document = documentBuilder.parse(new File(location));
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}

	/**
	 * Gets the Document
	 *
	 * @return a Document
	 */
	public Document getDocument(){
		return document;
	}
}
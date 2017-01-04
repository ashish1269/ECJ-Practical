package org.ashish.userhits;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


/**
 * @author Ashish
 *increaseHit() method increase the previous hit count by one
 *and then return the updated count back to calling method
 */
public class CountUserHits {
	
	
	private static long counts;	
	
	public static long increaseHit() throws java.lang.IllegalStateException, FileNotFoundException{
	
		File userDataFile = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;
		try {
			builder = factory.newDocumentBuilder();
			userDataFile = new File("/home/ashish/Documents/workspace/eclipse/Practical3/WebContent/WEB-INF/WebsiteHits.xml");
			
			if(!userDataFile.exists() ||userDataFile.length()==0)
			{
				userDataFile.createNewFile();
				FileWriter fis = new FileWriter(userDataFile);
				BufferedWriter bos = new BufferedWriter(fis);
				bos.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<hits>\n0\n</hits>");
				bos.flush();
				bos.close();
				document = builder.parse(userDataFile);
			}
			else
			{
				document = builder.parse(userDataFile);
			}
			
			//If courseFile doesn't have a root element add one
			if(document.getDocumentElement()==null)
			{
				Element root = document.createElement("hits");
				document.appendChild(root);
			}
			
			counts = Long.parseLong(document.getDocumentElement().getTextContent().trim())+1;
			
			document.getDocumentElement().setTextContent(Long.toString(counts));
			
			//Finally saving updated hit-counts
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			StreamResult result = new StreamResult(new FileWriter(userDataFile));
			DOMSource source = new DOMSource(document);
			transformer.transform(source, result);
			
		}catch(org.xml.sax.SAXParseException e){
			if("Content is not allowed in prolog.".equals(e.getMessage()))
			{
				userDataFile = new File("/home/ashish/Documents/workspace/eclipse/Practical3/WebContent/WEB-INF/WebsiteHits.xml");
				try {
					userDataFile.createNewFile();
					FileWriter fis = new FileWriter(userDataFile);
					BufferedWriter bos = new BufferedWriter(fis);
					bos.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<hits>\n1\n</hits>");
					bos.flush();
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				counts  = 1;
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Something happened with xml file");
			
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			if(document != null)
			{
				
				//Update counts to 0 in case there is garbled value
				document.getDocumentElement().getFirstChild().setTextContent("1");
				
				//Finally saving updated hit-counts to zero
				TransformerFactory transFactory = TransformerFactory.newInstance();
				Transformer transformer;
				try {
					transformer = transFactory.newTransformer();
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");
					StreamResult result = new StreamResult(new FileWriter(userDataFile));
					DOMSource source = new DOMSource(document);
					transformer.transform(source, result);
				} catch (TransformerConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (TransformerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				counts = 1;
			}
			else{
				System.out.println("Handle exception in better way. Document is null, yet there is NumberFormatException.");
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return counts;
	}

}
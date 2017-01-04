package org.ashish.question5_register;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.w3c.dom.Text;
import org.xml.sax.SAXException;


public class Question5 extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String name = null, uname=null, pass=null, address=null, email=null, sex=null,contact=null, altContact=null, edu10 = null,
				edu12 = null, eduBach=null, eduMast=null, utype=null,   EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$",
				MARKS_REGEX = "((^(100)([.][0]+)?$)|(^[0-9]{1,2}([.][0-9]+)?$))";
		
		boolean error = false;
		
		PrintWriter writer = response.getWriter();
		try{

			response.setContentType("text/html");
			name = request.getParameter("name");
			uname = request.getParameter("uname");
			pass = request.getParameter("pass");
			email = request.getParameter("email");
			sex = request.getParameter("sex");
			address = request.getParameter("address");
			contact = request.getParameter("contact");
			altContact = request.getParameter("alt-contact");
			edu10 = request.getParameter("edu-10");
			edu12 = request.getParameter("edu-12");
			eduBach = request.getParameter("edu-bach");
			eduMast = request.getParameter("edu-mast");
			utype = request.getParameter("utype");

			writer.println("<html><head><title>Register</title></head><body>");

			if(!name.matches("[a-zA-Z ]+"))
			{
				writer.println("<p style=\"color:red\">Name should contain english alphabets only.</p>");
				error = true;
			}
			if(!contact.matches("[0-9]+") || !altContact.matches("[0-9]+"))
			{
				writer.println("<p style=\"color:red\">Contact number must be numeric only</p>");
				error = true;
			}
			if(!email.matches(EMAIL_REGEX))
			{
				writer.println("<p style=\"color:red\">Email format is not correct</p>");
				error = true;
				
			}
			if(pass!=null && pass.length()<8)
			{
				writer.println("<p style=\"color:red\">Password length should be at least 8</p>");
				error = true;
			}
			if(!address.matches("[a-zA-Z0-9\n,-_]+"))
			{
				writer.println("<p style=\"color:red\">Address can contain alphanumeric characters and commas only</p>");
				error = true;
			}
			if(!edu10.matches(MARKS_REGEX) || !edu12.matches(MARKS_REGEX) || !eduBach.matches(MARKS_REGEX) || !eduMast.matches(MARKS_REGEX))
			{
				writer.println("<p style=\"color:red\">Education fileds can contain numeric characters and one point only</p>");
				error = true;
			}
			
			if(!error)
			{
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				File userDataFile = new File("/home/ashish/Documents/workspace/eclipse/Practical2/WebContent/WEB-INF/user-data.xml");
				Document document = null;
				
				if(!userDataFile.exists() ||userDataFile.length()==0)
				{
					userDataFile.createNewFile();
					FileWriter fis = new FileWriter(userDataFile);
					BufferedWriter bos = new BufferedWriter(fis);
					bos.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<user-data>\n\n</user-data>");
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
					Element root = document.createElement("students");
					document.appendChild(root);
				}
				Text text =  null; //Variable that adds textContent
				//Create <student> element
				Element userEle = document.createElement("student");
				//Create <name>, <uname>, <email>, <pass>, <sex>, <address>,<contact>, <alt-contact>, <edu-10>, <edu-12>,<edu-bach>, <edu-mast>, <utype> elements
				
				Element nameEle = document.createElement("name");
				text = document.createTextNode(name);
				nameEle.appendChild(text);
				
				Element unameEle = document.createElement("uname");
				text = document.createTextNode(uname);
				unameEle.appendChild(text);
				
				Element emailEle = document.createElement("email");
				text = document.createTextNode(email);
				emailEle.appendChild(text);
				
				Element passEle = document.createElement("pass");
				text = document.createTextNode(pass);
				passEle.appendChild(text);
				
				Element sexEle = document.createElement("sex");
				text = document.createTextNode(sex);
				sexEle.appendChild(text);
				
				Element addressEle = document.createElement("address");
				text = document.createTextNode(address);
				addressEle.appendChild(text);
				
				Element contactEle = document.createElement("contact");
				text = document.createTextNode(contact);
				contactEle.appendChild(text);
				
				Element altContactEle = document.createElement("alt-contact");
				text = document.createTextNode(altContact);
				altContactEle.appendChild(text);
				
				Element edu10Ele = document.createElement("edu-10");
				text = document.createTextNode(edu10);
				edu10Ele.appendChild(text);
				
				Element edu12Ele = document.createElement("edu-12");
				text = document.createTextNode(edu12);
				edu12Ele.appendChild(text);
				
				Element eduBachEle = document.createElement("edu-bach");
				text = document.createTextNode(eduBach);
				eduBachEle.appendChild(text);
				
				Element eduMastEle = document.createElement("edu-mast");
				text = document.createTextNode(eduMast);
				eduMastEle.appendChild(text);
				
				Element utypeEle = document.createElement("utype");
				text = document.createTextNode(utype);
				utypeEle.appendChild(text);
				
				//Append all children nodes to parents and parents to their parents
				userEle.appendChild(nameEle);
				userEle.appendChild(unameEle);
				userEle.appendChild(emailEle);
				userEle.appendChild(passEle);
				userEle.appendChild(sexEle);
				userEle.appendChild(addressEle);
				userEle.appendChild(contactEle);
				userEle.appendChild(altContactEle);
				userEle.appendChild(edu10Ele);
				userEle.appendChild(edu12Ele);
				userEle.appendChild(eduBachEle);
				userEle.appendChild(eduMastEle);
				userEle.appendChild(utypeEle);
				
				//Adding student's details to root node
				document.getDocumentElement().appendChild(userEle);
				
				//Finally saving updated details
				TransformerFactory transFactory = TransformerFactory.newInstance();
				Transformer transformer = transFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				StreamResult result = new StreamResult(new FileWriter(userDataFile));
				DOMSource source = new DOMSource(document);
				transformer.transform(source, result);
				writer.println("<p>Details saved. <big>You are <em>registered!</em></big></p>");
				
				
				
				/************ Add user to userfile.xml *******************/
				Document loginDocument = null;
				userDataFile = new File("/home/ashish/Documents/workspace/eclipse/Practical2/WebContent/WEB-INF/userfile.xml");
				
				if(!userDataFile.exists() ||userDataFile.length()==0)
				{
					userDataFile.createNewFile();
					FileWriter fis = new FileWriter(userDataFile);
					BufferedWriter bos = new BufferedWriter(fis);
					bos.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<users>\n\n</users>");
					bos.flush();
					bos.close();
					loginDocument = builder.parse(userDataFile);
				}
				else
				{
					loginDocument = builder.parse(userDataFile);
				}
				
				//If courseFile doesn't have a root element add one
				if(loginDocument.getDocumentElement()==null)
				{
					Element root = loginDocument.createElement("users");
					loginDocument.appendChild(root);
				}
				
				
				userEle = loginDocument.createElement(utype);
				
				unameEle = loginDocument.createElement("uname");
				text = loginDocument.createTextNode(uname);
				unameEle.appendChild(text);
				
				passEle = loginDocument.createElement("pass");
				text = loginDocument.createTextNode(pass);
				passEle.appendChild(text);
				
				userEle.appendChild(unameEle);
				userEle.appendChild(passEle);
				loginDocument.getDocumentElement().appendChild(userEle);
				
				//Finally saving updated details
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				result = new StreamResult(new FileWriter(userDataFile));
				source = new DOMSource(loginDocument);
				transformer.transform(source, result);
				
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(NullPointerException ex)
			{
				writer.println("<p style=\"color:red\">No field can't be left blank or unselected.</p>");
				error = true;
		}
				
		writer.println("</body></html>");
		writer.close();
				
	}

}
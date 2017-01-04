package org.ashish.question2_enroll;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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

import org.ashish.userhits.CountUserHits;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class Question2 extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//Count hit/visit
		CountUserHits.increaseHit();

		String name = null, email=null, enrolNo = null, course = null, EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		List<String> subjects = null;
		boolean error = false;
		
		PrintWriter writer = response.getWriter();
		try{

			response.setContentType("text/html");
			name = request.getParameter("uname");
			email = request.getParameter("email");
			enrolNo = request.getParameter("enrol");
			course = request.getParameter("course");

			writer.println("<html><head><title>Register</title></head><body>");
			subjects = new ArrayList<>(Arrays.asList(request.getParameterValues("subjects")));
			if(subjects.size()<3)
			{
				writer.println("<p style=\"color:red\">Select at least three subjects</p>");
				error = true;
			}


			if(name==null || email==null || enrolNo==null || course==null)
			{
				writer.println("<p style=\"color:red\">No field can't be left blank.</p>");
				error = true;
			}
			if(!name.matches("[a-zA-Z ]+"))
			{
				writer.println("<p style=\"color:red\">Name should contain english alphabets only.</p>");
				error = true;
			}
			if(!enrolNo.matches("[0-9]+"))
			{
				writer.println("<p style=\"color:red\">Enrolment number must be numeric only</p>");
				error = true;
			}
			if(!email.matches(EMAIL_REGEX))
			{
				writer.println("<p style=\"color:red\">Email format is not correct</p>");
				error = true;
				
			}
			
			if(!error)
			{
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				File courseFile = null;
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = null;
				
				
				String dataFileUrl = getServletConfig().getInitParameter("course-file-url");
				
				if(dataFileUrl == "" || dataFileUrl == null)
				{
					System.out.println("Data file url is not mentioned in web.xml -> Exiting!");
					return;
				}
				else
				{	
					courseFile = new File(dataFileUrl);
				}
				
				
				if(courseFile==null || !courseFile.exists() ||courseFile.length()==0)
				{
					courseFile.createNewFile();
					FileWriter fis = new FileWriter(courseFile);
					BufferedWriter bos = new BufferedWriter(fis);
					bos.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<students>\n\n</students>");
					bos.flush();
					bos.close();
				}
				
				document = builder.parse(courseFile);

				//If courseFile doesn't have a root element add one
				if(document.getDocumentElement()==null)
				{
					Element root = document.createElement("students");
					document.appendChild(root);
				}
				Text text =  null; //Variable that adds textContent
				//Create <student> element
				Element studentEle = document.createElement("student");
				//Create <enrol-no>, <name>, <email>, <course>, <subjects> elements
				Element enrolNoEle = document.createElement("enrol-no");
				text = document.createTextNode(enrolNo);
				enrolNoEle.appendChild(text);
				Element nameEle = document.createElement("name");
				text = document.createTextNode(name);
				nameEle.appendChild(text);
				Element emailEle = document.createElement("email");
				text = document.createTextNode(email);
				emailEle.appendChild(text);
				Element courseEle = document.createElement("course");
				text = document.createTextNode(course);
				courseEle.appendChild(text);
				Element subjectsEle = document.createElement("subjects");
				//<subjects> further contains many <subject> elements
				Element subject = null;
				for(int i=0; i<subjects.size(); i++)
				{
					subject = document.createElement("subject");
					text = document.createTextNode(subjects.get(i));
					subject.appendChild(text);
					subjectsEle.appendChild(subject);
				}
				//Append all children nodes to parents and parents to their parents
				studentEle.appendChild(enrolNoEle);
				studentEle.appendChild(nameEle);
				studentEle.appendChild(emailEle);
				studentEle.appendChild(courseEle);
				studentEle.appendChild(subjectsEle);
				
				//Adding student's details to root node
				document.getDocumentElement().appendChild(studentEle);
				
				//Finally saving updated details
				TransformerFactory transFactory = TransformerFactory.newInstance();
				Transformer transformer = transFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				StreamResult result = new StreamResult(new FileWriter(courseFile));
				DOMSource source = new DOMSource(document);
				transformer.transform(source, result);
				writer.println("<p>Details saved. <big>You are <em>enrolled!</em></big></p>");
				
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
			
		finally{
		
			if(writer!=null)
			{
				if(!error)
				{
					writer.println("<div><b>Name:</b> "+name+"</div>");
					writer.println("<div><b>Enrollment number:</b> "+enrolNo+"</div>");
					writer.println("<div><b>Email:</b> "+email+"</div>");
					writer.println("<div><b>Course:</b> "+course+"</div>");
					
					writer.print("<div><b>Subjects:</b> ");
					
					Iterator<String> subjectIterator = subjects.iterator();
					
					while(subjectIterator.hasNext())
						writer.print(" "+subjectIterator.next()+".");
					
					writer.println("</div>");
				}
				writer.println("</body></html>");
				writer.close();
			}
		}
	}

}
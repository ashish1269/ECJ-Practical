package org.ashish.question3_addmarks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;


public class Question3 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Question3() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		String course = request.getParameter("course");
		String name = request.getParameter("name");
		String enrolNo = request.getParameter("enrol-no");
		byte totalSubjects = Byte.parseByte(request.getParameter("total-subjects")), score=-1;
		boolean error = false;
		Map<String,Byte> marks = new HashMap<>();
		
		while(totalSubjects>0)
		{
			try{
				score = Byte.parseByte(request.getParameter("subject-"+totalSubjects));
			}
			catch(NumberFormatException ex)
			{
				System.out.println(ex.getMessage());
				error = true;
				break;
			}
			
			if(score>100 || score<0)
			{
				error = true;
				break;
			}
			marks.put(request.getParameterValues("subject")[totalSubjects-1], score);
			System.out.println(request.getParameterValues("subject")[totalSubjects-1]+" : "+score);
			totalSubjects--;
		}
		
		if(!error)
		{
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			File marksFile = new File("/home/ashish/Documents/workspace/eclipse/Practical2/WebContent/WEB-INF/marks-file.xml");
			Document document = null;
			DocumentBuilder builder = null;
			try {
				builder = factory.newDocumentBuilder();
				if(!marksFile.exists() ||marksFile.length()==0)
				{
					marksFile.createNewFile();
					FileWriter fis = new FileWriter(marksFile);
					BufferedWriter bos = new BufferedWriter(fis);
					bos.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<marks>\n\n</marks>");
					bos.flush();
					bos.close();
					document = builder.parse(marksFile);
				}
				else
				{
					document = builder.parse(marksFile);
				}

				//If courseFile doesn't have a root element add one
				if(document.getDocumentElement()==null)
				{
					Element root = document.createElement("students");
					document.appendChild(root);
				}
				
				Text text =  null; //Variable that adds textContent
				//Create <student-marks> element
				Element studentMarksEle = document.createElement("student-marks");
				
				Element enrolNoEle = document.createElement("enrol-no");
				text = document.createTextNode(enrolNo);
				enrolNoEle.appendChild(text);
				studentMarksEle.appendChild(enrolNoEle);
				
				Element nameEle = document.createElement("name");
				text = document.createTextNode(name);
				nameEle.appendChild(text);
				studentMarksEle.appendChild(nameEle);
				
				Element courseEle = document.createElement("course");
				text = document.createTextNode(course);
				courseEle.appendChild(text);
				studentMarksEle.appendChild(courseEle);
				
				Element subjectMarks = null;
				String subjectName = null;
				for(int i=0; i<marks.size(); i++)
				{
					subjectName = request.getParameterValues("subject")[i];
					
					subjectMarks = document.createElement("subject");
					subjectMarks.setAttribute("name", subjectName);
					
					text = document.createTextNode(marks.get(subjectName).toString());
					subjectMarks.appendChild(text);
					
					studentMarksEle.appendChild(subjectMarks);
				}
				document.getDocumentElement().appendChild(studentMarksEle);
			
				//Finally saving updated details
				TransformerFactory transFactory = TransformerFactory.newInstance();
				Transformer transformer = transFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				StreamResult result = new StreamResult(new FileWriter(marksFile));
				DOMSource source = new DOMSource(document);
				transformer.transform(source, result);
				
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				error = true;
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				error = true;
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		writer.println("<html><head><title>Register</title></head><body>");
		
		if(!error)
		{
			writer.println("<p><big>Marks have been added!</big></p></body></html>");
		}
		if(error)
		{
			writer.println("<p><big>Marks have not been added! Probably they aren't in range (0-100) or aren't in proper format (real number).</big></p></body></html>");
		}
		
		writer.close();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String enrolNo = request.getParameter("enrol-no");
		
		boolean error = false;
		if(!enrolNo.matches("[0-9]+"))
		{
			error = true;
		}
		if(!error)
		{
			System.out.println(enrolNo);
			StringBuilder html = new StringBuilder("<html><head><title>Add Marks</title></head>"+
						"<body><form action=\"get-student-details\" method=\"POST\">");
			
			String course = null, name = null;
			List<String> subjects = null;
			byte totalSubjects = 0;
			try {
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder;
				NodeList students = null;
				Node studentNode = null;
				NodeList subjectsNode = null;
				Element studentElement = null;
				Element subjectsElement = null;
				builder = factory.newDocumentBuilder();
				Document document = builder.parse(new File("/home/ashish/Documents/workspace/eclipse/Practical2/WebContent/WEB-INF/course-file.xml"));
				Element root = document.getDocumentElement();
				//optional
				root.normalize();
				
				students = document.getElementsByTagName("student");
				
				for(int temp = 0; temp< students.getLength(); temp++)
				{
					studentNode = students.item(temp);
					if(studentNode.getNodeType() == Node.ELEMENT_NODE){
						studentElement = (Element)studentNode;
						System.out.println(studentElement.getElementsByTagName("enrol-no").item(0).getTextContent());
						if(studentElement != null && enrolNo.equals(studentElement.getElementsByTagName("enrol-no").item(0).getTextContent().trim()))
						{
							name = studentElement.getElementsByTagName("name").item(0).getTextContent().trim();
							course = studentElement.getElementsByTagName("course").item(0).getTextContent().trim();
							subjects = new ArrayList<String>();
							subjectsElement = (Element)studentElement.getElementsByTagName("subjects").item(0);
							subjectsNode = subjectsElement.getElementsByTagName("subject");
							for(totalSubjects=0; totalSubjects<subjectsNode.getLength(); totalSubjects++)
							{
								subjects.add(subjectsNode.item(totalSubjects).getTextContent());
							}
						break;
						}
					}
					
				}
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			html.append("<div><label>Name: </label><span>"+name+"</span></div><input type=\"hidden\" name=\"name\" value=\""+name+"\">"+
					"<div><label>Course: </label><span>"+course+"</span></div><input type=\"hidden\" name=\"course\" value=\""+course+"\">"+
					"<div><label><big>Add marks: </big></label></div>");
			if(subjects!=null)
				
			for(int j=0; j<subjects.size(); j++)
			{
				html.append("<div><label>"+subjects.get(j)+": </label><input type=\"text\" name=\"subject-"+(j+1)+"\"></div><input type=\"hidden\" name=\"subject\" value=\""+subjects.get(j)+"\">");
			}
			html.append("<div><button type=\"submit\">Add marks</button></div>"+
			"<input type=\"hidden\" name=\"enrol-no\" value=\""+enrolNo+"\">"+
			"<input type=\"hidden\" name=\"total-subjects\" value=\""+totalSubjects+"\"></form></body></html>");
			PrintWriter writer = response.getWriter();
			writer.println(html);
			writer.close();
		}
		else{
			PrintWriter writer = response.getWriter();
			writer.println("<html><head><title>Record not found!</title></head><body><h2>This record doesn't exist!</h2></body></html>");
			writer.close();
		}	
	}
}
package org.ashish.question1_login;

import java.io.IOException;

import org.ashish.question4_adddetailsafterlogin.Question4;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Question1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Question1() {
        super();
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		String uname = request.getParameter("uname");
		String pass = request.getParameter("pass");
		String type = request.getParameter("type");

		
		boolean userConfirmed = false;
		NodeList users = null;
		Node userNode = null;
		Element userElement = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File("/home/ashish/Documents/workspace/eclipse/Practical2/WebContent/WEB-INF/userfile.xml"));

			Element root = document.getDocumentElement();
			//optional
			root.normalize();
			
			if("student".compareToIgnoreCase(type.trim())==0)
			{
				users = document.getElementsByTagName("student");
			}
			else if("faculty".compareToIgnoreCase(type.trim())==0)
			{
				users = document.getElementsByTagName("faculty");
			}
		
			for(int temp = 0; temp< users.getLength(); temp++)
			{
				userNode = users.item(temp);
				if(userNode.getNodeType() == Node.ELEMENT_NODE){
					userElement = (Element)userNode;
					
					if(userElement != null && uname.trim().toLowerCase().equalsIgnoreCase(userElement.getElementsByTagName("uname").item(0).getTextContent().trim().toLowerCase()))
					{
						if(pass.equals(userElement.getElementsByTagName("pass").item(0).getTextContent()))
						{
							userConfirmed = true;
							break;
						}
					}
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		if(userConfirmed)
		{
			/*PrintWriter writer = response.getWriter();
			writer.println("<html><head><title>Login</title></head><body><p>");
			writer.println("You are found in our repository! You are now logged in.");
			writer.println("</p></body></html>");
			writer.close();*/
			Question4.redirectUser(type, response);
		}
		else
		{
			
			PrintWriter writer = response.getWriter();
			writer.println("<html><head><title>Login</title></head><body><p>");
			writer.println("You are not found in our repository! You are NOT logged in.");
			writer.println("</p></body></html>");
			writer.close();
		}
		
	}
}
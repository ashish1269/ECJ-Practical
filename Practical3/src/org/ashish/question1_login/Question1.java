package org.ashish.question1_login;

import java.io.IOException;

import org.ashish.question4_adddetailsafterlogin.Question4;
import org.ashish.userhits.CountUserHits;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;

import java.io.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Question1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect("login.html");
	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//Count hit/visit
		CountUserHits.increaseHit();

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
			File userFile = null;
			Document document = null;
			DocumentBuilder builder = factory.newDocumentBuilder();
			String dataFileUrl = getServletConfig().getInitParameter("data-file-url");
			if(dataFileUrl == "" || dataFileUrl == null)
			{
				System.out.println("Data file url is not mentioned. Exiting!");
				return;
			}
			else
			{	
				userFile = new File(dataFileUrl);
			}

			int temp=0;
			if(userFile==null || !userFile.exists())
			{
				System.out.println("userfile.xml not found. Please register some users first");
				userConfirmed = false;
			}
			else{
				
				document = builder.parse(new File(dataFileUrl));
				
				if("student".compareToIgnoreCase(type.trim())==0)
				{
					users = document.getElementsByTagName("student");
				}
				else if("faculty".compareToIgnoreCase(type.trim())==0)
				{
					users = document.getElementsByTagName("faculty");
				}
				for(; temp< users.getLength(); temp++)
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
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		if(userConfirmed)
		{
			Question4.redirectUser(type, response);
		}
		else
		{
			request.setAttribute("registration-on-login-fail", new Boolean(true));
			RequestDispatcher rd = 	request.getRequestDispatcher("registration-form-on-login-fail.html");
			rd.include(request, response);
		}
		
	}
}
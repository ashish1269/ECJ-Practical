package org.ashish.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ashish.models.UserDao;
/*import org.ashish.models.Login;
import org.ashish.models.LoginDao;*/
import org.ashish.models.User;

public class RegisterUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//In case GET method request redirect user to register-user.html to fill registration details.
		resp.sendRedirect("register-user.html");
	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//Count hit/visit
		CountUserHits.increaseHit();

		String name = null, uname=null, pass=null, address=null, email=null, sex=null,contact=null, altContact=null, edu10 = null,
				edu12 = null, eduBach=null, eduMast=null, utype=null,   EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$",
				MARKS_REGEX = "((^(100)([.][0]+)?$)|(^[0-9]{1,2}([.][0-9]+)?$))";
		
		Map<String,String> userMap = new HashMap<>();
		
		boolean error = false;
		
		PrintWriter writer = response.getWriter();
		
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
			
			writer.println("<html><head><title>Login</title></head><body>");

			
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

				
				userMap.put("userName", uname);
				userMap.put("fullName", name);
				userMap.put("emailId", email);
				userMap.put("password", pass);
				userMap.put("sex", sex);
				userMap.put("address", address);
				userMap.put("contact", contact);
				userMap.put("altContact", altContact);
				userMap.put("education10", edu10);
				userMap.put("education12", edu12);
				userMap.put("educationGrad", eduBach);
				userMap.put("educationMast", eduMast);
				userMap.put("userType", utype);
				
				UserDao user = User.getUserInstance();

				/*
				 * If user information is saved on registration
				 * save her details to login table as well so
				 * that she can login to system
				 * 
				 * If there is error in saving login information
				 * then delete the respective user information
				 * from user table as well.
				 */
				if(!user.setUserInfo(userMap))
				{
					error = true;
				}

				
				if(error)
				{
					writer.println("<p>There is some problem on our side. We are taking care of this."+
							"\nPlease login after some time.</p>");
				}
				else
				{
					writer.println("<p>Details saved. <big>You are <em>registered!</em></big></p>");
					
					/*-----------Ask user to login after registration---------*/
					request.setAttribute("login-after-registration", new Boolean(true));
					RequestDispatcher rd = 	request.getRequestDispatcher("login-form.html");
					rd.include(request, response);
				}

					
			}
		
				
		writer.println("</body></html>");
		writer.close();
				
	}

}
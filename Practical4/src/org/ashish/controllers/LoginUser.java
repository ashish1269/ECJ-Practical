package org.ashish.controllers;

import java.io.IOException;

import org.ashish.models.Login;
import org.ashish.models.LoginDao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginUser extends HttpServlet {
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

		String userType = null;
		
		LoginDao login = Login.getLoginInstance();
		
		userType = login.loginUser(uname, pass);
		
		if("s".equals(userType))
			userType = "student";
		else if("f".equals(userType))
			userType = "faculty";
		/*
		 * If login successful then
		 * if type=='student' add student information
		 * else add marks of students
		 * This is done by redirection
		 * 
		 * 
		 * If login fails then redirect
		 * user to registration page and
		 * ask her to register first
		 * 
		 */
		
		if(userType != null)
		{	
			HttpSession session = request.getSession();
			session.setAttribute("userName", uname);
			session.setMaxInactiveInterval(20*60);
			
			request.setAttribute("user-name", uname);
			
			if("student".equals(userType))
			{
				request.getRequestDispatcher("student-home.jsp").forward(request, response);
			}
			else if("faculty".equals(userType))
			{
				request.getRequestDispatcher("faculty-home.jsp").forward(request, response);
			}
		}
		else
		{
			request.setAttribute("registration-on-login-fail", new Boolean(true));
			RequestDispatcher rd = 	request.getRequestDispatcher("registration-form-on-login-fail.html");
			rd.include(request, response);
		}
	}
}
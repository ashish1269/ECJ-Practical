package org.ashish.question4_adddetailsafterlogin;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

public class Question4 extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	public static void redirectUser(String userType, HttpServletResponse response){
		
		if("student".equals(userType))
		{
			try {
				response.sendRedirect("enroll-student.html");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if("faculty".equals(userType))
		{
			try {
				response.sendRedirect("add-marks.html");
			} catch (IOException e) {
				System.out.println("Error while redirecting faculty to addmarks page");
			}
		}
	}
/*    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	*//**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 *//*
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}*/

}

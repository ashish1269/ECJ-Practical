package org.ashish.controllers;

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

import org.ashish.models.StudentCourse;

public class EnrollStudent extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//Count hit/visit
		CountUserHits.increaseHit();

		String userName = null, email=null, enrolNo = null, course = null, EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		List<String> subjects = null;
		boolean error = false;
		
		PrintWriter writer = response.getWriter();
		try{

			response.setContentType("text/html");
			userName = request.getParameter("uname");
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


			if(userName==null || email==null || enrolNo==null || course==null)
			{
				writer.println("<p style=\"color:red\">No field can't be left blank.</p>");
				error = true;
			}
			if(!userName.matches("[a-zA-Z0-9]+{1,30}"))
			{
				writer.println("<p style=\"color:red\">Name should contain alphanumeric value only and its length must be less than 30.</p>");
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

				
				
				StudentCourse studentCourse = StudentCourse.getCourseInstance();
				
				studentCourse.setEnrollNo(Long.parseLong(enrolNo));
				studentCourse.setUserName(userName);
				studentCourse.setCourseName(course);
				studentCourse.setSubject1(subjects.get(0));
				studentCourse.setSubject2(subjects.get(1));
				studentCourse.setSubject3(subjects.get(2));
				
				/*
				 * At least three subjects are mandatory
				 * but 4th and 5th subjects are optional 
				 *
				 * If there is no 5th subject set it to blank
				 * 
				 */
				if(subjects.size() >= 4 )
					studentCourse.setSubject4(subjects.get(3));
				else
					studentCourse.setSubject4("");
				
				/*
				 * 
				 * The 5th optional subject
				 * 
				 * If there is no 5th subject set it to blank
				 * 
				 */
				if(subjects.size() == 5 )
					studentCourse.setSubject5(subjects.get(4));
				else
					studentCourse.setSubject5("");
				
				
				if(studentCourse.setStudentInfo())
				{
					writer.println("<p>Details saved. <big>You are <em>enrolled!</em></big></p>");
				}
				else
				{
					writer.println("<p>Details not saved. Error on server. <big>You are <em>not enrolled!</em></big></p>");
					error = true;
				}
			}
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
					writer.println("<div><b>Name:</b> "+userName+"</div>");
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
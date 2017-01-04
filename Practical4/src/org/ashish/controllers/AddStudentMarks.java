package org.ashish.controllers;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ashish.models.Marks;
import org.ashish.models.StudentCourse;


public class AddStudentMarks extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Count hit/visit
		CountUserHits.increaseHit();

		response.setContentType("text/html");
		String course = request.getParameter("course");
		String userName = request.getParameter("name");

		boolean error = false;		
		

		StudentCourse studentCourse = StudentCourse.getCourseInstance();
		
		studentCourse.setUserName(userName);
		
		studentCourse.getCourseInfo();
		

		Marks marks = Marks.getMarksInstance();
		
		marks.setEnrollNo(studentCourse.getEnrollNo());
		marks.setSubject1(Float.parseFloat(request.getParameter("subject1")));
		marks.setSubject2(Float.parseFloat(request.getParameter("subject2")));
		marks.setSubject3(Float.parseFloat(request.getParameter("subject3")));
		
		/* 
		 * if there are 4 or more subjects
		 * save marks of 4th subject first
		 *  
		 */
		
		if(studentCourse.getSubject4().trim().length() != 0)
		{
			marks.setSubject4(Float.parseFloat(request.getParameter("subject4")));
		}
		else
		{
			marks.setSubject4(-1);
		}
		

		/* 
		 * if there are 5 subjects
		 * save marks of 5th subject
		 *  
		 */
		
		if(studentCourse.getSubject5().trim().length() != 0)
		{
			marks.setSubject5(Float.parseFloat(request.getParameter("subject5")));
		}
		else
		{
			marks.setSubject5(-1);
		}
		
		if(!marks.saveMarks())
			error = true;

		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		writer.println("<html><head><title>Register</title></head><body>");
		
		if(!error)
		{
			
				writer.println("<p><big>Marks have been added.</big></p>");
				writer.println("<div><table><tr><td><b>Name</b></td><td>"+userName+"</td></tr>");
				writer.println("<tr><td><b>Course</b></td><td>"+course+"</td></tr>");
				writer.println("<tr><td><b>Enrollment Number</b></td><td>"+studentCourse.getEnrollNo()+"</td></tr>");
				writer.println("</table></div>");
				writer.println("<div><b>Marks: </b>");
				writer.println("<table>");

				writer.println("<tr><td> "+studentCourse.getSubject1()+"</td><td>"+marks.getSubject1()+"</td></tr>");
				writer.println("<tr><td> "+studentCourse.getSubject2()+"</td><td>"+marks.getSubject2()+"</td></tr>");
				writer.println("<tr><td> "+studentCourse.getSubject3()+"</td><td>"+marks.getSubject3()+"</td></tr>");
				if(marks.getSubject4() >= 0) writer.println("<tr><td> "+studentCourse.getSubject4()+"</td><td>"+marks.getSubject4()+"</td></tr>");
				if(marks.getSubject5() >= 0) writer.println("<tr><td> "+studentCourse.getSubject5()+"</td><td>"+marks.getSubject5()+"</td></tr>");
				writer.println("</table></div>");
				writer.println("</body></html>");
			
		}
		if(error)
		{
			writer.println("<p><big>Marks have not been added! Probably they aren't in range (0-100) or aren't in proper format (real number).</big></p></body></html>");
		}
		
		writer.close();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//Count hit/visit
		CountUserHits.increaseHit();

		String enrollNo = request.getParameter("enrol-no");
		
		boolean error = false;
		if(!enrollNo.matches("[0-9]+"))
		{
			error = true;
		}
		if(!error)
		{
			StringBuilder html = new StringBuilder("<html><head><title>Add Marks</title></head>"+
						"<body><form action=\"get-student-details\" method=\"POST\">");
			
			String course = null, userName = null;
			List<String> subjects = new ArrayList<>();
			
			
			StudentCourse studentCourse = StudentCourse.getCourseInstance();
			
			studentCourse.setEnrollNo(Long.parseLong(enrollNo));
			
			studentCourse.getCourseInfo();
			
			course = studentCourse.getCourseName();
			userName = studentCourse.getUserName();
			
			subjects.add(studentCourse.getSubject1());
			subjects.add(studentCourse.getSubject2());
			subjects.add(studentCourse.getSubject3());
			subjects.add(studentCourse.getSubject4());
			subjects.add(studentCourse.getSubject5());
			
			//if student with the sent enrollment number is found
			if(userName!=null)
			{
				html.append("<div><label>Name: </label><span>"+userName+"</span></div><input type=\"hidden\" name=\"name\" value=\""+userName+"\">"+
						"<div><label>Course: </label><span>"+course+"</span></div><input type=\"hidden\" name=\"course\" value=\""+course+"\">"+
						"<div><label><big>Add marks: </big></label></div>");
				if(subjects!=null)
					
				for(int j=0; j<subjects.size(); j++)
				{
					if(subjects.get(j).trim().length() != 0)
					{
						html.append("<div><label>"+subjects.get(j)+": </label><input type=\"text\" name=\"subject"+(j+1)+"\"></div><input type=\"hidden\" name=\"subject\" value=\""+subjects.get(j)+"\">");
					}
				}
				html.append("<div><button type=\"submit\">Add marks</button></div></form></body></html>");
			}
			//if student is not found in the record
			else
			{
				html.append("<h2>Student record with this enrollment number doesn't exist. First enroll the student.</h2></body></html>");
			}
			PrintWriter writer = response.getWriter();
			writer.println(html);
			writer.close();
		}
		//If the error == true
		else
		{
			PrintWriter writer = response.getWriter();
			writer.println("<html><head><title>Error!</title></head><body><h2>Enter enrolment number in correct format</h2></body></html>");
			writer.close();
		}	
	}
}
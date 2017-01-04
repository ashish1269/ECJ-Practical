package org.ashish.question2;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Question2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	String firstDay= null;
    	int totalDays = 0;
    	int totalHolidays=8;
    	
    	response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		writer.println("<html><head><title>Number of days</title></head><body><p>");
		
		
    	firstDay = request.getParameter("first-day");
    	totalDays = Integer.parseInt(request.getParameter("total-days"));
    	
    
    	if(firstDay==null){
    		writer.println("Enter first day</p></body></html>");
    		writer.close();
    		return;
    	}
    		
    	if("thirsday".equalsIgnoreCase("first-day"))
    		totalHolidays += totalDays-28 - 2;
    	else if("friday".equalsIgnoreCase(firstDay) && totalDays==30)
    		totalHolidays += 1;
    	else if("friday".equalsIgnoreCase(firstDay) && totalDays==31)
    		totalHolidays += 2;
    	else if("saturday".equalsIgnoreCase(firstDay))
    		totalHolidays += totalDays-29;
    	else if("sunday".equalsIgnoreCase(firstDay) && totalDays>28)
    		totalHolidays += 1;
    	else if(!("monday".equalsIgnoreCase(firstDay) || "tuesday".equalsIgnoreCase(firstDay) || "wednesday".equalsIgnoreCase(firstDay)))
    	{
    		writer.println("This is not correct day</p></body></html>");
    		writer.close();
    		return;
    	}
    	
		writer.println(totalHolidays);
		writer.println("</p></body></html>");
		writer.close();
	}

}
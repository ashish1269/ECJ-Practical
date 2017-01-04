package org.ashish.question3;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Question3 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Question3() {
        super();

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
    	response.setContentType("text/html");
    	PrintWriter writer = response.getWriter();
    	writer.println("<html><head><title>Number Grid</title></head><body align=\"center\">");
    	writer.println("<table>");
    	int rows = Integer.parseInt(request.getParameter("row-num"));
    	int cols = Integer.parseInt(request.getParameter("col-num"));
    	
    	Random rn = new Random();
    	int randomNum;
    	final int max = 9;
    	final int min = 1;
    	for(int i=0; i< rows; i++)
    	{
    		writer.println("<tr>");
    		for(int j=0; j< cols; j++)
    		{	
    			randomNum = rn.nextInt((max-min)+1)+min;
    			
    			writer.println("<td>"+randomNum+"</td>");
    		}
    		writer.println("</tr>");
    	}
    	writer.println("</table></body></html>");
    	writer.close();
	}

}
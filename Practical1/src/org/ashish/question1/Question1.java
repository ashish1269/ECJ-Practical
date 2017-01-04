package org.ashish.question1;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Question1 extends HttpServlet {

	private static final long serialVersionUID = 4221817343303617962L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		char vowels[] = {'a','e','i','o','u'};
		String string = request.getParameter("string");
		PrintWriter writer = response.getWriter();
		response.setContentType("text/html");
		writer.println("<html><head><title>Vowels</title></head><body><p>");
		for(int i=0; i<string.length(); i++)
			if(Arrays.binarySearch(vowels,  string.charAt(i))>=0)
				writer.println( string.charAt(i)+" : "+(i+1)+"<br/>");
		writer.println("</p></body></html>");
		writer.close();
	}

}
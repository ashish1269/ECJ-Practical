package org.ashish.demouserhits;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ashish.userhits.CountUserHits;

public class DemoUserHits extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Showing the hits is responsibility of doPost()
		//If any request comes in GET method there's
		//still a need to increase the hits by one
		CountUserHits.increaseHit();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long hit;
		hit = CountUserHits.increaseHit();
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		writer.println("<html><head><title>Hits On Site</title></head><body>");
		writer.println("<h1>Hits are: "+hit+"</h1>");
		writer.println("<h2>Click here again to see the effect of your own hit/visit on site</h2>");
		writer.println("<form action=\"total-user-hits\" method=\"POST\">"+
				"<div><button type=\"submit\" value=\"show-hits\">Click again.</button></div>"+
				"</form>");
		writer.println("</body></html>");
		writer.close();
	}
}
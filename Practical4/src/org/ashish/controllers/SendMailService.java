package org.ashish.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ashish.models.MailBox;
import org.ashish.models.User;


public class SendMailService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SendMailService() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Count hit/visit
		CountUserHits.increaseHit();
		response.sendRedirect("send-mail.jsp");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Count hit/visit
		CountUserHits.increaseHit();
		
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		writer.println("<html><head><title>SendEmail</title></head><body>");
		
		String sender = request.getParameter("user-name");
		String receiver = request.getParameter("send-to");
		String subject = request.getParameter("subject");
		StringBuffer message = new StringBuffer(request.getParameter("message"));
		
		boolean error = false;
		
		if(!receiver.matches("[a-zA-Z0-9]+") || !User.getUserInstance().userNameAvailable(receiver))
		{
			error = true;
			writer.println("<h2 style=\"color:red\">Such a user with user name "+receiver+" doesn't exist");
		}
		
		if(!error)
		{
			MailBox mail = new MailBox();
			
			mail.setUserName(sender);
			mail.setSendTo(receiver);
			mail.setSubject(subject);
			mail.setMessage(message.toString());
			
			mail.sendMail();
		
			writer.println("<h2>The mail has been sent to "+receiver);
		}
		writer.println("</body></html>");
		writer.close();
	}

}

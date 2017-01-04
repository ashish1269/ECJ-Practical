package org.ashish.models;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MailBox implements MailBoxDao {

	private static String dbDriver = null;
	private static String dbUrl = null;
	private static String dbUser = null;
	private static String dbPass = null;
	private static Connection con = null;
	
	private String userName;
	private String sendTo;
	private String subject;
	private String message;
	
	

	@Override
	public List<?> refreshMailbox() {
		List<Mail> mails = new ArrayList<>();
		
		try {
			if(con==null)
				con=DriverManager.getConnection(dbUrl, dbUser, dbPass);
			
			Statement statement = con.createStatement();
			
			String query = "select user_name as received_from, subject, message, time from mail_box where send_to='"+userName+"' and status='"+"u'";
			
			ResultSet resultSet = null;
			
			resultSet = statement.executeQuery(query);
			
			while(resultSet.next())
			{
				Mail mail = new Mail();
				mail.userName = userName;
				mail.receivedFrom = resultSet.getString("received_from");
				mail.subject = resultSet.getString("subject");
				mail.message = resultSet.getString("message");
				mail.time = resultSet.getString("time");
				mails.add(mail);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return mails;
	}

	@Override
	public boolean sendMail() {

		meetDB();
		
		try {
			if(con==null)
				con=DriverManager.getConnection(dbUrl, dbUser, dbPass);

			Statement statement = con.createStatement();
			
			String query = "insert into mail_box values('"+userName+"','"+sendTo+"','"+ subject+"','"+message+"', null)";

			if(statement.executeUpdate(query) == 1)
			{
				return true;
			}
			
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	public boolean meetDB()
	{
		try {
			
			Properties DBProperty = null;
			String propFileName = null;
			InputStream inputStream = null;	
			propFileName = "database.properties";

			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);		
			
			DBProperty =new Properties();
			DBProperty.load(inputStream);
			
						
			dbDriver = DBProperty.getProperty("dbDriver");
			dbUrl = DBProperty.getProperty("connectionUrl");
			dbUser = DBProperty.getProperty("username");
			dbPass = DBProperty.getProperty("password");
			
			Class.forName(dbDriver);

			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	

	public final String getUserName() {
		return userName;
	}

	public final void setUserName(String userName) {
		this.userName = userName;
	}

	public final String getSendTo() {
		return sendTo;
	}

	public final void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public final String getSubject() {
		return subject;
	}

	public final void setSubject(String subject) {
		this.subject = subject;
	}

	public final String getMessage() {
		return message;
	}

	public final void setMessage(String message) {
		this.message = message;
	}

}

package org.ashish.models;

import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.sql.ResultSet;
import java.sql.Connection;

public final class User implements UserDao{

	public static User userInstance = null;
	
	private Map<String,String> user = null;

	static String dbDriver = null;
	static String dbUrl = null;
	static String dbUser = null;
	static String dbPass = null;

	static Connection con = null;
	
	private User(){
	}
	

	
	public static User getUserInstance()
	{
		if(userInstance==null)
			userInstance = new User();
		
		return userInstance;
	}
	
	@Override
	public final boolean setUserInfo(Map<String, String> user) {
		this.user = user;
		return	saveInformation();
	}

	@Override
	public final Map<String, String> getUserInfo(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final boolean userNameAvailable(String userName) {
		 
		ResultSet queryResult = null;
		
		meetDB();
		
		try {
				if(con==null)
					con=DriverManager.getConnection(dbUrl, dbUser, dbPass);
				Statement statement = con.createStatement();
				
				String query = "select user_name from user where user_name = '"+userName+"'";
				
				queryResult = statement.executeQuery(query);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		if(queryResult == null)
			return true;
		
		return false;
	}

	private boolean saveInformation ()
	{

		boolean dbTransactionSuccess = false;
	
		if(meetDB() && !userNameAvailable(user.get("userName")))
		try {
		
			
			if(con==null)
				con=DriverManager.getConnection(dbUrl, dbUser, dbPass);
			
			Statement statement = con.createStatement();
			
			String query = "insert into user values('"+
					user.get("userName")+"','"+
					user.get("fullName")+"','"+
					user.get("emailId")+"','"+
					user.get("password")+"','"+
					user.get("sex").charAt(0)+"','"+
					user.get("address")+"',"+
					Long.parseLong(user.get("contact"))+","+
					Long.parseLong(user.get("altContact"))+","+
					Float.parseFloat(user.get("education10"))+","+
					Float.parseFloat(user.get("education12"))+","+
					Float.parseFloat(user.get("educationGrad"))+","+
					Float.parseFloat(user.get("educationMast"))+",'"+
					user.get("userType").charAt(0)+"')";
			
			
			if(statement.executeUpdate(query)==1)			
				dbTransactionSuccess = true;
			
		} catch (SQLException e) {

			dbTransactionSuccess = false;
		}
		
		else
		{
			dbTransactionSuccess = false;
		}
		
		return dbTransactionSuccess;
		
	}

	@Override
	public boolean deleteUserInfo(String userName) {

		try {
			
			
			meetDB();
			
			if(con==null)
				con=DriverManager.getConnection(dbUrl, dbUser, dbPass);
			
			String query = "delete from user where user_name='"+userName+"'";
			
			Statement statement = con.createStatement();
			
			statement.executeUpdate(query);
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
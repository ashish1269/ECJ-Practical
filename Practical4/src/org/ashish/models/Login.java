package org.ashish.models;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Login implements LoginDao{


	static String dbDriver = null;
	static String dbUrl = null;
	static String dbUser = null;
	static String dbPass = null;
	
	static Connection con = null;

	
	private Login(){
			
	}

	
	private static Login loginInstance = null;
	
	public static Login getLoginInstance()
	{
		if(loginInstance==null)
			loginInstance = new Login();
		
		return loginInstance;
	}
	
	@Override
	public boolean userExists(String userName) {
		
		ResultSet queryResult = null;
		
		if(meetDB())
		try {
				if(con==null)
					con=DriverManager.getConnection(dbUrl, dbUser, dbPass);
				Statement statement = con.createStatement();
				
				String query = "select count(*) total_rows from user where user_name = '"+userName+"'";
				
				queryResult = statement.executeQuery(query);
				
				while(queryResult.next())
				{
					if(queryResult.getInt("total_rows")==1)
						return true;
					
					return false;
				}
				/*if(queryResult.getString("user_name") != null)
					return true;*/
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return false;
	}

	@Override
	public boolean setLoginInfo(String userName, String password) {

		boolean dbTransactionSuccess = false;
		
		if(meetDB() && !userExists("userName"))
		try {
			
			if(con==null)
				con=DriverManager.getConnection(dbUrl, dbUser, dbPass);
			
			Statement statement = con.createStatement();
			
			String query = "insert into login values("+
						"'"+userName+"',"+
						"'"+password+"')";
			
			if(statement.executeUpdate(query)==1)
				dbTransactionSuccess = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dbTransactionSuccess = false;
		}
		else
		{
			dbTransactionSuccess = false;
		}

		return dbTransactionSuccess;
	}

	@Override
	public String loginUser(String userName, String password) {

		ResultSet queryResult = null;
		
		if(meetDB())
		try {
				if(con==null)
					con=DriverManager.getConnection(dbUrl, dbUser, dbPass);
				Statement statement = con.createStatement();
				
				String query = "select password, user_type from user where user_name = '"+userName+"'";
				
				
				queryResult = statement.executeQuery(query);
				
				while(queryResult.next())
				{
					if(password.equals(queryResult.getString("password")))
					{
						return queryResult.getString("user_type");
					}
					break;
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		else
			System.out.println("Inside loginUser().... meetDB() returned false!");
		return null;

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
	
}
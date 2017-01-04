package org.ashish.models;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class Marks implements MarksDao {

	/*
	 * Class attributes
	 */
	long enrollNo;
	float subject1;
	float subject2;
	float subject3;
	float subject4;
	float subject5;
	
	
	
	public static Marks marksInstance = null;
	
	private static String dbDriver = null;
	private static String dbUrl = null;
	private static String dbUser = null;
	private static String dbPass = null;

	private static Connection con = null;
	
	/*
	 * Private constructor to avoid
	 * others from creating an object
	 * of this class
	 * 
	 */
	
	private Marks(){}
	
	/*
	 * Singleton pattern.
	 * Factory pattern.
	 */
	
	public static Marks getMarksInstance(){
		
		if(marksInstance == null)
			marksInstance = new Marks();
		
		return marksInstance;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.ashish.models.MarksDao#saveMarks(long, java.util.Map)
	 * 
	 * This method saves marks if they are not already saved in DB
	 * If marks of a student are already saved in DB they are updated
	 * On update method caller is NOT informed about update.
	 * It just looks like marks have been saved for the first time.
	 * 
	 */
	
	
	
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

	@Override
	public boolean saveMarks() {

		if(meetDB())
		try {
			
			if(con==null)
				con=DriverManager.getConnection(dbUrl, dbUser, dbPass);
			
			Statement statement = con.createStatement();
			
			StringBuilder query = new StringBuilder("");
			
			if(!marksExist())
			{
				query.append("insert into marks values("+enrollNo+","+subject1+
							","+subject2+","+subject3);
			
				if(subject4 != -1)
				{
					query.append(","+subject4);
				}
				else
				{
					query.append(",null");
				}
				
				if(subject5 != -1)
				{
					query.append(","+subject5);
				}
				else
				{
					query.append(",null");
				}
				
				query.append(")");
			}
			else
			{
				query.append("update table marks set subject1="+subject1+", subject2="+subject2+", subject3="+subject3);
				
				if(subject4 != -1)
				{
					query.append(", subject4="+subject4);
				}
				else
				{
					query.append(",null");
				}
				
				if(subject5 != -1)
				{
					query.append(", subject5="+subject5);
				}
				else
				{
					query.append(",null");
				}
				
				query.append(" where enroll_no = "+enrollNo+";");
			}

			if(statement.executeUpdate(query.toString())==1)
				return true;
			else 
				return false;
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}

		return false;
	}

	
	
	@Override
	public boolean getMarks() {

		if(meetDB())
		try {
			
			if(con==null)
				con=DriverManager.getConnection(dbUrl, dbUser, dbPass);
			
			Statement statement = con.createStatement();
			
			String query = "select subject1, subject2, subject3, subject4, subject5 from marks where enroll_no = "+enrollNo;
			
			ResultSet rs = statement.executeQuery(query);
				
			while(rs.next())
			{
				subject1 = rs.getFloat("subject1");
				subject2 = rs.getFloat("subject2");
				subject3 = rs.getFloat("subject3");
				subject4 = rs.getFloat("subject4");
				subject5 = rs.getFloat("subject5");
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	

	@Override
	public boolean marksExist() {

		
		try {
			if(con==null)
				con=DriverManager.getConnection(dbUrl, dbUser, dbPass);
			
			Statement statement = con.createStatement();

			String query = "select count(*) as totalRows from marks where enroll_no="+enrollNo;
			
			ResultSet resultSet = null;
			
			resultSet = statement.executeQuery(query);
			
			while(resultSet.next())
			{
				if(resultSet.getInt("totalRows")==1)
					return true;
				return false;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
		

		
		return false;
	}

	public final long getEnrollNo() {
		return enrollNo;
	}

	public final void setEnrollNo(long enrollNo) {
		this.enrollNo = enrollNo;
	}

	public final float getSubject1() {
		return subject1;
	}

	public final void setSubject1(float subject1) {
		this.subject1 = subject1;
	}

	public final float getSubject2() {
		return subject2;
	}

	public final void setSubject2(float subject2) {
		this.subject2 = subject2;
	}

	public final float getSubject3() {
		return subject3;
	}

	public final void setSubject3(float subject3) {
		this.subject3 = subject3;
	}

	public final float getSubject4() {
		return subject4;
	}

	public final void setSubject4(float subject4) {
		this.subject4 = subject4;
	}

	public final float getSubject5() {
		return subject5;
	}

	public final void setSubject5(float subject5) {
		this.subject5 = subject5;
	}

}
package org.ashish.models;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class StudentCourse implements StudentCourseDao {

	long enrollNo;
	String userName;
	String courseName;
	String subject1;
	String subject2;
	String subject3;
	String subject4;
	String subject5;
	
	public static StudentCourse courseInstance = null;
	
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
	
	private StudentCourse(){}
	
	/*
	 * Singleton pattern.
	 * Factory pattern.
	 */
	
	public static StudentCourse getCourseInstance(){
		
		if(courseInstance == null)
			courseInstance = new StudentCourse();
		
		return courseInstance;
	}

	
	@Override
	public boolean marksExist() {
	
		try {
			if(con==null)
				con=DriverManager.getConnection(dbUrl, dbUser, dbPass);

			Statement statement = con.createStatement();
			
			String query = "select user_name from course where enroll_no="+enrollNo;
			
			ResultSet resultSet = statement.executeQuery(query);
			
			while(resultSet.next())
			{
				if(resultSet.getString("user_name")==this.userName)
				{
					return true;
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return false;
	}

	@Override
	public boolean setStudentInfo() {

		if(meetDB())
		try {
			
			if(con==null)
				con=DriverManager.getConnection(dbUrl, dbUser, dbPass);

			Statement statement = con.createStatement();
			
			String query=null;
			
			/* 
			 * 
			 * If marks aren't already saved insert them
			 * if they are saved already update them....
			 * 
			 */
			if(!marksExist())
				query = "insert into course values("+enrollNo+",'"+userName+"','"+courseName+"','"+subject1+"','"+subject2+"','"+subject3+"','"+subject4+"','"+subject5+"')";
			else
				query = "update course set subject1='"+subject1+"', subject2='"+subject2+"', subject3='"+subject3+"', subject4='"+subject4+"', subject5='"+subject5+"')";
						
			if(statement.executeUpdate(query)==1)
				return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		else
			System.out.println("Couldn't connect to DB");
		
		return false;
	}

	@Override
	public boolean getCourseInfo() {

		if(meetDB())
		try {
			if(con==null)
				con=DriverManager.getConnection(dbUrl, dbUser, dbPass);
			
			ResultSet rs = null;
			
			Statement statement = con.createStatement();

			String query = "select user_name, course_name, subject1,"+
							" subject2, subject3, subject4, subject5"+
							" from course where enroll_no="+enrollNo;

			rs = statement.executeQuery(query);
			
			while(rs.next())
			{
				userName = rs.getString("user_name");
				courseName = rs.getString("course_name");
				subject1 = rs.getString("subject1");
				subject2 = rs.getString("subject2");
				subject3 = rs.getString("subject3");
				subject4 = rs.getString("subject4");
				subject5 = rs.getString("subject5");
				break;
			}
			
			return true;
			
		} catch (SQLException e) {

			e.printStackTrace();
		}

		else
			System.out.println("Problem getting driver to connect to DB");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public final long getEnrollNo() {
		return enrollNo;
	}

	public final void setEnrollNo(long enrollNo) {
		this.enrollNo = enrollNo;
	}

	public final String getUserName() {
		return userName;
	}

	public final void setUserName(String userName) {
		this.userName = userName;
	}

	public final String getCourseName() {
		return courseName;
	}

	public final void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public final String getSubject1() {
		return subject1;
	}

	public final void setSubject1(String subject1) {
		this.subject1 = subject1;
	}

	public final String getSubject2() {
		return subject2;
	}

	public final void setSubject2(String subject2) {
		this.subject2 = subject2;
	}

	public final String getSubject3() {
		return subject3;
	}

	public final void setSubject3(String subject3) {
		this.subject3 = subject3;
	}

	public final String getSubject4() {
		return subject4;
	}

	public final void setSubject4(String subject4) {
		this.subject4 = subject4;
	}

	public final String getSubject5() {
		return subject5;
	}

	public final void setSubject5(String subject5) {
		this.subject5 = subject5;
	}

}

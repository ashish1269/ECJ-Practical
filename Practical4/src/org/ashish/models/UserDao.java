package org.ashish.models;

import java.util.Map;

public interface UserDao {

	/*
	 * The user (Map) must have following keys:
	 * 
	 * 
	 * fullName
	 * userName
	 * emailId
	 * password
	 * sex
	 * address
	 * contact
	 * altContact
	 * education10
	 * education12
	 * educationGrad
	 * educationMast
	 * userType
	 * 
	 * 
	 * The same Map will be returned by getUserInfo()
	 */
	
	boolean setUserInfo(Map<String, String> user);
	
	Map<String,String> getUserInfo(String userName);
	
	boolean userNameAvailable(String userName);
	
	boolean deleteUserInfo(String userName);
}
package org.ashish.models;

public interface LoginDao {
	public boolean setLoginInfo(String userName, String password);
	public boolean userExists(String userName);
	public String loginUser(String userName, String password);
}

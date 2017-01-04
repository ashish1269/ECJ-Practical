package org.ashish.models;

import java.util.List;

public interface MailBoxDao {
	public List<?> refreshMailbox();
	public boolean sendMail();
}

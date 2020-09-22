package root.busslogic.session.interfaces;

import root.user.model.User;

/*
 * To be Implemented as a Session Service to be invoked in Custom Success Handler
 */
public interface ISessionUser
{
	public User getSessionUser(
	);
	
	public void setSessionUser(
	        User user
	);
}

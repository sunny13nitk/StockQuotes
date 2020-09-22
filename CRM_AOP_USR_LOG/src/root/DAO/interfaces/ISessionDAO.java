package root.DAO.interfaces;

import root.entity.SessionH;

public interface ISessionDAO
{
	public void saveSession(
	        SessionH session
	);
	
	public void saveUpdateSession(
	);
}

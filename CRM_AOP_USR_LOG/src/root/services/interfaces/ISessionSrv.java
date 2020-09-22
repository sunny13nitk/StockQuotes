package root.services.interfaces;

import root.entity.SessionH;

public interface ISessionSrv
{
	public void saveNewSession(
	);
	
	public void initSession(
	        String userName
	);
	
	public SessionH getSessionHeader(
	);
	
	public void saveUpdateSession(
	);
}

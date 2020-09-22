package root.services.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
/*
 * SESSION SCOPED SESSION SERVICE
 */

import root.DAO.interfaces.ISessionDAO;
import root.entity.SessionH;
import root.services.interfaces.ISessionSrv;

@Service
@Scope(
        value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS
)
public class SessionSrv implements ISessionSrv
{
	@Autowired
	private ISessionDAO sessDAO;
	
	private SessionH sessH;
	
	@Override
	@Transactional
	public void saveNewSession(
	)
	{
		if (sessDAO != null && sessH != null)
		{
			sessDAO.saveSession(sessH);
		}
		
	}
	
	@Override
	public void initSession(
	        String userName
	)
	{
		// Validate User Name for Existence using UserSrv
		
		if (userName != null)
		{
			
			// Initialize Session Properties
			this.sessH = new SessionH(userName);
			this.sessH.setSessionGuid(UUID.randomUUID());
			this.sessH.setTimeStamp(new Timestamp(new Date().getTime()));
			
		}
		
	}
	
	@Override
	public SessionH getSessionHeader(
	)
	{
		
		return this.sessH;
	}
	
	@Override
	@Transactional
	public void saveUpdateSession(
	)
	{
		sessDAO.saveUpdateSession();
		
	}
	
}

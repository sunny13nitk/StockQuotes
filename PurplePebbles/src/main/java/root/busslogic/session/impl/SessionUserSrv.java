package root.busslogic.session.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import root.busslogic.session.interfaces.ISessionUser;
import root.user.model.User;

@Service
@Scope(
        value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS
)
public class SessionUserSrv implements ISessionUser
{
	
	private User sessionUser;
	
	@Override
	public User getSessionUser(
	)
	{
		return this.sessionUser;
	}
	
	@Override
	public void setSessionUser(
	        User user
	)
	{
		if (user != null)
		{
			this.sessionUser = user;
		}
		
	}
	
}

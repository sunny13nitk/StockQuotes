package root.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import root.busslogic.Services.interfaces.IConfigSrv;
import root.busslogic.session.interfaces.ISessionUser;
import root.user.model.User;
import root.user.services.interfaces.IUserService;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IConfigSrv configSrv;
	
	@Autowired
	private ISessionUser sessUserSrv; // Custom to hold Session User
	
	@Override
	public void onAuthenticationSuccess(
	        HttpServletRequest request, HttpServletResponse response, Authentication authentication
	) throws IOException, ServletException
	{
		
		System.out.println("\n\nIn customAuthenticationSuccessHandler\n\n");
		
		String userName = authentication.getName();
		
		System.out.println("userName=" + userName);
		
		User theUser = userService.findByUserName(userName);
		
		// Set User credentials in Session Scoped Bean to be referred later
		this.sessUserSrv.setSessionUser(theUser);
		
		// now place in the session
		HttpSession session = request.getSession();
		session.setAttribute("user", theUser);
		
		configSrv.Initialize();
		
		// forward to home page
		
		response.sendRedirect(request.getContextPath() + "/");
	}
	
}

package root.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController
{
	
	@GetMapping(
	    "/myLoginPage"
	)
	public String showLoginPage(
	)
	{
		// return "fancy-login";
		
		return "login-metro";
	}
	
	@GetMapping(
	    "/access-denied"
	)
	public String showaccessDenied(
	)
	{
		return "access-denied";
	}
	
}

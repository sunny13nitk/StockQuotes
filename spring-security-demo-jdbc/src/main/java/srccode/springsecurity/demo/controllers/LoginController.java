package srccode.springsecurity.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController
{
	@GetMapping("/showmyLoginPage")
	public String showMyLoginPage()
	{
		// return "plain-login";

		return "fancy-login";
	}

	@GetMapping("/access-denied")
	public String showAccessDenied()
	{
		return "access-denied";
	}
}

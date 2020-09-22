package srccode.springsecurity.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController
{

	@GetMapping("/")
	public String goToHome()
	{
		return "home";
	}

	/*
	 * @GetMapping("/admin")
	 * public String goToAdmin()
	 * {
	 * return "admin";
	 * }
	 */

}

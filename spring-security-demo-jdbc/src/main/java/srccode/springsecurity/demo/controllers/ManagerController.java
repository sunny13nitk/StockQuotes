package srccode.springsecurity.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/managers")
public class ManagerController
{

	@GetMapping("/main")
	public String goToAdmin()
	{
		return "managers";
	}

}

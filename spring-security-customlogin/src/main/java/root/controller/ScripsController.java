package root.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(
    "/scrips"
)
public class ScripsController
{
	@GetMapping(
	    "/main"
	)
	public String showScripsPage(
	)
	{
		return "scrips-main";
	}
}

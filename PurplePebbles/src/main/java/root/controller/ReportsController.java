package root.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(
    "/reports"
)
public class ReportsController
{
	@GetMapping(
	    "/main"
	)
	public String getreportsMain(
	)
	{
		return "pf-details-metro-new";
	}
	
}

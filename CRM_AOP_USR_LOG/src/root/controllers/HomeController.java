package root.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import root.entity.User;
import root.services.interfaces.ISessionSrv;
import root.services.interfaces.IUserSrv;

@Controller
@RequestMapping(
    "/home"
)
public class HomeController
{
	@Autowired
	private IUserSrv userSrv;
	
	@Autowired
	private ISessionSrv sessSrv;
	
	@GetMapping(
	    "/welcome"
	)
	public String showWelcomePage(
	        Model theModel
	)
	{
		User user = new User();
		theModel.addAttribute("user", user);
		return "logon";
	}
	
	@GetMapping(
	    "/logout"
	)
	public String processLogout(
	        Model theModel
	)
	{
		// Process Session Items save to DB
		sessSrv.saveUpdateSession();
		// New logon Proceed
		User user = new User();
		theModel.addAttribute("user", user);
		return "logon";
	}
	
	@PostMapping(
	    "/logon"
	)
	public String processLogOn(
	        @ModelAttribute(
	            "user"
	        ) User theUser
	)
	{
		if (userSrv != null)
		{
			if (userSrv.getUserbyUser(theUser) != null)
			{
				/*
				 * Session Service to initiate Session
				 */
				sessSrv.initSession(theUser.getUserName());
				
				// Save it right here for Now- later delegate on Logout
				sessSrv.saveNewSession();
				
				return "redirect:/customer/list";
			} else
			{
				return "redirect:/home/welcome";
			}
		}
		
		return "redirect:/customer/list";
	}
}

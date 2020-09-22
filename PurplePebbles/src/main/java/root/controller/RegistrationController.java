package root.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import root.user.helperPOJO.RegUser;
import root.user.services.interfaces.IUserService;

@Controller
@RequestMapping(
    "/register"
)
public class RegistrationController
{
	@Autowired
	private IUserService userSrv;
	
	/*
	 * It is used in the form validation process. Here we add support to trim empty strings to null.
	 */
	@InitBinder
	public void initBinder(
	        WebDataBinder dataBinder
	)
	{
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping(
	    "/showRegistrationForm"
	)
	public String showRegistrationForm(
	        Model model
	)
	{
		RegUser newUser = new RegUser();
		model.addAttribute("newUser", newUser);
		return "registration-form";
	}
	
	@PostMapping(
	    "processRegistrationForm"
	)
	public String processNewUserRegistration(
	        @Valid @ModelAttribute(
	            "newUser"
	        ) RegUser newUser, BindingResult bRes, Model model
	)
	{
		
		// Validate Form for Errors- Found stay at form!
		if (bRes.hasErrors())
		{
			return "registration-form";
		} else
		{
			try
			{
				userSrv.saveUser(newUser);
				// Successful Registration - populate Model attribute with User Name to show on Confirmation Page
				model.addAttribute("username", newUser.getUserName());
			} catch (Exception e)
			{
				// Populate Model with REgistration Error to display on Registration Form
				model.addAttribute("registrationError", e.getMessage());
				return "registration-form";
			}
			
		}
		
		return "registration-confirmation";
	}
	
}

package root.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import root.busslogic.Services.interfaces.ISCUploadSrv;
import root.helperPOJO.FilePathAttr;

@Controller
@RequestMapping(
    "/admin"
)
public class AdminController
{
	@Autowired
	private ISCUploadSrv scUploadSrv;
	
	/*
	 * ----------------- GET MAPPINGS ----------------------
	 */
	
	@GetMapping(
	    "/main"
	)
	public String showAdminPage(
	)
	{
		return "admin-mainM";
	}
	
	@GetMapping(
	    "/upload"
	)
	public String showScripsUploadPage(
	        Model model
	)
	{
		FilePathAttr fpAttr = new FilePathAttr();
		model.addAttribute("filePAttr", fpAttr);
		return "scrip-upload";
	}
	
	/*
	 * ----------------- POST MAPPINGS ----------------------
	 */
	@PostMapping(
	    "/processSCUpload"
	)
	public String processSCUpload(
	        @Valid @ModelAttribute(
	            "filePAttr"
	        ) FilePathAttr fpAttr, BindingResult bRes, Model model
	)
	{
		
		// Validate Form for Errors- Found stay at form!
		if (bRes.hasErrors())
		{
			model.addAttribute("formError", bRes.getAllErrors().get(0));
			return "scrip-upload";
		} else
		{
			// Call the REST API Srv
			
			try
			{
				String resp = scUploadSrv.uploadScrip(fpAttr.getFilePath());
				model.addAttribute("formSucc", resp);
				
			} catch (Exception e)
			{
				model.addAttribute("formError", e.getMessage());
				return "scrip-upload";
			}
		}
		
		return null; // Stats
	}
	
}

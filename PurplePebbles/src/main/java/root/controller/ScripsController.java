package root.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import root.busslogic.SQLProc.entities.Rep_Scrips_BA;
import root.busslogic.Services.interfaces.IConfigSrv;
import root.busslogic.Services.interfaces.IScripsSrv;
import root.busslogic.entity.ConfigDuration;

@Controller
@RequestMapping(
    "/scrips"
)
public class ScripsController
{
	
	@Autowired
	private IConfigSrv configSrv;
	
	@Autowired
	private IScripsSrv scripsSrv;
	
	@GetMapping(
	    "/main"
	)
	public String showScripsPage(
	        Model model
	)
	{
		if (configSrv.getDurations().size() > 0)
		{
			// Load Default Duration
			model.addAttribute("currDur", configSrv.getDurations().get(0).getDuration());
		}
		return "scrips-mainM";
	}
	
	@GetMapping(
	    "/fa"
	)
	public String showScripsforAnalysis(
	        @RequestParam(
	            "did"
	        ) String did, Model model
	)
	{
		if (configSrv != null)
		{
			if (configSrv.getScrips().size() > 0)
			{
				// Load Durations for User Selection
				model.addAttribute("durations", configSrv.getDurations());
				model.addAttribute("selDur", new ConfigDuration());
				String currDur = null;
				
				if (did != null)
				{
					currDur = did;
					
				} else
				{
					if (configSrv.getDurations().size() > 0)
					{
						
						// Load Current Selected Duration
						currDur = configSrv.getDurations().get(0).getDuration();
						
					}
				}
				
				// Load Current Selected Duration
				model.addAttribute("currDur", currDur);
				
				// Invoke for Scrips BA - Stored Procedure
				List<Rep_Scrips_BA> scripsBA = scripsSrv.getAllScrips_Comparison_BasicInfo(currDur);
				if (scripsBA != null)
				{
					model.addAttribute("scripsBA", scripsBA);
				}
			}
		}
		return "scrip-faM";
	}
	
}

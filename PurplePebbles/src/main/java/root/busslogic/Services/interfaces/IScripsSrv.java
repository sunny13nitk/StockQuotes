package root.busslogic.Services.interfaces;

import java.util.List;

import root.busslogic.SQLProc.entities.Rep_Scrips_BA;

public interface IScripsSrv
{
	/*
	 * Get Basic Comparison information for All Scrips in System for a Duration 3Yr, 5Yr, 7Yr, 10Yr
	 */
	public List<Rep_Scrips_BA> getAllScrips_Comparison_BasicInfo(
	        String Duration
	);
	
}

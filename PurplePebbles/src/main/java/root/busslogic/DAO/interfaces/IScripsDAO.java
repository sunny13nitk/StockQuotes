package root.busslogic.DAO.interfaces;

import java.util.List;

import root.busslogic.SQLProc.entities.Rep_Scrips_BA;

public interface IScripsDAO
{
	/*
	 * Get Basic Comparison information for All Scrips in System
	 */
	public List<Rep_Scrips_BA> getAllScrips_Comparison_BasicInfo(
	        String duration
	);
	
}

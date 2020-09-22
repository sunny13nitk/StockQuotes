package root.busslogic.Services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.busslogic.DAO.interfaces.IScripsDAO;
import root.busslogic.SQLProc.entities.Rep_Scrips_BA;
import root.busslogic.Services.interfaces.IScripsSrv;

@Service
public class ScripsSrv implements IScripsSrv
{
	@Autowired
	private IScripsDAO scripsDAO;
	
	@Override
	@Transactional
	public List<Rep_Scrips_BA> getAllScrips_Comparison_BasicInfo(
	        String duration
	)
	{
		return scripsDAO.getAllScrips_Comparison_BasicInfo(duration);
	}
	
}

package root.busslogic.DAO.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import root.busslogic.DAO.interfaces.IScripsDAO;
import root.busslogic.SQLProc.entities.Rep_Scrips_BA;
import root.busslogic.Services.interfaces.IConfigSrv;

@Repository
public class ScripsDAO implements IScripsDAO
{
	
	@Autowired
	private SessionFactory sFac;
	
	@Autowired
	private IConfigSrv configSrv;
	
	@Override
	public List<Rep_Scrips_BA> getAllScrips_Comparison_BasicInfo(
	        String duration
	)
	{
		List<Rep_Scrips_BA> scripsBA    = null;
		String              lv_duration = duration;
		if (lv_duration == null)
		{
			/*
			 * Get 1st Duration maintained in Config_Duration in case of blank duration
			 */
			lv_duration = configSrv.getDurations().get(0).getDuration();
		}
		
		Session sess = sFac.getCurrentSession();
		if (sess != null)
		{
			@SuppressWarnings(
			    "rawtypes"
			)
			Query query = sess.createSQLQuery(
			        "{CALL GetSA_Basic(:period)}").addEntity(Rep_Scrips_BA.class);
			query.setParameter("period", lv_duration);
			
			@SuppressWarnings(
			    "rawtypes"
			)
			List result = query.getResultList();
			if (result != null)
			{
				if (result.size() > 0)
				{
					scripsBA = new ArrayList<Rep_Scrips_BA>();
					for (int i = 0; i < result.size(); i++)
					{
						Rep_Scrips_BA scripBA = (Rep_Scrips_BA) result.get(i);
						scripsBA.add(scripBA);
					}
				}
			}
		}
		
		return scripsBA;
	}
	
}

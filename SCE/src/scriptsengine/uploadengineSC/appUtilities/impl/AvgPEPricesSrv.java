package scriptsengine.uploadengineSC.appUtilities.impl;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.exceptions.EX_InvalidRelationException;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengineSC.appUtilities.defn.AvgPEPricesPojo;
import scriptsengine.uploadengineSC.appUtilities.interfaces.IAvgPEPricesSrv;
import scriptsengine.uploadengineSC.entities.EN_SC_General;
import scriptsengine.uploadengineSC.entities.EN_SC_Trends;
import scriptsengine.uploadengineSC.scripSheetServices.interfaces.ISCExistsDB_Srv;

@Service("AvgPEPricesSrv")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AvgPEPricesSrv implements IAvgPEPricesSrv
{

	/**
	 * AUTOWIRED BEANS
	 */

	@Autowired
	private ISCExistsDB_Srv	scEXSrv;

	/**
	 * PRIVATE PROPERTIES
	 */
	private EN_SC_General	scRoot;
	private String			relTrends	= "EN_SC_Trends_Rel";
	private String			period3Yr	= "3Yr";
	private String			period5Yr	= "5Yr";

	/**
	 * INTERFACE METHODS
	 */
	@Override
	public AvgPEPricesPojo getAvgPricesforScCode(String ScCode) throws EX_General
	{
		AvgPEPricesPojo avgPrices_Pojo = null;
		if (ScCode != null && scEXSrv != null)
		{
			this.scRoot = scEXSrv.Get_ScripExisting_DB(ScCode);
			if (scRoot != null)
			{
				avgPrices_Pojo = getAvgPricesforScRoot(scRoot);
			}
		}
		return avgPrices_Pojo;
	}

	@Override
	public AvgPEPricesPojo getAvgPricesforScDesc(String ScDescBeginsWith) throws EX_General
	{
		AvgPEPricesPojo avgPrices_Pojo = null;
		if (ScDescBeginsWith != null && scEXSrv != null)
		{
			this.scRoot = scEXSrv.Get_ScripExisting_DB_DescSW(ScDescBeginsWith);
			if (scRoot != null)
			{
				avgPrices_Pojo = getAvgPricesforScRoot(scRoot);
			}
		}

		return avgPrices_Pojo;
	}

	@Override
	public AvgPEPricesPojo getAvgPricesforScRoot(EN_SC_General scRoot) throws EX_General
	{
		AvgPEPricesPojo avgPrices_Pojo = new AvgPEPricesPojo();
		if (scRoot != null)
		{
			if (this.scRoot == null)
			{
				this.scRoot = scRoot;
			}

			// 1. Get data for Scrip
			try
			{
				ArrayList<EN_SC_Trends> trendsEntList = scRoot.getRelatedEntities(relTrends);
				if (trendsEntList != null)
				{
					if (trendsEntList.size() > 0)
					{
						avgPrices_Pojo.setScCode(scRoot.getSCCode());
						avgPrices_Pojo.setEPS(scRoot.getEPS());
						avgPrices_Pojo.setCMP(scRoot.getCMP());
						avgPrices_Pojo.setPECurr(scRoot.getCurrPE());

						try
						{
							// 3 Yrs PE Data
							EN_SC_Trends trend3YrEnt = trendsEntList.stream().filter(x -> x.getPeriod().equals(period3Yr)).findFirst().get();
							if (trend3YrEnt != null)
							{
								avgPrices_Pojo.setPE3Yr(trend3YrEnt.getAvgPE());
								avgPrices_Pojo.setPrice3Yr((trend3YrEnt.getAvgPE() * scRoot.getEPS()));
								double delta = ((scRoot.getCMP() - avgPrices_Pojo.getPrice3Yr()) / scRoot.getCMP()) * 100;
								avgPrices_Pojo.setPrDelta3Yr(delta);

							}

							// 5 Yrs PE Data
							EN_SC_Trends trend5YrEnt = trendsEntList.stream().filter(x -> x.getPeriod().equals(period5Yr)).findFirst().get();
							if (trend5YrEnt != null)
							{
								avgPrices_Pojo.setPE5Yr(trend5YrEnt.getAvgPE());
								avgPrices_Pojo.setPrice5Yr((trend5YrEnt.getAvgPE() * scRoot.getEPS()));
								double delta = ((scRoot.getCMP() - avgPrices_Pojo.getPrice5Yr()) / scRoot.getCMP()) * 100;
								avgPrices_Pojo.setPrDelta5Yr(delta);

							}

						}
						catch (NoSuchElementException e)
						{
							// Do Nothing - just avoid exception
						}

					}
				}
			}
			catch (EX_InvalidRelationException e)
			{
				EX_General ex = new EX_General(new Object[]
				{ e.getMessage()
				});
				throw ex;
			}

		}

		return avgPrices_Pojo;
	}

}

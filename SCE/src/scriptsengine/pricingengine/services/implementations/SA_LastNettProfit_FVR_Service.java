package scriptsengine.pricingengine.services.implementations;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import scriptsengine.constants.SheetNamesConstants;
import scriptsengine.pojos.OB_Scrip_BalSheet;
import scriptsengine.pricingengine.definitions.TY_Last_NP_FVR;
import scriptsengine.pricingengine.services.interfaces.ISA_LastNettProfit_FVR_Service;
import scriptsengine.uploadengine.definitions.ScripDataContainer;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IScripDataContainerSrv;
import scriptsengine.utilities.types.PenultimateQYear;

/**
 * Last Nett Profit Getter and Face Value(s) Ratio Calculator Service for a Scrip
 *
 */
@Service
public class SA_LastNettProfit_FVR_Service implements ISA_LastNettProfit_FVR_Service, ApplicationContextAware
{

	private ApplicationContext		appCtxt;
	private IScripDataContainerSrv	scDataConSrv;

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{

		this.appCtxt = ctxt;
	}

	@SuppressWarnings(
	{ "rawtypes", "unchecked"
	})
	@Override
	public TY_Last_NP_FVR getLastNettProfit_FVRforScripCode(String scCode) throws EX_General
	{
		TY_Last_NP_FVR lastNP_FVR = new TY_Last_NP_FVR();
		OB_Scrip_BalSheet spYearBalSheetData;

		PenultimateQYear qyearTo_beforeScan = new PenultimateQYear();
		if (scCode != null)
		{
			if (appCtxt != null)
			{
				if (scDataConSrv == null)
				{
					scDataConSrv = appCtxt.getBean(IScripDataContainerSrv.class);

				}
				if (scDataConSrv != null)
				{
					ScripDataContainer scDataCon = scDataConSrv.getScripDetailsfromDB(scCode);
					if (scDataCon != null)
					{
						SheetEntities SheetEntities = scDataCon.getEntitiesforSheet(SheetNamesConstants.BalanceSheet);
						if (SheetEntities != null)
						{
							ArrayList<OB_Scrip_BalSheet> balSheetEntities = SheetEntities.getSheetEntityList();
							if (balSheetEntities != null)
							{
								spYearBalSheetData = evaluate_PenultimateYear(qyearTo_beforeScan, balSheetEntities);
								if (spYearBalSheetData != null)
								{

									lastNP_FVR.setLastNettProfit(spYearBalSheetData.getNettProfit());

									if (scDataCon.getScRoot() != null)
									{
										if (scDataCon.getScRoot().getfaceValue() != 0)
										{
											lastNP_FVR.setFVR(spYearBalSheetData.getFaceValue() / scDataCon.getScRoot().getfaceValue());
										}
									}

								}
							}
						}
						if (scDataCon.getScRoot() != null)
						{
							lastNP_FVR.setCurrFV(scDataCon.getScRoot().getfaceValue());
						}
					}
				}

			}
		}
		return lastNP_FVR;
	}

	private OB_Scrip_BalSheet evaluate_PenultimateYear(PenultimateQYear pQyear, ArrayList<OB_Scrip_BalSheet> balSheetEntList)
	{

		OB_Scrip_BalSheet spYearBalSheet = null;

		if (pQyear.getYear() != 0 && balSheetEntList != null)
		{
			// First check if Balance Sheet Data exists for the To Year (e.g Last Year balanceSheet data is not yet
			// maintained and current month is April as annual results for last year are not yet consolidated and out

			try
			{
				spYearBalSheet = balSheetEntList.stream().filter(x -> x.getyear() == pQyear.getYear()).findFirst().get();

			}

			catch (NoSuchElementException e)
			{
				// Data Not found for the Target Year can be case of April this year where last year BalanceSheet
				// Data is not yet consolidated
				// Reduce the year by 1 in this case

				try
				{
					spYearBalSheet = balSheetEntList.stream().filter(x -> x.getyear() == pQyear.getYear() - 1).findFirst().get();
				}

				catch (NoSuchElementException e1)
				{

					// Data not Maintained even for the penultimate year can be coz the ba;lance sheet is not yet
					// consolidated for last year
					// Reduce year by 2 in this case
					// No try in this case possible
					spYearBalSheet = balSheetEntList.stream().filter(x -> x.getyear() == pQyear.getYear() - 2).findFirst().get();

				}

			}

		}
		return spYearBalSheet;

	}

}

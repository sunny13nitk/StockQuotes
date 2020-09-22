package scriptsengine.pricingengine.services.implementations;

import java.util.ArrayList;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import scriptsengine.constants.SheetNamesConstants;
import scriptsengine.pojos.OB_Scrip_BalSheet;
import scriptsengine.pricingengine.definitions.TY_ENPR;
import scriptsengine.pricingengine.services.interfaces.ISA_ENPRService;
import scriptsengine.uploadengine.definitions.ScripDataContainer;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IScripDataContainerSrv;

/**
 * 
 * ENPR service with Context Scope
 * This service will help to give Avg. ENPR Ratio for a given Scrip
 * Utilizes several helper Services
 */
@Service
public class SA_ENPRService implements ISA_ENPRService, ApplicationContextAware
{

	private ApplicationContext		appCtxt;

	private IScripDataContainerSrv	scDataConSrv;

	/**
	 * Get the Average ENPR for a Scrip
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings(
	{ "unchecked", "rawtypes"
	})
	@Override
	public double getAvgENPRforScripCode(String scCode) throws EX_General
	{
		double avgENPR = 0;
		ArrayList<TY_ENPR> ENPRList = new ArrayList<TY_ENPR>();

		if (scCode != null && appCtxt != null)
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
						ArrayList<OB_Scrip_BalSheet> balShEntities = SheetEntities.getSheetEntityList();
						if (balShEntities != null)
						{
							if (balShEntities.size() > 0)
							{
								for ( OB_Scrip_BalSheet ob_Scrip_BalSheet : balShEntities )
								{
									if (ob_Scrip_BalSheet.getFaceValue() > 0 && ob_Scrip_BalSheet.getEPS() != 0
									          && ob_Scrip_BalSheet.getNettProfit() != 0)
									{
										double epsUnitvalue = ob_Scrip_BalSheet.getEPS() / ob_Scrip_BalSheet.getFaceValue();
										double enpr = (epsUnitvalue / ob_Scrip_BalSheet.getNettProfit()) * 100;

										TY_ENPR objENPR = new TY_ENPR(ob_Scrip_BalSheet.getyear(), epsUnitvalue, enpr);
										ENPRList.add(objENPR);
									}
								}
								// Get the average ENPR
								avgENPR = ENPRList.stream().mapToDouble(TY_ENPR::getENPR).average().getAsDouble();
							}
						}
					}
				}
			}

		}

		return avgENPR;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		this.appCtxt = ctxt;

	}

}

package scriptsengine.statistics.implementations.keyFigGenerators;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import scriptsengine.constants.SheetNamesConstants;
import scriptsengine.pojos.OB_Scrip_BalSheet;
import scriptsengine.statistics.definitions.TY_KeyFigure;
import scriptsengine.statistics.interfaces.IKeyFiguresGenerator;
import scriptsengine.uploadengine.definitions.ScripDataContainer;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;

@Service("KFG_CapexBwRatio_Srv")
/**
 * 
 * Capex = (Nett Block + CWIP + Investments)/ Borrowings = (Secured Loans + Unsecured Loans) Ratio
 *
 */
public class KFG_CapexBwRatio_Srv implements IKeyFiguresGenerator
{

	@SuppressWarnings(
	{ "rawtypes", "unchecked"
	})
	@Override
	public ArrayList<TY_KeyFigure> generateKeyFigures(ScripDataContainer scDataCon) throws EX_General
	{
		ArrayList<TY_KeyFigure> keyFigs = new ArrayList<TY_KeyFigure>();

		if (scDataCon != null)
		{
			SheetEntities depObjList = scDataCon.getEntitiesforSheet(SheetNamesConstants.BalanceSheet);
			if (depObjList != null)
			{
				ArrayList<OB_Scrip_BalSheet> balSheetEntList = depObjList.getSheetEntityList();
				if (balSheetEntList != null)
				{
					if (balSheetEntList.size() > 0)
					{
						for ( OB_Scrip_BalSheet ob_Scrip_BalSheet : balSheetEntList )
						{
							TY_KeyFigure keyFig = null;

							if (ob_Scrip_BalSheet.getsecuredLoans() + ob_Scrip_BalSheet.getunsecuredLoans() != 0)
							{
								keyFig = new TY_KeyFigure(ob_Scrip_BalSheet.getyear(),
								          ((ob_Scrip_BalSheet.getNettBlock() + ob_Scrip_BalSheet.getCWIP()
								                    + ob_Scrip_BalSheet.getInvestments())
								                    / (ob_Scrip_BalSheet.getsecuredLoans() + ob_Scrip_BalSheet.getunsecuredLoans())));
							}
							else
							{
								keyFig = new TY_KeyFigure(ob_Scrip_BalSheet.getyear(), 0);
							}

							keyFigs.add(keyFig);
						}
					}
				}
			}
		}

		return keyFigs;
	}

}

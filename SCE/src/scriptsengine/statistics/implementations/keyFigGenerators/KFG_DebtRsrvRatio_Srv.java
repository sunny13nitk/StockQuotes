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

/**
 * 
 * Ratio of Total Debt(Secured = Unsecured Loans)/Reserves for a Scrip
 */
@Service("KFG_DebtRsrvRatio_Srv")
public class KFG_DebtRsrvRatio_Srv implements IKeyFiguresGenerator
{

	@SuppressWarnings(
	{ "unchecked", "rawtypes"
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
							TY_KeyFigure keyFig = new TY_KeyFigure(ob_Scrip_BalSheet.getyear(),
							          ((ob_Scrip_BalSheet.getsecuredLoans() + ob_Scrip_BalSheet.getunsecuredLoans())
							                    / ob_Scrip_BalSheet.getReserves()));

							keyFigs.add(keyFig);
						}
					}
				}
			}
		}

		return keyFigs;
	}

}

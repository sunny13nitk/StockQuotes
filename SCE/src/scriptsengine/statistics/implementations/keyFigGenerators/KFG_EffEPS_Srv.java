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
 * Ratio of Effective EPS over years i.e Earnings/Face Value of share in a particular Year
 */
@Service("KFG_EffEPS_Srv")
public class KFG_EffEPS_Srv implements IKeyFiguresGenerator
{

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
							          (ob_Scrip_BalSheet.getEPS() / ob_Scrip_BalSheet.getFaceValue()));

							keyFigs.add(keyFig);
						}
					}
				}
			}
		}

		return keyFigs;
	}

}

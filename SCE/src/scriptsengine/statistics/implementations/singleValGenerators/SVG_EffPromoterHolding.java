package scriptsengine.statistics.implementations.singleValGenerators;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import scriptsengine.constants.SheetNamesConstants;
import scriptsengine.pojos.OB_Scrip_Shareholding;
import scriptsengine.statistics.definitions.TY_AttribSingleVal;
import scriptsengine.statistics.interfaces.ISingleValGenerator;
import scriptsengine.uploadengine.definitions.ScripDataContainer;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;

@Service("SVG_EffPromoterHolding")
/**
 * Single Value Generator Service for Efective Promoter Holding - considering pledged holding
 *
 */
public class SVG_EffPromoterHolding implements ISingleValGenerator
{

	@SuppressWarnings(
	{ "unchecked", "rawtypes"
	})
	@Override
	public TY_AttribSingleVal generateSinglValueAttribute(ScripDataContainer scDataCon) throws EX_General
	{
		TY_AttribSingleVal effPromHolding = new TY_AttribSingleVal();

		if (scDataCon != null)
		{
			SheetEntities depObjList = scDataCon.getEntitiesforSheet(SheetNamesConstants.ShareHoldingSheet);
			if (depObjList != null)
			{
				ArrayList<OB_Scrip_Shareholding> sHSheetEntList = depObjList.getSheetEntityList();
				if (sHSheetEntList != null)
				{
					if (sHSheetEntList.size() > 0)
					{
						effPromHolding.setAttrType(Double.class.getSimpleName());
						OB_Scrip_Shareholding sHEntity = sHSheetEntList.get(0);
						if (sHEntity != null)
						{
							effPromHolding.setValue(sHEntity.getpromoter() - sHEntity.getpledged());
						}
					}
				}
			}
		}
		return effPromHolding;
	}

}

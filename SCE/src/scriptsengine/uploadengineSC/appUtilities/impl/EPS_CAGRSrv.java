package scriptsengine.uploadengineSC.appUtilities.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.exceptions.EX_InvalidRelationException;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengineSC.Metadata.services.implementations.SCWBMetadataSrv;
import scriptsengine.uploadengineSC.appUtilities.interfaces.IEPS_CAGRSrv;
import scriptsengine.uploadengineSC.entities.EN_SC_BalSheet;
import scriptsengine.uploadengineSC.entities.EN_SC_General;
import scriptsengine.uploadengineSC.scripSheetServices.definitions.SheetsNames;
import scriptsengine.utilities.implementations.CAGRCalcSrv;

@Service("EPS_CAGRSrv")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EPS_CAGRSrv implements IEPS_CAGRSrv
{

	@Autowired
	private SCWBMetadataSrv wbMdtSrv;

	@Override
	public double getEPSCAGR(EN_SC_General scRoot, int Duration) throws EX_General
	{
		double epsCAGR = 0;

		if (scRoot != null && wbMdtSrv != null)
		{
			String relName = wbMdtSrv.getRelationNameforSheetName(SheetsNames.sheet_Analysis);
			if (relName != null)
			{
				try
				{
					ArrayList<EN_SC_BalSheet> balSheetEntList = scRoot.getRelatedEntities(relName);
					if (balSheetEntList != null)
					{
						int size = balSheetEntList.size();
						int entToPickS = 0;
						int entToPickE = 0;
						if (size < Duration)
						{
							entToPickS = 0;
							entToPickE = size - 1; // Array Index begins with 0 - Last Entity
						}
						else
						{
							entToPickS = size - Duration;
							entToPickE = size - 1; // Array Index begins with 0 - Last Entity

						}
						double epsS = balSheetEntList.get(entToPickS).getEPS();
						double epsE = scRoot.getEPS();

						epsCAGR = CAGRCalcSrv.getCAGR(epsS, epsE, Duration);

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
		}

		return epsCAGR;
	}

}

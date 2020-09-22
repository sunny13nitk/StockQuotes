package mypackage.Executions;

import java.util.ArrayList;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import mypackage.Interfaces.IExecutable;
import scriptsengine.uploadengineSC.entities.EN_SC_BalSheet;
import scriptsengine.uploadengineSC.scContainer.definitions.SCScripDataContainer;
import scriptsengine.uploadengineSC.scContainer.interfaces.ISC_Scrip_DataCont_Srv;

@Service("SCDataContSrvExe")
public class SCDataContSrvExe implements IExecutable
{

	@SuppressWarnings("unchecked")
	@Override
	public void execute(ApplicationContext appctxt) throws Exception
	{
		if (appctxt != null)
		{
			String ScCode = "AVANTI";

			ISC_Scrip_DataCont_Srv scDataConSrv = appctxt.getBean(ISC_Scrip_DataCont_Srv.class);
			if (scDataConSrv != null)
			{
				SCScripDataContainer scDataCon = scDataConSrv.get_ScripData(ScCode);
				if (scDataCon != null)
				{
					System.out.println("Data REtrieved for - " + scDataCon.getScRoot().getSCName());

					ArrayList<EN_SC_BalSheet> balsheetENList = scDataCon.getEntitiesforSheet("Analysis").getSheetEntityList();
					if (balsheetENList != null)
					{
						System.out.println(balsheetENList.size());
						for ( EN_SC_BalSheet en_SC_BalSheet : balsheetENList )
						{
							System.out.println(en_SC_BalSheet.getYear());

						}

					}

					// balsheetEnt = scDataCon.getEntitiesforSheet("Analysis");

				}

				// Try Again to check cache
				SCScripDataContainer scDataCon2 = scDataConSrv.get_ScripData(ScCode);
				if (scDataCon2 != null)
				{
					System.out.println("Data REtrieved for - " + scDataCon2.getScRoot().getSCName());
				}

			}

		}

	}

}

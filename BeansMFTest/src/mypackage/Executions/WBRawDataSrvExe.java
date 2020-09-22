package mypackage.Executions;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import mypackage.Interfaces.IExecutable;
import scriptsengine.uploadengineSC.tools.definitions.SheetFldValsHeadersList;
import scriptsengine.uploadengineSC.tools.definitions.TY_SingleCard_SheetRawData;
import scriptsengine.uploadengineSC.tools.definitions.TY_WBRawData;
import scriptsengine.uploadengineSC.tools.interfaces.IWBRawDataSrv;

@Service("WBRawDataSrvExe")
public class WBRawDataSrvExe implements IExecutable
{

	@Override
	public void execute(ApplicationContext appctxt) throws Exception
	{
		if (appctxt != null)
		{
			String filePath = "C://WBConfig//My PF//Capacit'e Infra.xlsx";

			IWBRawDataSrv wbRawDatSrv = appctxt.getBean(IWBRawDataSrv.class);
			if (wbRawDatSrv != null)
			{
				TY_WBRawData wbRawData = wbRawDatSrv.getSheetFldVals(filePath);
				if (wbRawData != null)
				{
					for ( SheetFldValsHeadersList collSheet : wbRawData.getCollSheetsRawData() )
					{
						System.out.println(collSheet.getSheetName());
					}

					for ( TY_SingleCard_SheetRawData nonCollSheet : wbRawData.getNonCollSheetsRawData() )
					{
						System.out.println(nonCollSheet.getSheetName());
					}
				}
			}

		}

	}

}

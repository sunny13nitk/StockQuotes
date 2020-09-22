package mypackage.Executions;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import mypackage.Interfaces.IExecutable;
import scriptsengine.uploadengineSC.tools.definitions.TY_WBRawData;
import scriptsengine.uploadengineSC.tools.interfaces.IWBRawDataSrv;

@Service("SheetRawDataSrvExe")
public class SheetRawDataSrvExe implements IExecutable
{

	@Override
	public void execute(ApplicationContext appctxt) throws Exception
	{
		if (appctxt != null)
		{
			String filePath = "C://WBConfig//My PF//Sunteck Realty.xlsx";
			String sheetName = "Analysis";

			IWBRawDataSrv wbRawDatSrv = appctxt.getBean(IWBRawDataSrv.class);
			if (wbRawDatSrv != null)
			{
				TY_WBRawData shRawData = wbRawDatSrv.getSheetFldVals(filePath, sheetName);
				if (shRawData != null)
				{

					System.out.println(shRawData.getCollSheetsRawData().get(0).getSheetName());

				}
			}

		}

	}

}

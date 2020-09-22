package mypackage.Executions;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import mypackage.Interfaces.IExecutable;
import scriptsengine.uploadengineSC.scripSheetServices.definitions.SC_XLS_CrUpd_Mode;
import scriptsengine.uploadengineSC.scripSheetServices.interfaces.ISCDetCrUpd_Mode;

@Service("WBDetCrUpd_ModeSrvExe")
public class WBDetCrUpd_ModeSrvExe implements IExecutable
{

	@Override
	public void execute(ApplicationContext appctxt) throws Exception
	{
		if (appctxt != null)
		{
			String filePath = "C://WBConfig//Avanti Feeds.xlsx";

			ISCDetCrUpd_Mode CrUpdModSrv = appctxt.getBean(ISCDetCrUpd_Mode.class);
			if (CrUpdModSrv != null)
			{
				SC_XLS_CrUpd_Mode scMode = CrUpdModSrv.getModeforFilePath(filePath);
				if (scMode != null)
				{
					System.out.println(scMode.getSCCode());
					System.out.println(scMode.getMode().toString());
				}
			}

		}

	}

}

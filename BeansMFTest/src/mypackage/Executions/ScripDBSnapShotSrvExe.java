package mypackage.Executions;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import mypackage.Interfaces.IExecutable;
import scriptsengine.uploadengineSC.scripSheetServices.definitions.SCSheetValStamps;
import scriptsengine.uploadengineSC.scripSheetServices.definitions.SheetValStamp;
import scriptsengine.uploadengineSC.scripSheetServices.interfaces.IScripDBSnapShotSrv;

@Service("ScripDBSnapShotSrvExe")
public class ScripDBSnapShotSrvExe implements IExecutable
{

	@Override
	public void execute(ApplicationContext appctxt) throws Exception
	{
		if (appctxt != null)
		{
			String scDesc = "Avanti";

			IScripDBSnapShotSrv scDBSSSrv = appctxt.getBean(IScripDBSnapShotSrv.class);
			if (scDBSSSrv != null)
			{
				SCSheetValStamps SHValStamps = scDBSSSrv.getScripLatestValsbyScDesc(scDesc);
				if (SHValStamps != null)
				{
					System.out.println(SHValStamps.getScCode());
					for ( SheetValStamp shValStamp : SHValStamps.getSheetVals() )
					{
						System.out.println(shValStamp.getSheetName());
						System.out.println(shValStamp.getValue());
					}
				}
			}

		}

	}

}

package mypackage.Executions;

import java.util.ArrayList;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import mypackage.Interfaces.IExecutable;
import scriptsengine.uploadengineSC.scripSheetServices.definitions.SCSheetValStamps;
import scriptsengine.uploadengineSC.scripSheetServices.definitions.SheetValStamp;
import scriptsengine.uploadengineSC.scripSheetServices.interfaces.IScripGDBSnapShotSrv;

@Service("ScripGDBSnapShotSrvExe")
public class ScripGDBSnapShotSrvExe implements IExecutable
{

	@Override
	public void execute(ApplicationContext appctxt) throws Exception
	{
		if (appctxt != null)
		{

			IScripGDBSnapShotSrv scDBSSSrv = appctxt.getBean(IScripGDBSnapShotSrv.class);
			if (scDBSSSrv != null)
			{
				ArrayList<SCSheetValStamps> SHValStamps = scDBSSSrv.getScripsDBSnapShot();
				if (SHValStamps != null)
				{

					for ( SCSheetValStamps scSheetValStamps : SHValStamps )
					{

						System.out.println();
						System.out.print(scSheetValStamps.getScCode() + "->   ");
						for ( SheetValStamp shValStamp : scSheetValStamps.getSheetVals() )
						{
							System.out.print(" " + shValStamp.getSheetName() + " - " + shValStamp.getValue() + "  ||");

						}
					}
				}
			}

		}

	}

}

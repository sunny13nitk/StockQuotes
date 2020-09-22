package root.scripsEngine.uploadEngine.scripSheetServices.interfaces;

import java.util.ArrayList;

import root.scripsEngine.uploadEngine.exceptions.EX_General;
import root.scripsEngine.uploadEngine.scripSheetServices.definitions.SCSheetValStamps;

public interface IScripGDBSnapShotSrv
{
	public ArrayList<SCSheetValStamps> getScripsDBSnapShot(
	) throws EX_General;
	
}

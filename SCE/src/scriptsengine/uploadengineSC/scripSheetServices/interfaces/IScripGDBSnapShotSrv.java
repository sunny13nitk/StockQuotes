package scriptsengine.uploadengineSC.scripSheetServices.interfaces;

import java.util.ArrayList;

import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengineSC.scripSheetServices.definitions.SCSheetValStamps;

public interface IScripGDBSnapShotSrv
{
	public ArrayList<SCSheetValStamps> getScripsDBSnapShot() throws EX_General;

}

package scriptsengine.uploadengineSC.scripSheetServices.interfaces;

import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengineSC.entities.EN_SC_General;
import scriptsengine.uploadengineSC.scripSheetServices.definitions.SCSheetValStamps;

public interface IScripDBSnapShotSrv
{
	public SCSheetValStamps getScripLatestValsbyScCode(String ScCode) throws EX_General;

	public SCSheetValStamps getScripLatestValsbyScCode(EN_SC_General scRoot) throws EX_General;

	public SCSheetValStamps getScripLatestValsbyScDesc(String ScDescBeginsWith) throws EX_General;
}

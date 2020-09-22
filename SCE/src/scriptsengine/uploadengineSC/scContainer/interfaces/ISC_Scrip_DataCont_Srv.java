package scriptsengine.uploadengineSC.scContainer.interfaces;

import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengineSC.scContainer.definitions.SCScripDataContainer;

public interface ISC_Scrip_DataCont_Srv
{
	public SCScripDataContainer get_ScripData(String SCCode) throws EX_General;
}

package scriptsengine.uploadengine.services.interfaces;

import scriptsengine.uploadengine.definitions.ScripDataContainer;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Scrip Data Container Service - Interface
 */
public interface IScripDataContainerSrv
{
	public ScripDataContainer getScripDetailsfromDB(String scCode) throws EX_General;

	public void clearCache();

}

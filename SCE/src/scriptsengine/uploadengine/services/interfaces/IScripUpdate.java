package scriptsengine.uploadengine.services.interfaces;

import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * Scrip Update Specific Interface - Methods
 *
 */
public interface IScripUpdate
{
	// Create Scrip
	public void updateScrip() throws EX_General;

	public IScripBaseSrv getBaseService();

	public void setBaseService(IScripBaseSrv baseSrv);
}

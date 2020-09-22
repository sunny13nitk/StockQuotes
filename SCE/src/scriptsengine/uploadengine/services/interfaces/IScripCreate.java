package scriptsengine.uploadengine.services.interfaces;

import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * Scrip Creation Interface - Methods Specific to Creation of Scrip
 *
 */
public interface IScripCreate
{
	// Create Scrip
	public void createScrip() throws EX_General;

	public IScripBaseSrv getBaseService();

	public void setBaseService(IScripBaseSrv baseSrv);

}

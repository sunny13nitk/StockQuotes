package scriptsengine.uploadengine.updateIdentifiers.services.interfaces;

import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Interface for Scrip Mass Update Service
 */
public interface IScripMassUpdateSrv
{
	public void generateTemplatesforScripsPendingUpdate(String filepath) throws EX_General;

	public void updateScripsfromFilePath(String folder) throws EX_General;
}

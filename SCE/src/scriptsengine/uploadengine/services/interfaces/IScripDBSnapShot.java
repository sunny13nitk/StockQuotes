package scriptsengine.uploadengine.services.interfaces;

import scriptsengine.uploadengine.definitions.ScripDBSnaphot;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * Scrip DB SnapShot Service Interface
 *
 */
public interface IScripDBSnapShot
{
	/**
	 * Get the Scrip DB Snapshot for the Scrip Code
	 * 
	 * @param scCode
	 *             - Scrip Code
	 * @return - Scrip DB Snapshot Object
	 */
	public ScripDBSnaphot getScripDBSnapshot(String scCode) throws EX_General;

	public ScripDBSnaphot getScDBsnapshot();
}

package scriptsengine.uploadengineSC.scripSheetServices.interfaces;

import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengineSC.entities.EN_SC_General;

/**
 * 
 * Scrip exists in DB Service
 */
public interface ISCExistsDB_Srv
{
	public boolean Is_ScripExisting_DB(String scCode) throws EX_General;

	public EN_SC_General Get_ScripExisting_DB(String scCode) throws EX_General;

	public boolean Is_ScripExisting_DB_DescSW(String scDesc) throws EX_General;

	public EN_SC_General Get_ScripExisting_DB_DescSW(String scDesc) throws EX_General;

}

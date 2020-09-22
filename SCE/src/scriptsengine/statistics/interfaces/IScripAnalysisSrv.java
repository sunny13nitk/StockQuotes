package scriptsengine.statistics.interfaces;

import java.util.ArrayList;

import scriptsengine.reportsengine.definitions.Ty_SectorAvgStats;
import scriptsengine.reportsengine.definitions.Ty_SectorCount;
import scriptsengine.reportsengine.repDS.definitions.TY_ScRoot_AttrContainers;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.valuehelpers.definitions.TY_StringKeyDesc;

/**
 * 
 * Scrip analysis Service Interface
 */
public interface IScripAnalysisSrv
{

	public void populateDataforAllScrips() throws EX_General;

	public void populateDataforSector(String Sector) throws EX_General;

	public void populateDataforSectors(ArrayList<String> Sectors) throws EX_General;

	public ArrayList<TY_ScRoot_AttrContainers> getScRootAttContainersList();

	public ArrayList<TY_StringKeyDesc> getScCodes();

	public ArrayList<String> getScSectors();

	public ArrayList<Ty_SectorAvgStats> getSectorsAvgStats();

	public ArrayList<Ty_SectorCount> getSectorsCount();

}

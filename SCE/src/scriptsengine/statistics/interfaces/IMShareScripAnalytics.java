package scriptsengine.statistics.interfaces;

import java.util.ArrayList;

import scriptsengine.reportsengine.repDS.definitions.TY_ScRoot_AttrContainers;
import scriptsengine.statistics.definitions.TY_Sector_AttrContainers;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * Market Share Scrips Analytics Specific Interface
 *
 */
public interface IMShareScripAnalytics
{
	/**
	 * ----------------------------------------------------------------------------------------------------------------
	 * ---
	 * Get Sector specific Market Share Analytics Enabled Attributes Containers (1...N) s.that N = ScCode Key Figures
	 * List for each of the Attributes
	 * 
	 * @param scRootContainersList
	 *             - Sector Specific Attribute Container List to be provide as import Parameter
	 * @return - Attribute specific - Scrip Code Key Figures containers
	 * @throws EX_General
	 *              ---------------------------------------------------------------------------------------------------
	 * 
	 */
	public TY_Sector_AttrContainers generateMarketShareAnalytics(ArrayList<TY_ScRoot_AttrContainers> scRootContainersList) throws EX_General;

	public ArrayList<String> getScCodes();

	public int getMinYear();

	public int getMaxYear();

}

package scriptsengine.reportsengine.repDS.interfaces;

import scriptsengine.reportsengine.repDS.definitions.TY_ScRoot_AttrContainers;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * Interface for REport Data Source that would always return an ArrayList of TY_Attr_Container depending upon the fields
 * defined and bound using annotation @ReportRefFieldName in the corresponding implementations. Generation of Attribute
 * Containers will be centrally handled using aspects with Scrip Code going in as Input
 *
 */
public interface IReportDataSource
{
	/**
	 * Generate Report Data Source for Scrip Code
	 * 
	 * @param scCode
	 *             - Scrip Code
	 * @return - Array List of Attributes Data Containers mapped in implementaion with annotation @ReportRefFieldName
	 * @throws EX_General
	 */
	public TY_ScRoot_AttrContainers generateReportDataSourceforScripCode(String scCode) throws EX_General;
}

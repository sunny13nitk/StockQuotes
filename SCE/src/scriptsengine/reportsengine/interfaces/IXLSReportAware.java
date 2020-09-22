package scriptsengine.reportsengine.interfaces;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Excel Report generation Aware Interface to be implmeneted by services that would have Excel D/L enabled
 */
public interface IXLSReportAware
{
	/**
	 * Generate XLS Report - No definition Needed will be handled via an Aspect
	 * 
	 * @param filepath
	 *             - filepath where xls report will be generated
	 * @throws EX_General
	 */
	public void generateXLSReport(String filepath) throws EX_General;

	/**
	 * Generate XLS Report in a new sheet in specified Workbook Context - No definition Needed will be handled via an
	 * Aspect
	 * 
	 * @param wbCtxt
	 *             - Specified Work Book Context where XLS report is to be generated
	 */
	public void generateXLSReport(XSSFWorkbook wbCtxt) throws EX_General;

}

package scriptsengine.uploadengineSC.scripSheetServices.interfaces;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengineSC.scripSheetServices.definitions.SC_XLS_CrUpd_Mode;

/*
 * Determine Create Update Mode for an Scrip Workbook
 */
public interface ISCDetCrUpd_Mode
{

	public SC_XLS_CrUpd_Mode getModeforWB(XSSFWorkbook wbCtxt) throws EX_General;

	public SC_XLS_CrUpd_Mode getModeforFilePath(String filePath) throws EX_General;

}

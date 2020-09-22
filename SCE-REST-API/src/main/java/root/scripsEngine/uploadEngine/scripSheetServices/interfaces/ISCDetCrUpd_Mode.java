package root.scripsEngine.uploadEngine.scripSheetServices.interfaces;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import root.scripsEngine.uploadEngine.exceptions.EX_General;
import root.scripsEngine.uploadEngine.scripSheetServices.definitions.SC_XLS_CrUpd_Mode;

/*
 * Determine Create Update Mode for an Scrip Workbook
 */
public interface ISCDetCrUpd_Mode
{
	
	public SC_XLS_CrUpd_Mode getModeforWB(
	        XSSFWorkbook wbCtxt
	) throws EX_General;
	
	public SC_XLS_CrUpd_Mode getModeforFilePath(
	        String filePath
	) throws EX_General;
	
}

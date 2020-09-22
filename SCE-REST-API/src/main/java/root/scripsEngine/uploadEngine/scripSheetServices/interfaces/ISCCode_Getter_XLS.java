package root.scripsEngine.uploadEngine.scripSheetServices.interfaces;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import root.scripsEngine.uploadEngine.exceptions.EX_General;

/**
 * 
 * Interface to get SC Code from XLS Sheet
 */
public interface ISCCode_Getter_XLS
{
	
	public String getSCCode(
	        String FilePath
	) throws EX_General;
	
	public String getSCCode(
	        XSSFWorkbook wbCtxt
	) throws EX_General;
	
}

package scriptsengine.uploadengine.validations.interfaces;

import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IScripSheetMetadata;

/**
 * WorkBook Validator Interface - Generic
 * Filepath to come from Filepath validation Service
 */
public interface IWBValidator
{
	public void validateWB(XSSFWorkbook wb, String filepath) throws EX_General;

	public ArrayList<SheetEntities> getWbEntities();

	public String getXsheetvalidatorname();

	// Validate Across Sheets for Entities
	public void validateXsheets();

	public IScripSheetMetadata getScMdtSrv();

	public String getScripCode();
}

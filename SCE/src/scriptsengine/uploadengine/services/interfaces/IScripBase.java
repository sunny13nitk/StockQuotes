package scriptsengine.uploadengine.services.interfaces;

import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import scriptsengine.enums.SCEenums;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * Scrip Base Interface
 * to be implmented in a Proxy scoped BEan
 */
public interface IScripBase
{
	// Set Work Book context in Scrip Create Service
	public void setWBcontext(String Filepath) throws IOException;

	// Validate Workbook for Creation
	public void validateWB() throws EX_General;

	// Get WB context
	public XSSFWorkbook getWbContext();

	// Set Mode for Base Service
	// Possible Values - Create / Update
	public void setMode(SCEenums.ModeOperation mode);
}

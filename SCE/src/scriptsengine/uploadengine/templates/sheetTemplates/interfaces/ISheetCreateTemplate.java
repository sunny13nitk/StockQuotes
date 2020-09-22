package scriptsengine.uploadengine.templates.sheetTemplates.interfaces;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import scriptsengine.uploadengine.JAXB.definitions.SheetMetadata;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Interface for Sheet Creation Template
 */
public interface ISheetCreateTemplate
{
	public void createSheet(XSSFWorkbook wbCtxt, SheetMetadata shMdt) throws EX_General;
}

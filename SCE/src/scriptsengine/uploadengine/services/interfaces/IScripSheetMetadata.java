package scriptsengine.uploadengine.services.interfaces;

import java.util.ArrayList;

import scriptsengine.uploadengine.JAXB.definitions.SheetMetadata;
import scriptsengine.uploadengine.definitions.SheetSplitAttrs;

/**
 * 
 * Scrips Sheet Metadata Service Interface
 */
public interface IScripSheetMetadata
{

	public String getBaseSheetName();

	public String getBaseSheetKeyField();

	public String getBaseSheetKeyObjField();

	public SheetMetadata getSheetMdtbySheetName(String sheetName);

	public ArrayList<String> getMandatorySheets();

	public String getRelationNameforSheetName(String sheetName);

	public String getTableNameforSheetName(String sheetName);

	public ArrayList<String> getKeyfieldsforSheet(String sheetName);

	public ArrayList<SheetSplitAttrs> getSplitAwareSheetAttrs();

	public ArrayList<String> getIntKeyfieldsforSheet(String sheetName);

	public String getRootObjectName();

	public String getUpdateReqdProcessBeanforSheetName(String sheetName);
}

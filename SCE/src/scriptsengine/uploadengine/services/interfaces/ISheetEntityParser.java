package scriptsengine.uploadengine.services.interfaces;

import java.lang.reflect.InvocationTargetException;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * Interface for Sheet Entities Parser
 * Parses the Sheet(s) of Workbook Into respective Pojos and returns the entities list
 * 
 * @param <T>
 */
public interface ISheetEntityParser<T>
{
	public SheetEntities getEntitiesfromSheet(XSSFSheet sheet)
	          throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, EX_General;

}

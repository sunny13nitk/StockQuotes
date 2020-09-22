package scriptsengine.uploadengine.services.interfaces;

import java.lang.reflect.InvocationTargetException;

import scriptsengine.pojos.OB_Scrip_General;
import scriptsengine.uploadengine.definitions.SheetEntFilter;
import scriptsengine.uploadengine.definitions.SheetEntities;

/**
 * entities update procesor - Interface
 *
 */
public interface IDBQueryFilter
{
	public SheetEntFilter getEntFilterforSheetEntities(OB_Scrip_General scRoot, SheetEntities shEntities)
	          throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;

}

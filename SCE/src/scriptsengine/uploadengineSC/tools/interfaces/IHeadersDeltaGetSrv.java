package scriptsengine.uploadengineSC.tools.interfaces;

import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengineSC.Metadata.definitions.SCSheetMetadata;

public interface IHeadersDeltaGetSrv
{
	public <T> ArrayList<T> getHeadersDelta(XSSFSheet sheetRef, ArrayList<T> sheetEntityList, SCSheetMetadata shtMdt) throws EX_General;
}

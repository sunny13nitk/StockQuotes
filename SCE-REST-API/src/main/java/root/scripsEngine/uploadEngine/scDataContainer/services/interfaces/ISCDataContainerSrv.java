package root.scripsEngine.uploadEngine.scDataContainer.services.interfaces;

import java.lang.reflect.Method;
import java.util.ArrayList;

import root.scripsEngine.uploadEngine.exceptions.EX_General;
import root.scripsEngine.uploadEngine.scDataContainer.types.scDataContainer;

public interface ISCDataContainerSrv
{
	
	public void load(
	        String scCode
	) throws EX_General, Exception;
	
	public <T> ArrayList<T> load(
	        String scCode, String bobjName
	);
	
	public scDataContainer getScDC(
	);
	
	public Method getMethodfromSCDataContainer(
	        String sheetName, char Type
	);
}

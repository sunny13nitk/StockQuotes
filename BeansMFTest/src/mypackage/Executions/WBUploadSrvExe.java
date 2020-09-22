package mypackage.Executions;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import mypackage.Interfaces.IExecutable;
import scriptsengine.uploadengineSC.scripSheetServices.interfaces.IXLS_Scrip_Upload_Srv;

@Service(
    "WBUploadSrvExe"
)
public class WBUploadSrvExe implements IExecutable
{
	
	@Override
	public void execute(
	        ApplicationContext appctxt
	) throws Exception
	{
		if (appctxt != null)
		{
			String filePath = "C://WBConfig//Pidilite Inds.xlsx";
			
			IXLS_Scrip_Upload_Srv uploadSrv = appctxt.getBean(IXLS_Scrip_Upload_Srv.class);
			if (uploadSrv != null)
			{
				if (uploadSrv.Upload_Scrip_from_XLS_Filepath(filePath))
				{
					System.out.println("File Uploaded");
				}
			}
			
		}
		
	}
	
}

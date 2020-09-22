package mypackage.Executions;

import java.util.ArrayList;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import mypackage.Interfaces.IExecutable;
import scriptsengine.uploadengineSC.tools.interfaces.ISheetHeadersSrv;

@Service("SheetHeaderScanSrvExe")
public class SheetHeaderScanSrvExe implements IExecutable
{

	@Override
	public void execute(ApplicationContext appctxt) throws Exception
	{
		if (appctxt != null)
		{
			ISheetHeadersSrv shHdSrv = appctxt.getBean(ISheetHeadersSrv.class);
			if (shHdSrv != null)
			{
				@SuppressWarnings("unchecked")
				// ArrayList<Integer> headers = shHdSrv.getHeadersbyWbPathandSheetName("C://WBConfig//Capacit'e
				// Infra.xlsx", "Analysis");
				ArrayList<Integer> headers = shHdSrv.getHeadersbyWbPathandSheetName("C://WBConfig//Avanti Feeds.xlsx", "Analysis");
				if (headers.size() > 0)
				{
					for ( Integer header : headers )
					{
						if (header != null)
						{
							System.out.println("Header -  " + header.toString());
						}
					}

					if (shHdSrv.getNonBlankColPosBegin() > 0)
					{
						System.out.println("First Non Blank Col Pos - " + shHdSrv.getNonBlankColPosBegin());
					}
				}
			}
		}
	}

}

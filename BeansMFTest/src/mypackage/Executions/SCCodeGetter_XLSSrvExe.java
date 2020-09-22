package mypackage.Executions;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import mypackage.Interfaces.IExecutable;
import scriptsengine.uploadengineSC.scripSheetServices.interfaces.ISCCode_Getter_XLS;

@Service("SCCodeGetter_XLSSrvExe")
public class SCCodeGetter_XLSSrvExe implements IExecutable
{

	@Override
	public void execute(ApplicationContext appctxt) throws Exception
	{
		if (appctxt != null)
		{
			String filePath = "C://WBConfig//Capacit'e Infra.xlsx";

			ISCCode_Getter_XLS scCodeXlsSrv = appctxt.getBean(ISCCode_Getter_XLS.class);
			if (scCodeXlsSrv != null)
			{
				String scCode = scCodeXlsSrv.getSCCode(filePath);
				System.out.println(scCode);
			}

		}

	}

}

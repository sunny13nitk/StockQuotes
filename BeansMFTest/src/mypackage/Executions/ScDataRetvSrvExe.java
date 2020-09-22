package mypackage.Executions;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import mypackage.Interfaces.IExecutable;
import scriptsengine.uploadengineSC.appUtilities.defn.ScCalcData_PoJo;
import scriptsengine.uploadengineSC.appUtilities.interfaces.IScDataRetvSrv;

@Service("ScDataRetvSrvExe")
public class ScDataRetvSrvExe implements IExecutable
{

	@Override
	public void execute(ApplicationContext appctxt) throws Exception
	{
		if (appctxt != null)
		{
			String scCode = "AVANTIFEED";
			IScDataRetvSrv scDataRetvSrv = appctxt.getBean(IScDataRetvSrv.class);
			if (scDataRetvSrv != null)
			{
				ScCalcData_PoJo scdataPojo = scDataRetvSrv.retrieveDataforSCCode(scCode, 5);
				if (scdataPojo != null)
				{
					System.out.println("EPS CAGR - " + scdataPojo.getEPS_CAGR());
					System.out.println("Avg. ROCE - " + scdataPojo.getAvgROCE());
					System.out.println("Avg. ROE - " + scdataPojo.getAvgROE());
					System.out.println("Avg. Div. Payout - " + scdataPojo.getDivP_Avg());
					System.out.println("Curr. DPS - " + scdataPojo.getDPS_Curr());
					System.out.println("Num. Shares (Cr.) - " + scdataPojo.getNumShares());

				}
			}

		}

	}

}

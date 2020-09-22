package mypackage.Executions;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import mypackage.Interfaces.IExecutable;
import scriptsengine.utilities.dateStringUtil.interfaces.IMonthYearInfoSrv;
import scriptsengine.utilities.dateStringUtil.types.MonthYearInfo;

@Service("MonthYrInfoSrvExe")
public class MonthYrInfoSrvExe implements IExecutable
{

	@Override
	public void execute(ApplicationContext appctxt) throws Exception
	{
		if (appctxt != null)
		{
			IMonthYearInfoSrv monYrInfoSrv = appctxt.getBean(IMonthYearInfoSrv.class);
			if (monYrInfoSrv != null)
			{
				MonthYearInfo monYrInfo = monYrInfoSrv.getMonthYearInfoforString("Mar-09", "-");
				if (monYrInfo != null)
				{
					System.out.println("It is - " + monYrInfo.getMonthInfo().getMonthNames().getLname() + " Month of Year "
					          + String.valueOf(monYrInfo.getYear()));

				}

			}
		}

	}

}

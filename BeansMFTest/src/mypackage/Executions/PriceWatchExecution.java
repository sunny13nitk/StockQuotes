package mypackage.Executions;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import mypackage.Interfaces.IExecutable;
import scriptsengine.pricingengine.reports.interfaces.IPriceWatchService;

@Service("PriceWatchExecution")
public class PriceWatchExecution implements IExecutable
{

	private ApplicationContext appCtxt;

	@Override
	public void execute(ApplicationContext appctxt) throws Exception
	{
		if (appctxt != null)
		{
			this.appCtxt = appctxt;
			triggerPriceWatch();
		}

	}

	private void triggerPriceWatch() throws Exception
	{
		IPriceWatchService pwSrv = appCtxt.getBean(IPriceWatchService.class);
		if (pwSrv != null)
		{
			pwSrv.computeandShowProjectedPrices_Console();
		}
	}

}

package scriptsengine.customizing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import scriptsengine.customizing.interfaces.IOppCostSrv;
import scriptsengine.customizing.types.TY_ScripSellBaseConfig;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OppCostSrv implements IOppCostSrv
{
	@Autowired
	private TY_ScripSellBaseConfig baseConfig;

	@Override
	public double calculateOpportunityCost(int NumDays, double AmountInvested)
	{
		double oppCost = 0;
		if (NumDays > 0 && AmountInvested > 0 && baseConfig != null)
		{
			double daysRatio = (double) NumDays / (double) baseConfig.getNumDays_oppCost();
			oppCost = AmountInvested * (baseConfig.getOppCostperAnnum() / 100) * daysRatio;
			oppCost = oppCost - oppCost * (baseConfig.getOppCostTDS() / 100 * daysRatio);
		}

		return oppCost;
	}

}

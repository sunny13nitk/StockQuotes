package scriptsengine.customizing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import scriptsengine.customizing.interfaces.ICapitalGainsSrv;
import scriptsengine.customizing.types.TY_ScripSellBaseConfig;

/**
 * Capital Gaings Service to compute Capital gains for aholding period less than LTCG period specified in base Config
 * Bean in scebeans.xml
 *
 */

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CapitalGainsSrv implements ICapitalGainsSrv
{

	@Autowired
	private TY_ScripSellBaseConfig baseConfig;

	@Override
	public double calculateCapitalGainsTax(int NumDays, double RealizedAmount)
	{
		double gainsTax = 0;

		if (NumDays > 0 && RealizedAmount > 0 && baseConfig != null)
		{
			if (NumDays > baseConfig.getNumDays_LTCG())
			{
				gainsTax = 0;
			}
			else
			{
				gainsTax = baseConfig.getCapitalGainsTaxRate() / 100 * RealizedAmount;
			}
		}

		return gainsTax;
	}

}

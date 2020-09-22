package scriptsengine.customizing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import scriptsengine.customizing.interfaces.IBrokerageFeeSrv;
import scriptsengine.customizing.types.TY_CU_DematTaxes;
import scriptsengine.portfolio.services.interfaces.IPortfolioManager;
import scriptsengine.uploadengine.exceptions.EX_General;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BrokerageFeeSrv implements IBrokerageFeeSrv
{

	@Autowired
	private IPortfolioManager	portFMgr;

	@Autowired
	private TY_CU_DematTaxes		dematTaxes;

	/**
	 * Brokerage - From AC
	 * Exchange Transaction Tax (ETT) 0.003%
	 * Service Tax 14% of Sum(Brokerage + ETT)
	 * Securities Transaction Tax 0.10%
	 * 
	 */
	@Override
	public double calculateBrokerageinclTaxes(String dematAC, double Amount) throws EX_General
	{
		double fee = 0;

		if (portFMgr != null && dematTaxes != null && Amount > 0)
		{
			if (portFMgr.validateDematAC(dematAC))
			{
				double brokerage = portFMgr.getMyDematACs().stream().filter(x -> x.getAcNum().equals(dematAC)).findFirst().get().getBrokerage();
				double bpart = Amount * (brokerage / 100) + (Amount * (dematTaxes.getETT() / 100));

				fee = bpart + ((dematTaxes.getSrvTax() / 100) * bpart) + Amount * dematTaxes.getSTT() / 100;
			}

		}

		return fee;
	}

}

package scriptsengine.dividends.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import scriptsengine.customizing.types.TY_CU_DividendTaxes;
import scriptsengine.dividends.interfaces.IDividendCalcSrv;
import scriptsengine.uploadengine.exceptions.EX_General;

/*
 * Dividend Calculation Service
 * 
 */
@Service
public class DividendCalcSrv implements IDividendCalcSrv
{

	@Autowired
	private TY_CU_DividendTaxes divTaxesConfigBean;

	/**
	 * * REturn the Dividend actually recieved on the Nett Announced dividend Amount
	 * Nett Announced Amount = Dividend Per share * Number of Shares
	 */
	@Override
	public double getCreditedDividendforAmount(double nettAnnouncedAmount) throws EX_General
	{
		double divCredit = 0;
		if (nettAnnouncedAmount > 0 && divTaxesConfigBean != null)
		{
			double tax = divTaxesConfigBean.getTax() * nettAnnouncedAmount;
			double surcharge = tax * divTaxesConfigBean.getSurcharge();
			double ecess = (tax + surcharge) * divTaxesConfigBean.getEcess();

			divCredit = nettAnnouncedAmount - (tax + surcharge + ecess);

		}

		return divCredit;
	}

}

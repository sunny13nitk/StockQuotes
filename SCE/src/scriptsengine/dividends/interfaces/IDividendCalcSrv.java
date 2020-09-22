package scriptsengine.dividends.interfaces;

import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Interface for Dividend Calculation
 */
public interface IDividendCalcSrv
{

	public double getCreditedDividendforAmount(double nettAnnouncedAmount) throws EX_General;
}

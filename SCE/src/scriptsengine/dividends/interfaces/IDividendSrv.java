package scriptsengine.dividends.interfaces;

import java.util.Date;

import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * Interface for Dividend Service
 *
 */
public interface IDividendSrv
{
	public void processDividendforScrip(String scCode, Date date, double DPS) throws EX_General;

	public void processDividendforScripDesc(String scDesc, Date date, double DPS) throws EX_General, Exception;

}

package scriptsengine.portfolio.services.interfaces;

import scriptsengine.portfolio.definitions.TY_ScripSell;
import scriptsengine.portfolio.definitions.TY_SellModeQtyAmnt;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Service Interface to determine Sell Scrip Mode from scSellHelper and Position Header
 *
 */
public interface ISellScripModeDetSrv
{

	/**
	 * ------------------------------------------------------------------------------------------------
	 * Determine and Return Scrip Sell Helper according to System Rules : Only with Scrip Sell Container
	 * Uses Portfolio Manager bean as autowired to deduce results
	 * 
	 * @param selScripDetails
	 *             - Scrip Sell Container
	 * @return - Scrip Sell Helper Instance
	 *         * @throws EX_General
	 *         -------------------------------------------------------------------------------------------
	 * 
	 */

	public TY_SellModeQtyAmnt getScripSellMode(TY_ScripSell selScripDetails) throws EX_General;

}

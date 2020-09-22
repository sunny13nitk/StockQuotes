package scriptsengine.portfolio.services.interfaces;

import scriptsengine.portfolio.definitions.TY_ScripSell;
import scriptsengine.portfolio.definitions.TY_SellModeQtyAmnt;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Interface for Scrips Sell Service
 */
public interface IScripSellService
{

	/**
	 * -------------------- SEll Scrip by Providing Scip Details--------------------------
	 * 
	 * @param scripSellDetails
	 *             - SEll Scrip Details
	 * @throws EX_General
	 *              - Exception
	 *              -----------------------------------------------------------------------------
	 */
	public void sellScrip(TY_ScripSell scripSellDetails) throws EX_General;

	/**
	 * -------------------------------------------------------------------------------------------------------------
	 * Get Scrip Sell Helper - Need this exposed from Interface since this would be acccesed by the
	 * ScripSellFI Aspect
	 * 
	 * @return - Scripy Sell Helper that has free shares eligibility, total qty, total current selling amount, Total
	 *         Current Realization amount for FI consolidation
	 *         --------------------------------------------------------------------------------------------------
	 */
	public TY_SellModeQtyAmnt getScripSellHelper();

	/**
	 * Set Scrip Sell Helper from Scrip Sell FI ASpect from Preprocess methods of the Scrip Sell Service Bean
	 * 
	 * @param scBuyHelper
	 */
	public void setScripSellHelper(TY_SellModeQtyAmnt scSellHelper);

	/**
	 * Pre Selling Process Trigger - Handled in Aspect
	 */
	public void preSellProcessTrigger(TY_ScripSell scripSellDetails);

	/**
	 * Post Selling Process Trigger - Handled in Aspect
	 */
	public void postSellProcessTrigger();

}

package scriptsengine.portfolio.services.interfaces;

import scriptsengine.enums.SCEenums;
import scriptsengine.simulations.sell.definitions.TY_Sell_Proposal;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Portfolio Services - Scrip Sell Service Interface
 *
 */
public interface IPF_ScripSellSrv
{

	/**
	 * ------------------------------ SELL Scrip from Portfolio ---------------------
	 * 
	 * @param sellMode
	 *             - Sell Mode - P&L or Free Shares
	 * @throws EX_General
	 *              - Exception
	 *              -------------------------------------------------------------------------------------
	 * @throws Exception
	 */
	public void executeScripSell(SCEenums.scripSellMode sellMode) throws Exception;

	/**
	 * -------------------------------------------------------
	 * Pre Selling Process Trigger - Handled in Aspect
	 * REfdata can either be Sell Quote or Sell Proposal
	 * ----------------------------------------------------
	 */
	public void preSellProcessTrigger(Object refData);

	/**
	 * Post Selling Process Trigger - Handled in Aspect
	 */
	public void postSellProcessTrigger();

	/**
	 * GET and SET SELL Proposals to be used in Scrip Sell FI Aspect
	 * 
	 */

	public void setSellProposal(TY_Sell_Proposal sellProposal);

	public TY_Sell_Proposal getSellProposal();

	/**
	 * Get Executed Sell Mode - to be used in Post Process FI Aspect for FI Posting for realization
	 * 
	 * @return - executed Sell Mode
	 */
	public SCEenums.scripSellMode getExeSellMode();

}

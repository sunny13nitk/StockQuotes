package scriptsengine.portfolio.services.interfaces;

import scriptsengine.portfolio.definitions.TY_DematSrcAmnt;
import scriptsengine.portfolio.definitions.TY_ScripBuy;
import scriptsengine.portfolio.definitions.TY_ScripBuySummary;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Scrip Buy Service Interface - Implmented as a Prototype bean
 *
 */
public interface IScripBuyService
{

	/**
	 * -------------------------------------------------------------------------------
	 * Buy a Scrip passing in Scrip buy container Instance
	 * 
	 * @param scripPurchaseDetails
	 *             - Scrip buy container Instance
	 * @throws EX_General
	 *              - General Exception
	 *              --------------------------------------------------------------------
	 */
	public TY_ScripBuySummary buyScrip(TY_ScripBuy scripPurchaseDetails, Boolean simulate) throws EX_General;

	/**
	 * -------------------------------------------------------------------------------------------------------------
	 * Get Scrip Buy Helper - Need this exposed from Interface since this would be acccesed by the
	 * ScripBuyFI Aspect
	 * 
	 * @return - Scripy Buy Helper that has Demat A/C, Source Parent Demat A/C, Txn. amount for FI consolidation
	 *         --------------------------------------------------------------------------------------------------
	 */
	public TY_DematSrcAmnt getScripBuyHelper();

	public void setScripBuyHelper(TY_DematSrcAmnt scBuyHelper);

	/**
	 * Pre Purchase Process Trigger - Handled in Aspect
	 */
	public void prePurchaseProcessTrigger(TY_ScripBuy scripPurchaseDetails);

	/**
	 * Post Purchase Process Trigger - Handled in Aspect
	 */
	public void postPurchaseProcessTrigger();

}

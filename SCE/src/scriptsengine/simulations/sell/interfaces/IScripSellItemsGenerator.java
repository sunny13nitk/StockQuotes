package scriptsengine.simulations.sell.interfaces;

import java.util.ArrayList;

import scriptsengine.simulations.sell.definitions.TY_SCTxn_Summary;
import scriptsengine.simulations.sell.definitions.TY_SaleEligibleItems;
import scriptsengine.simulations.sell.definitions.TY_SellProposalI;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * SCRIPS Sell Items Generator Interface
 *
 */
public interface IScripSellItemsGenerator
{

	/**
	 * ---------------------- GENERATE SELL PROPOSAL ITEMS for a Scrip and it's Txn. Summary-----------
	 * 
	 * @param scCode
	 *             - Srip Code (TO basically get hold of Positions Details from Portfolio Manager)
	 * @param tnxSummary
	 *             - Txn. Totals Summary
	 * @return - Sell Proposal Items
	 * @throws EX_General
	 *              ---------------------------------------------------------------------------------------------
	 */
	public ArrayList<TY_SellProposalI> generateSellPorposalItems(TY_SCTxn_Summary txnSummary) throws EX_General;

	/**
	 * Get Eligible Items as per LTCG to be used in Sell Proposal to Prepare Tax Perspective
	 * 
	 * @return
	 */
	public ArrayList<TY_SaleEligibleItems> getEligibleItems();

	/**
	 * Get Scrip Code
	 * 
	 * @return
	 */
	public String getScCode();

}

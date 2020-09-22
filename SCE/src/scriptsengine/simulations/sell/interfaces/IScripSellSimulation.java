package scriptsengine.simulations.sell.interfaces;

import scriptsengine.simulations.sell.definitions.TY_Sell_Proposal;
import scriptsengine.simulations.sell.definitions.TY_Sell_Quote;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Interface for Scrip Sell Simulation Service
 *
 */
public interface IScripSellSimulation
{
	/**
	 * ----------------------------------------------------------------------------------------------
	 * Generate Selling Proposal with Perspectives Free Shares and P&L for a specific Scrip Sell Quote
	 * 
	 * @param scSellQuote
	 *             - Scrip Sel Quote Container
	 * @return - Complete Selling Proposal
	 * @throws EX_General
	 *              - Exception
	 *              ----------------------------------------------------------------------------------
	 */
	public TY_Sell_Proposal generateProposalforSellQuote(TY_Sell_Quote scSellQuote) throws EX_General;

}

package scriptsengine.simulations.sell.interfaces;

import java.util.ArrayList;

import scriptsengine.simulations.sell.definitions.TY_SCTxn_Summary;
import scriptsengine.simulations.sell.definitions.TY_SaleEligibleItems;
import scriptsengine.uploadengine.exceptions.EX_General;

/*
 * Interface to determine and find the Eligible Items optimally from Positions Items of a Scrip considering Taxation and
 * LTCG
 */
public interface IScripSellEligibleItemsFinder
{

	/**
	 * -------------------------------------------------------------------------------------------------------
	 * Determine Eligible Sell Items from Scrip's Current Positions in Portfolio Manager considering Tax Implicatioons
	 * for Present and Future
	 * 
	 * @param scCode
	 *             - Scrip Code
	 * @param tnxSummary
	 *             - Txn. Totals summary
	 * @return - Eligible Sale Items
	 * @throws EX_General
	 *              ---------------------------------------------------------------------------------------------------
	 */
	public ArrayList<TY_SaleEligibleItems> getEligibleSaleItems(TY_SCTxn_Summary tnxSummary) throws EX_General;

}

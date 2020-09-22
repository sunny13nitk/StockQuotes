package scriptsengine.simulations.sell.interfaces;

import java.util.Date;

import scriptsengine.simulations.sell.definitions.TY_SCTxn_Summary;
import scriptsengine.simulations.sell.definitions.TY_Sell_Quote;

/**
 * 
 * Scrip Transaction Summary Interface - Prepare summary in terms of total Quantity, price and Average Price per Unit
 */
public interface IScTxnSummary
{
	public TY_SCTxn_Summary prepareTxnSummary(TY_Sell_Quote sellQuote);

	public TY_SCTxn_Summary prepareTxnSummary(String scCode, int Qty, double sppu, Date date);

}

package scriptsengine.customizing.interfaces;

import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * Brokerage Fee Service Interface - Implemented as a prototype Bean
 *
 */
public interface IBrokerageFeeSrv
{
	/**
	 * ------------------------------------------------------------------------------------
	 * Get Brokerage and Fee incl. of Taxes
	 * 
	 * @param dematAC
	 *             - Demat Account to Transact from
	 * @param Amount
	 *             - Amount of Trade
	 * @return - Brokerage and Fee incl. of Taxes
	 * @throws EX_General
	 *              ---------------------------------------------------------------------------
	 */
	public double calculateBrokerageinclTaxes(String dematAC, double TxnAmount) throws EX_General;

}

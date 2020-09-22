package scriptsengine.simulations.sell.definitions;

import java.util.Date;

/**
 * 
 * Sell Summary Figures and Totals
 */
public class TY_SCTxn_Summary
{
	private Date	txnDate;
	private String	scCode;
	private int	totalQty;
	private double	totalAmount;
	private double	avgPPU;

	/**
	 * @return the txnDate
	 */
	public Date getTxnDate()
	{
		return txnDate;
	}

	/**
	 * @param txnDate
	 *             the txnDate to set
	 */
	public void setTxnDate(Date txnDate)
	{
		this.txnDate = txnDate;
	}

	/**
	 * @return the scCode
	 */
	public String getScCode()
	{
		return scCode;
	}

	/**
	 * @param scCode
	 *             the scCode to set
	 */
	public void setScCode(String scCode)
	{
		this.scCode = scCode;
	}

	/**
	 * @return the totalQty
	 */
	public int getTotalQty()
	{
		return totalQty;
	}

	/**
	 * @param totalQty
	 *             the totalQty to set
	 */
	public void setTotalQty(int totalQty)
	{
		this.totalQty = totalQty;
	}

	/**
	 * @return the totalAmount
	 */
	public double getTotalAmount()
	{
		return totalAmount;
	}

	/**
	 * @param totalAmount
	 *             the totalAmount to set
	 */
	public void setTotalAmount(double totalPrice)
	{
		this.totalAmount = totalPrice;
	}

	/**
	 * @return the avgPPU
	 */
	public double getAvgPPU()
	{
		return avgPPU;
	}

	/**
	 * @param avgPPU
	 *             the avgPPU to set
	 */
	public void setAvgPPU(double avgPPU)
	{
		this.avgPPU = avgPPU;
	}

	/**
	 * 
	 */
	public TY_SCTxn_Summary()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param txnDate
	 * @param scCode
	 * @param totalQty
	 * @param totalAmount
	 * @param avgPPU
	 */
	public TY_SCTxn_Summary(Date txnDate, String scCode, int totalQty, double totalAmount, double avgPPU)
	{
		super();
		this.txnDate = txnDate;
		this.scCode = scCode;
		this.totalQty = totalQty;
		this.totalAmount = totalAmount;
		this.avgPPU = avgPPU;
	}

}

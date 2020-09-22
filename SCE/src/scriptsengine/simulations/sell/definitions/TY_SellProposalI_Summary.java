package scriptsengine.simulations.sell.definitions;

/**
 * 
 * Sell Proposal Line Items - Summarized View by Demat AC consolidated - User Presentation
 * Subset of Sell Proposal Items in Sell Proposal
 */
public class TY_SellProposalI_Summary
{

	private String	dematAC;
	private int	sellQty;
	private double	txnAmount;
	private double	taxIncurred;
	private double	feeIncurred;
	private double	oppCost;
	private double	Realization;

	/**
	 * @return the txnAmount
	 */
	public double getTxnAmount()
	{
		return txnAmount;
	}

	/**
	 * @param txnAmount
	 *             the txnAmount to set
	 */
	public void setTxnAmount(double txnAmount)
	{
		this.txnAmount = txnAmount;
	}

	/**
	 * @return the dematAC
	 */
	public String getDematAC()
	{
		return dematAC;
	}

	/**
	 * @param dematAC
	 *             the dematAC to set
	 */
	public void setDematAC(String dematAC)
	{
		this.dematAC = dematAC;
	}

	/**
	 * @return the sellQty
	 */
	public int getSellQty()
	{
		return sellQty;
	}

	/**
	 * @param sellQty
	 *             the sellQty to set
	 */
	public void setSellQty(int sellQty)
	{
		this.sellQty = sellQty;
	}

	/**
	 * @return the taxIncurred
	 */
	public double getTaxIncurred()
	{
		return taxIncurred;
	}

	/**
	 * @param taxIncurred
	 *             the taxIncurred to set
	 */
	public void setTaxIncurred(double taxIncurred)
	{
		this.taxIncurred = taxIncurred;
	}

	/**
	 * @return the feeIncurred
	 */
	public double getFeeIncurred()
	{
		return feeIncurred;
	}

	/**
	 * @param feeIncurred
	 *             the feeIncurred to set
	 */
	public void setFeeIncurred(double feeIncurred)
	{
		this.feeIncurred = feeIncurred;
	}

	/**
	 * @return the oppCost
	 */
	public double getOppCost()
	{
		return oppCost;
	}

	/**
	 * @param oppCost
	 *             the oppCost to set
	 */
	public void setOppCost(double oppCost)
	{
		this.oppCost = oppCost;
	}

	/**
	 * @return the realization
	 */
	public double getRealization()
	{
		return Realization;
	}

	/**
	 * @param realization
	 *             the realization to set
	 */
	public void setRealization(double realization)
	{
		Realization = realization;
	}

	/**
	 * 
	 */
	public TY_SellProposalI_Summary()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param dematAC
	 * @param sellQty
	 * @param txnAmount
	 * @param taxIncurred
	 * @param feeIncurred
	 * @param oppCost
	 * @param realization
	 */
	public TY_SellProposalI_Summary(String dematAC, int sellQty, double txnAmount, double taxIncurred, double feeIncurred, double oppCost,
	          double realization)
	{
		super();
		this.dematAC = dematAC;
		this.sellQty = sellQty;
		this.txnAmount = txnAmount;
		this.taxIncurred = taxIncurred;
		this.feeIncurred = feeIncurred;
		this.oppCost = oppCost;
		Realization = realization;
	}

}

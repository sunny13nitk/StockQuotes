package scriptsengine.simulations.sell.definitions;

public class TY_PandLInfo
{
	private double	totalTxnAmount;
	private double	totalTaxliability;
	private double	totalFees;
	private double	totalOppCost;
	private double	totalRealization;

	/**
	 * @return the totalOppCost
	 */
	public double getTotalOppCost()
	{
		return totalOppCost;
	}

	/**
	 * @param totalOppCost
	 *             the totalOppCost to set
	 */
	public void setTotalOppCost(double totalOppCost)
	{
		this.totalOppCost = totalOppCost;
	}

	/**
	 * @return the totalTxnAmount
	 */
	public double getTotalTxnAmount()
	{
		return totalTxnAmount;
	}

	/**
	 * @param totalTxnAmount
	 *             the totalTxnAmount to set
	 */
	public void setTotalTxnAmount(double totalTxnAmount)
	{
		this.totalTxnAmount = totalTxnAmount;
	}

	/**
	 * @return the totalTaxliability
	 */
	public double getTotalTaxliability()
	{
		return totalTaxliability;
	}

	/**
	 * @param totalTaxliability
	 *             the totalTaxliability to set
	 */
	public void setTotalTaxliability(double totalTaxliability)
	{
		this.totalTaxliability = totalTaxliability;
	}

	/**
	 * @return the totalFees
	 */
	public double getTotalFees()
	{
		return totalFees;
	}

	/**
	 * @param totalFees
	 *             the totalFees to set
	 */
	public void setTotalFees(double totalFees)
	{
		this.totalFees = totalFees;
	}

	/**
	 * @return the totalRealization
	 */
	public double getTotalRealization()
	{
		return totalRealization;
	}

	/**
	 * @param totalRealization
	 *             the totalRealization to set
	 */
	public void setTotalRealization(double totalRealization)
	{
		this.totalRealization = totalRealization;
	}

	/**
	 * 
	 */
	public TY_PandLInfo()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param totalTxnAmount
	 * @param totalTaxliability
	 * @param totalFees
	 * @param totalOppCost
	 * @param totalRealization
	 */
	public TY_PandLInfo(double totalTxnAmount, double totalTaxliability, double totalFees, double totalOppCost, double totalRealization)
	{
		super();
		this.totalTxnAmount = totalTxnAmount;
		this.totalTaxliability = totalTaxliability;
		this.totalFees = totalFees;
		this.totalOppCost = totalOppCost;
		this.totalRealization = totalRealization;
	}

}

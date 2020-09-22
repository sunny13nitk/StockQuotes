package scriptsengine.pricingengine.definitions;

/**
 * class to hold Projected Prices
 */
public class TY_ProjectedPrices
{
	private double		avgPE_PP;
	private double		maxPE_PP;
	private double		minPE_PP;
	private boolean	peAdjusted;

	/**
	 * @return the peAdjusted
	 */
	public boolean isPeAdjusted()
	{
		return peAdjusted;
	}

	/**
	 * @param peAdjusted
	 *             the peAdjusted to set
	 */
	public void setPeAdjusted(boolean peAdjusted)
	{
		this.peAdjusted = peAdjusted;
	}

	/**
	 * @return the avgPE_PP
	 */
	public double getAvgPE_PP()
	{
		return avgPE_PP;
	}

	/**
	 * @param avgPE_PP
	 *             the avgPE_PP to set
	 */
	public void setAvgPE_PP(double avgPE_PP)
	{
		this.avgPE_PP = avgPE_PP;
	}

	/**
	 * @return the maxPE_PP
	 */
	public double getMaxPE_PP()
	{
		return maxPE_PP;
	}

	/**
	 * @param maxPE_PP
	 *             the maxPE_PP to set
	 */
	public void setMaxPE_PP(double maxPE_PP)
	{
		this.maxPE_PP = maxPE_PP;
	}

	/**
	 * @return the minPE_PP
	 */
	public double getMinPE_PP()
	{
		return minPE_PP;
	}

	/**
	 * @param minPE_PP
	 *             the minPE_PP to set
	 */
	public void setMinPE_PP(double minPE_PP)
	{
		this.minPE_PP = minPE_PP;
	}

	/**
	 * @param avgPE_PP
	 * @param maxPE_PP
	 * @param minPE_PP
	 * @param peAdjusted
	 */
	public TY_ProjectedPrices(double avgPE_PP, double maxPE_PP, double minPE_PP, boolean peAdjusted)
	{
		super();
		this.avgPE_PP = avgPE_PP;
		this.maxPE_PP = maxPE_PP;
		this.minPE_PP = minPE_PP;
		this.peAdjusted = peAdjusted;
	}

	/**
	 * 
	 */
	public TY_ProjectedPrices()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}

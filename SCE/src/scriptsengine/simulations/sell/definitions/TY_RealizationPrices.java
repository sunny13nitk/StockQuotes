package scriptsengine.simulations.sell.definitions;

import scriptsengine.pricingengine.definitions.TY_ProjectedPrices;

public class TY_RealizationPrices
{

	private double				actualBuyPrice;
	private double				actualSellPrice;

	private TY_ProjectedPrices	avgPEPrices;

	private TY_ProjectedPrices	adjPEPrices;

	private boolean			PEDifferent;

	/**
	 * @return the actualBuyPrice
	 */
	public double getActualBuyPrice()
	{
		return actualBuyPrice;
	}

	/**
	 * @param actualBuyPrice
	 *             the actualBuyPrice to set
	 */
	public void setActualBuyPrice(double actualBuyPrice)
	{
		this.actualBuyPrice = actualBuyPrice;
	}

	/**
	 * @return the actualSellPrice
	 */
	public double getActualSellPrice()
	{
		return actualSellPrice;
	}

	/**
	 * @param actualSellPrice
	 *             the actualSellPrice to set
	 */
	public void setActualSellPrice(double actualSellPrice)
	{
		this.actualSellPrice = actualSellPrice;
	}

	/**
	 * @return the avgPEPrices
	 */
	public TY_ProjectedPrices getAvgPEPrices()
	{
		return avgPEPrices;
	}

	/**
	 * @param avgPEPrices
	 *             the avgPEPrices to set
	 */
	public void setAvgPEPrices(TY_ProjectedPrices avgPEPrices)
	{
		this.avgPEPrices = avgPEPrices;
	}

	/**
	 * @return the adjPEPrices
	 */
	public TY_ProjectedPrices getAdjPEPrices()
	{
		return adjPEPrices;
	}

	/**
	 * @param adjPEPrices
	 *             the adjPEPrices to set
	 */
	public void setAdjPEPrices(TY_ProjectedPrices adjPEPrices)
	{
		this.adjPEPrices = adjPEPrices;
	}

	/**
	 * @return the pEDifferent
	 */
	public boolean isPEDifferent()
	{
		return PEDifferent;
	}

	/**
	 * @param pEDifferent
	 *             the pEDifferent to set
	 */
	public void setPEDifferent(boolean pEDifferent)
	{
		PEDifferent = pEDifferent;
	}

	/**
	 * 
	 */
	public TY_RealizationPrices()
	{
		super();
		this.adjPEPrices = new TY_ProjectedPrices();
		this.avgPEPrices = new TY_ProjectedPrices();
	}

	/**
	 * @param actualBuyPrice
	 * @param actualSellPrice
	 * @param avgPEPrices
	 * @param adjPEPrices
	 * @param pEDifferent
	 */
	public TY_RealizationPrices(double actualBuyPrice, double actualSellPrice, TY_ProjectedPrices avgPEPrices, TY_ProjectedPrices adjPEPrices,
	          boolean pEDifferent)
	{
		super();
		this.actualBuyPrice = actualBuyPrice;
		this.actualSellPrice = actualSellPrice;
		this.avgPEPrices = avgPEPrices;
		this.adjPEPrices = adjPEPrices;
		PEDifferent = pEDifferent;
	}

}

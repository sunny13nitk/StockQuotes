package scriptsengine.simulations.sell.definitions;

/**
 * Prices and Realizations POJO for a SEll Simulation - to be a part of Sell Proposal
 *
 */
public class TY_SS_PricesRealizations
{

	private TY_RealizationPrices	prices;
	private TY_SS_RealRatios		ratios;
	private TY_SS_RelRealizations	realizations;

	/**
	 * @return the prices
	 */
	public TY_RealizationPrices getPrices()
	{
		return prices;
	}

	/**
	 * @param prices
	 *             the prices to set
	 */
	public void setPrices(TY_RealizationPrices prices)
	{
		this.prices = prices;
	}

	/**
	 * @return the ratios
	 */
	public TY_SS_RealRatios getRatios()
	{
		return ratios;
	}

	/**
	 * @param ratios
	 *             the ratios to set
	 */
	public void setRatios(TY_SS_RealRatios ratios)
	{
		this.ratios = ratios;
	}

	/**
	 * @return the realizations
	 */
	public TY_SS_RelRealizations getRealizations()
	{
		return realizations;
	}

	/**
	 * @param realizations
	 *             the realizations to set
	 */
	public void setRealizations(TY_SS_RelRealizations realizations)
	{
		this.realizations = realizations;
	}

	/**
	 * 
	 */
	public TY_SS_PricesRealizations()
	{
		super();
		this.prices = new TY_RealizationPrices();
		this.ratios = new TY_SS_RealRatios();
		this.realizations = new TY_SS_RelRealizations();
	}

	/**
	 * @param prices
	 * @param ratios
	 * @param realizations
	 */
	public TY_SS_PricesRealizations(TY_RealizationPrices prices, TY_SS_RealRatios ratios, TY_SS_RelRealizations realizations)
	{
		super();
		this.prices = prices;
		this.ratios = ratios;
		this.realizations = realizations;
	}

}

package scriptsengine.simulations.sell.definitions;

public class TY_SS_RealRatios
{
	private double				actualBuytoSellRealization;
	private TY_RealizationRatios	avgPERealizationRatios;
	private TY_RealizationRatios	adjPERealizationRatios;

	/**
	 * @return the actualBuytoSellRealization
	 */
	public double getActualBuytoSellRealization()
	{
		return actualBuytoSellRealization;
	}

	/**
	 * @param actualBuytoSellRealization
	 *             the actualBuytoSellRealization to set
	 */
	public void setActualBuytoSellRealization(double actualBuytoSellRealization)
	{
		this.actualBuytoSellRealization = actualBuytoSellRealization;
	}

	/**
	 * @return the avgPERealizationRatios
	 */
	public TY_RealizationRatios getAvgPERealizationRatios()
	{
		return avgPERealizationRatios;
	}

	/**
	 * @param avgPERealizationRatios
	 *             the avgPERealizationRatios to set
	 */
	public void setAvgPERealizationRatios(TY_RealizationRatios avgPERealizationRatios)
	{
		this.avgPERealizationRatios = avgPERealizationRatios;
	}

	/**
	 * @return the adjPERealizationRatios
	 */
	public TY_RealizationRatios getAdjPERealizationRatios()
	{
		return adjPERealizationRatios;
	}

	/**
	 * @param adjPERealizationRatios
	 *             the adjPERealizationRatios to set
	 */
	public void setAdjPERealizationRatios(TY_RealizationRatios adjPERealizationRatios)
	{
		this.adjPERealizationRatios = adjPERealizationRatios;
	}

	/**
	 * 
	 */
	public TY_SS_RealRatios()
	{
		super();
		this.adjPERealizationRatios = new TY_RealizationRatios();
		this.avgPERealizationRatios = new TY_RealizationRatios();

	}

	/**
	 * @param actualBuytoSellRealization
	 * @param avgPERealizationRatios
	 * @param adjPERealizationRatios
	 */
	public TY_SS_RealRatios(double actualBuytoSellRealization, TY_RealizationRatios avgPERealizationRatios,
	          TY_RealizationRatios adjPERealizationRatios)
	{
		super();
		this.actualBuytoSellRealization = actualBuytoSellRealization;
		this.avgPERealizationRatios = avgPERealizationRatios;
		this.adjPERealizationRatios = adjPERealizationRatios;
	}

}

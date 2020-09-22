package scriptsengine.simulations.sell.definitions;

public class TY_RealizationRatios
{
	private double	actualBuytoAvgPrice;
	private double	actualBuytoMaxPrice;

	/**
	 * @return the actualBuytoAvgPrice
	 */
	public double getActualBuytoAvgPrice()
	{
		return actualBuytoAvgPrice;
	}

	/**
	 * @param actualBuytoAvgPrice
	 *             the actualBuytoAvgPrice to set
	 */
	public void setActualBuytoAvgPrice(double actualBuytoAvgPrice)
	{
		this.actualBuytoAvgPrice = actualBuytoAvgPrice;
	}

	/**
	 * @return the actualBuytoMaxPrice
	 */
	public double getActualBuytoMaxPrice()
	{
		return actualBuytoMaxPrice;
	}

	/**
	 * @param actualBuytoMaxPrice
	 *             the actualBuytoMaxPrice to set
	 */
	public void setActualBuytoMaxPrice(double actualBuytoMaxPrice)
	{
		this.actualBuytoMaxPrice = actualBuytoMaxPrice;
	}

	/**
	 * 
	 */
	public TY_RealizationRatios()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param actualBuytoAvgPrice
	 * @param actualBuytoMaxPrice
	 */
	public TY_RealizationRatios(double actualBuytoAvgPrice, double actualBuytoMaxPrice)
	{
		super();
		this.actualBuytoAvgPrice = actualBuytoAvgPrice;
		this.actualBuytoMaxPrice = actualBuytoMaxPrice;
	}

}

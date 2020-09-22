package scriptsengine.simulations.sell.definitions;

public class TY_RelRealization
{
	private double	relRealizationAvgPrice;
	private double	relRealizationMaxPrice;

	/**
	 * @return the relRealizationAvgPrice
	 */
	public double getRelRealizationAvgPrice()
	{
		return relRealizationAvgPrice;
	}

	/**
	 * @param relRealizationAvgPrice
	 *             the relRealizationAvgPrice to set
	 */
	public void setRelRealizationAvgPrice(double relRealizationAvgPrice)
	{
		this.relRealizationAvgPrice = relRealizationAvgPrice;
	}

	/**
	 * @return the relRealizationMaxPrice
	 */
	public double getRelRealizationMaxPrice()
	{
		return relRealizationMaxPrice;
	}

	/**
	 * @param relRealizationMaxPrice
	 *             the relRealizationMaxPrice to set
	 */
	public void setRelRealizationMaxPrice(double relRealizationMaxPrice)
	{
		this.relRealizationMaxPrice = relRealizationMaxPrice;
	}

	/**
	 * @param relRealizationAvgPrice
	 * @param relRealizationMaxPrice
	 */
	public TY_RelRealization(double relRealizationAvgPrice, double relRealizationMaxPrice)
	{
		super();
		this.relRealizationAvgPrice = relRealizationAvgPrice;
		this.relRealizationMaxPrice = relRealizationMaxPrice;
	}

	/**
	 * 
	 */
	public TY_RelRealization()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}

package scriptsengine.pricingengine.definitions;

/**
 * 
 * Prices Return Structure
 */
public class TY_PricesReturn
{
	private String				priceType;
	private double				CYPP;			// Current year Projected Profit
	private double				CYPEPUS;			// Current year Projected Earning Per unit Value of Share
	private TY_ProjectedPrices	projectedPrices;

	/**
	 * @return the priceType
	 */
	public String getPriceType()
	{
		return priceType;
	}

	/**
	 * @param priceType
	 *             the priceType to set
	 */
	public void setPriceType(String priceType)
	{
		this.priceType = priceType;
	}

	/**
	 * @return the cYPP
	 */
	public double getCYPP()
	{
		return CYPP;
	}

	/**
	 * @param cYPP
	 *             the cYPP to set
	 */
	public void setCYPP(double cYPP)
	{
		CYPP = cYPP;
	}

	/**
	 * @return the cYPE
	 */
	public double getCYPEPUS()
	{
		return CYPEPUS;
	}

	/**
	 * @param cYPE
	 *             the cYPE to set
	 */
	public void setCYPEPUS(double cYPE)
	{
		CYPEPUS = cYPE;
	}

	/**
	 * @return the projectedPrices
	 */
	public TY_ProjectedPrices getProjectedPrices()
	{
		return projectedPrices;
	}

	/**
	 * @param projectedPrices
	 *             the projectedPrices to set
	 */
	public void setProjectedPrices(TY_ProjectedPrices projectedPrices)
	{
		this.projectedPrices = projectedPrices;
	}

	/**
	 * @param priceType
	 * @param cYPP
	 * @param cYPE
	 * @param projectedPrices
	 */
	public TY_PricesReturn(String priceType, double cYPP, double cYPE, TY_ProjectedPrices projectedPrices)
	{
		super();
		this.priceType = priceType;
		CYPP = cYPP;
		CYPEPUS = cYPE;
		this.projectedPrices = projectedPrices;
	}

	/**
	 * 
	 */
	public TY_PricesReturn()
	{
		super();
		this.projectedPrices = new TY_ProjectedPrices();
	}

}

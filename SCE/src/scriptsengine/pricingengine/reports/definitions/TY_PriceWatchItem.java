package scriptsengine.pricingengine.reports.definitions;

import scriptsengine.pricingengine.definitions.TY_ProjectedPrices;

/**
 * Price Watch Item Type Class
 *
 */
public class TY_PriceWatchItem
{
	private String				scCode;
	private String				scDesc;

	private double				unadj_avgPE;
	private double				avgPE;
	private double				secPE;

	private TY_ProjectedPrices	unadj_projPrices;
	private TY_ProjectedPrices	projPrices;

	private double				DMA_200;
	private double				promoterHolding;

	private double				deltaAvgMin;
	private double				deltaAvgMax;

	private double				PEadj_per;

	/**
	 * @return the dMA_200
	 */
	public double getDMA_200()
	{
		return DMA_200;
	}

	/**
	 * @param dMA_200
	 *             the dMA_200 to set
	 */
	public void setDMA_200(double dMA_200)
	{
		DMA_200 = dMA_200;
	}

	/**
	 * @return the promoterHolding
	 */
	public double getPromoterHolding()
	{
		return promoterHolding;
	}

	/**
	 * @param promoterHolding
	 *             the promoterHolding to set
	 */
	public void setPromoterHolding(double promoterHolding)
	{
		this.promoterHolding = promoterHolding;
	}

	/**
	 * @return the unadj_projPrices
	 */
	public TY_ProjectedPrices getUnadj_projPrices()
	{
		return unadj_projPrices;
	}

	/**
	 * @param unadj_projPrices
	 *             the unadj_projPrices to set
	 */
	public void setUnadj_projPrices(TY_ProjectedPrices unadj_projPrices)
	{
		this.unadj_projPrices = unadj_projPrices;
	}

	/**
	 * @return the unadj_avgPE
	 */
	public double getUnadj_avgPE()
	{
		return unadj_avgPE;
	}

	/**
	 * @param unadj_avgPE
	 *             the unadj_avgPE to set
	 */
	public void setUnadj_avgPE(double unadj_avgPE)
	{
		this.unadj_avgPE = unadj_avgPE;
	}

	/**
	 * @return the pEadj_per
	 */
	public double getPEadj_per()
	{
		return PEadj_per;
	}

	/**
	 * @param pEadj_per
	 *             the pEadj_per to set
	 */
	public void setPEadj_per(double pEadj_per)
	{
		PEadj_per = pEadj_per;
	}

	/**
	 * @return the avgPE
	 */
	public double getAvgPE()
	{
		return avgPE;
	}

	/**
	 * @param avgPE
	 *             the avgPE to set
	 */
	public void setAvgPE(double avgPE)
	{
		this.avgPE = avgPE;
	}

	/**
	 * @return the secPE
	 */
	public double getSecPE()
	{
		return secPE;
	}

	/**
	 * @param secPE
	 *             the secPE to set
	 */
	public void setSecPE(double secPE)
	{
		this.secPE = secPE;
	}

	/**
	 * @return the deltaAvgMin
	 */
	public double getDeltaAvgMin()
	{
		return deltaAvgMin;
	}

	/**
	 * @param deltaAvgMin
	 *             the deltaAvgMin to set
	 */
	public void setDeltaAvgMin(double deltaAvgMin)
	{
		this.deltaAvgMin = deltaAvgMin;
	}

	/**
	 * @return the deltaAvgMax
	 */
	public double getDeltaAvgMax()
	{
		return deltaAvgMax;
	}

	/**
	 * @param deltaAvgMax
	 *             the deltaAvgMax to set
	 */
	public void setDeltaAvgMax(double deltaAvgMax)
	{
		this.deltaAvgMax = deltaAvgMax;
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
	 * @return the scDesc
	 */
	public String getScDesc()
	{
		return scDesc;
	}

	/**
	 * @param scDesc
	 *             the scDesc to set
	 */
	public void setScDesc(String scDesc)
	{
		this.scDesc = scDesc;
	}

	/**
	 * @return the projPrices
	 */
	public TY_ProjectedPrices getProjPrices()
	{
		return projPrices;
	}

	/**
	 * @param projPrices
	 *             the projPrices to set
	 */
	public void setProjPrices(TY_ProjectedPrices projPrices)
	{
		this.projPrices = projPrices;
	}

	/**
	 * 
	 */
	public TY_PriceWatchItem()
	{
		super();
		this.projPrices = new TY_ProjectedPrices();
		this.unadj_projPrices = new TY_ProjectedPrices();
	}

	/**
	 * @param scCode
	 * @param scDesc
	 * @param unadj_avgPE
	 * @param avgPE
	 * @param secPE
	 * @param unadj_projPrices
	 * @param projPrices
	 * @param dMA_200
	 * @param promoterHolding
	 * @param deltaAvgMin
	 * @param deltaAvgMax
	 * 
	 * @param pEadj_per
	 */
	public TY_PriceWatchItem(String scCode, String scDesc, double unadj_avgPE, double avgPE, double secPE, TY_ProjectedPrices unadj_projPrices,
	          TY_ProjectedPrices projPrices, double dMA_200, double promoterHolding, double deltaAvgMin, double deltaAvgMax, double pEadj_per)
	{
		super();
		this.scCode = scCode;
		this.scDesc = scDesc;
		this.unadj_avgPE = unadj_avgPE;
		this.avgPE = avgPE;
		this.secPE = secPE;
		this.projPrices = new TY_ProjectedPrices();
		this.unadj_projPrices = new TY_ProjectedPrices();
		DMA_200 = dMA_200;
		this.promoterHolding = promoterHolding;
		this.deltaAvgMin = deltaAvgMin;
		this.deltaAvgMax = deltaAvgMax;

		PEadj_per = pEadj_per;
	}

}
